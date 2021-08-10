package it.dbortoluzzi.tuttiapposto.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.tuttiapposto.utils.NotificationUtils


@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // TODO: check why it doesn't work
//        Log.d(TAG, TAG + "onReceive")
//        NotificationUtils().sendNotification(3000, context)
    }

    companion object {
        val TAG = "BootReceiver"
    }
}