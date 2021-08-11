package it.dbortoluzzi.tuttiapposto.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import it.dbortoluzzi.tuttiapposto.framework.BookingAlarmReceiver
import java.util.*

class NotificationUtils {

    fun sendNotification(timeInMilliSeconds: Long, context: Context) {

        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, BookingAlarmReceiver::class.java) // AlarmReceiver1 = broadcast receiver

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMilliSeconds

        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun sendAtTimeNotification(hour: Int, minute: Int, isRecurrent: Boolean, context: Context) {

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0

        if (Calendar.getInstance().after(calendar)) {
            calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] +1
        }

        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, BookingAlarmReceiver::class.java) // AlarmReceiver1 = broadcast receiver

        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        Log.d(TAG, "sendAtTimeNotification for ${calendar.time.toString()}")

        if(!isRecurrent)
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,pendingIntent);
        else
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    companion object {
        val TAG = "NotificationUtils"
    }
}