package it.dbortoluzzi.tuttiapposto.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.tuttiapposto.ui.activities.MainActivity
import it.dbortoluzzi.usecases.GetBookings
import it.dbortoluzzi.usecases.GetUser
import javax.inject.Inject

@AndroidEntryPoint
class BookingNotificationService : Service() {

    private lateinit var mNotification: Notification
    private val mNotificationId: Int = 1000

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        execNotification()

        stopForeground(false)
        return START_NOT_STICKY
    }

    private fun execNotification() {
        //Create Channel
        createChannel()

        val context = this.applicationContext
        val notifyIntent = Intent(this, MainActivity::class.java)

        val title = getString(R.string.reminder_today_booking_title)
        val message = getString(R.string.reminder_today_booking_desc) + "\n" + getString(R.string.reminder_click_here_to_view)

        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("message", message)
        notifyIntent.putExtra(SHOW_BOOKINGS, true)

        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val res = this.resources
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotification = Notification.Builder(this, CHANNEL_ID)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.outline_bookmark_add)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_round))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                            .bigText(message))
                    .setContentText(message).build()
        } else {
            mNotification = Notification.Builder(this)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.outline_bookmark_add)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_round))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                            .bigText(message))
                    .setSound(uri)
                    .setContentText(message).build()
        }
        startForeground(mNotificationId, mNotification)
    }

    @SuppressLint("NewApi")
    private fun createChannel() {
        val context = this.applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
        notificationChannel.enableVibration(false)
        notificationChannel.setShowBadge(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.parseColor("#e8334a")
        notificationChannel.description = getString(R.string.reminder_today_booking_title)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.cancelAll()
    }

    companion object {
        const val CHANNEL_ID = "it.dbortoluzzi.tuttiapposto.framework.NotificationServices.CHANNEL_ID"
        const val CHANNEL_NAME = "REMINDER_TODAY_BOOKING"
        const val SHOW_BOOKINGS = "SHOW_BOOKINGS"
        const val TAG = "BookingNotificationServ"
    }
}