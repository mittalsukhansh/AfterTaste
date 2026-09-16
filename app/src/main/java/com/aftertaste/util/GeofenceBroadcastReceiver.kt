package com.aftertaste.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aftertaste.MainActivity
import com.aftertaste.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences ?: return

            for (geofence in triggeringGeofences) {
                val cafeId = geofence.requestId
                val cafeName = intent.getStringExtra("cafe_name_$cafeId") ?: "Nearby Cafe"
                showNearbyCafeNotification(context, cafeName)
            }
        }
    }

    private fun showNearbyCafeNotification(context: Context, cafeName: String) {
        val channelId = "cafe_nearby_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Nearby Cafe Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when you are near an artisanal coffee spot"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("prefilled_cafe_name", cafeName)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            cafeName.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Near $cafeName ☕")
            .setContentText("You're near $cafeName — tap to log a visit in AfterTaste!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(cafeName.hashCode(), notification)
    }
}
