package com.aftertaste.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

object GeofenceManager {

    private fun getGeofencingClient(context: Context): GeofencingClient {
        return LocationServices.getGeofencingClient(context)
    }

    private fun createGeofencePendingIntent(context: Context, cafes: List<NearbyCafeSpot>): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        cafes.forEach { cafe ->
            intent.putExtra("cafe_name_${cafe.id}", cafe.name)
        }
        return PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun registerCafeGeofences(context: Context, cafes: List<NearbyCafeSpot>) {
        if (cafes.isEmpty()) return

        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocation) return

        val geofencingClient = getGeofencingClient(context)
        val pendingIntent = createGeofencePendingIntent(context, cafes)

        val geofences = cafes.take(100).map { cafe ->
            Geofence.Builder()
                .setRequestId(cafe.id)
                .setCircularRegion(cafe.lat, cafe.lng, 75f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()
        }

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofences)
            .build()

        geofencingClient.removeGeofences(pendingIntent).addOnCompleteListener {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
        }
    }
}
