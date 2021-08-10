package it.dbortoluzzi.tuttiapposto.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import it.dbortoluzzi.tuttiapposto.framework.AlarmReceiver
import java.util.*

class NotificationUtils {

    fun sendNotification(timeInMilliSeconds: Long, context: Context) {

        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java) // AlarmReceiver1 = broadcast receiver

        alarmIntent.putExtra("reason", "notification")
        alarmIntent.putExtra("timestamp", timeInMilliSeconds)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMilliSeconds

        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}