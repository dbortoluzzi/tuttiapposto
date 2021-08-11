package it.dbortoluzzi.tuttiapposto.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.services.BookingNotificationService
import it.dbortoluzzi.tuttiapposto.utils.TuttiAppostoUtils
import it.dbortoluzzi.usecases.GetBookings
import it.dbortoluzzi.usecases.GetUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BookingAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var bookingNotificationService: BookingNotificationService

    @Inject
    lateinit var getBookings: GetBookings

    @Inject
    lateinit var getUser: GetUser

    override fun onReceive(context: Context, intent: Intent) {
        if (PrefsValidator.isConfigured(prefs)) {
            val startCal = TuttiAppostoUtils.resetCal(Calendar.getInstance())
            val startDate = startCal.time
            startCal.set(Calendar.DAY_OF_YEAR, startCal.get(Calendar.DAY_OF_YEAR) + 1)
            val endDate = startCal.time
            val user = getUser()
            when (user) {
                is ServiceResult.Success -> {
                    Log.d(BookingNotificationService.TAG, "Found user ${user.data.email}")
                    GlobalScope.launch(Dispatchers.IO) {
                        val bookings = withContext(Dispatchers.IO) { getBookings(user.data.uID, prefs.companyUId!!, null, null, startDate, endDate) }
                        Log.d(BookingNotificationService.TAG, "Found ${bookings.size} bookings")
                        if (bookings.isNotEmpty()) {
                            val service = Intent(context, bookingNotificationService::class.java)
                            service.putExtra("reason", intent.getStringExtra("reason"))
                            service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                Log.d(BookingNotificationService.TAG, "Exec notification for user = ${user.data.email} and date = ${startDate.toString()}")
                                context.startForegroundService(service)
                            }
                        }
                    }
                }
                else -> return
            }
        }
    }


}