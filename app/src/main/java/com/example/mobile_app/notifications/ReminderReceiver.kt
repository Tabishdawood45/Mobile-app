package com.example.mobile_app.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mobile_app.R

class ReminderReceiver : BroadcastReceiver() {
    private var ringtone: Ringtone? = null

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            // Retrieve the note from the Intent
            val note = intent?.getStringExtra("NOTE") ?: "Time for your breathing exercise!" // Default message if no note

            // Show Toast with the note
            Toast.makeText(it, note, Toast.LENGTH_LONG).show()

            // Get default alarm ringtone
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ringtone = RingtoneManager.getRingtone(it, alarmUri)

            // Play ringtone
            ringtone?.play()

            // Start listening for volume button press
            listenForVolumeChange(it)

            // Dismiss Action in Notification
            val dismissIntent = Intent(it, ReminderReceiver::class.java).apply {
                action = "STOP_ALARM"
            }
            val dismissPendingIntent = PendingIntent.getBroadcast(
                it, 1, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Send Notification with the note
            val builder = NotificationCompat.Builder(it, "reminder_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Reminder!")
                .setContentText(note) // Show the note in the notification
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground, "Dismiss", dismissPendingIntent) // Add Dismiss button

            val notificationManager = NotificationManagerCompat.from(it)
            notificationManager.notify(1, builder.build())
        }

        // Stop alarm when notification dismiss action is clicked
        if (intent?.action == "STOP_ALARM") {
            stopAlarm(context)
        }
    }

    private fun stopAlarm(context: Context?) {
        ringtone?.stop()
        ringtone = null
        context?.contentResolver?.unregisterContentObserver(volumeObserver) // Stop listening for volume changes
    }

    private val volumeObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            stopAlarm(null) // Stop alarm when volume changes
        }
    }

    private fun listenForVolumeChange(context: Context) {
        context.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI, true, volumeObserver
        )
    }
}
