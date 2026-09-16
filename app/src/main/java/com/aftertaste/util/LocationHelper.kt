package com.aftertaste.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class NearbyCafeSpot(
    val id: String,
    val name: String,
    val address: String,
    val distanceKm: Double,
    val rating: Float,
    val reviewCount: Int,
    val lat: Double,
    val lng: Double,
    val isOpenNow: Boolean = true,
    val tags: List<String> = emptyList()
)

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation || coarseLocation
    }

    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(context: Context): Location? {
        if (!hasLocationPermission(context)) return null

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    continuation.resume(location)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    suspend fun fetchNearbyCafes(lat: Double, lng: Double, apiKey: String): List<NearbyCafeSpot> =
        withContext(Dispatchers.IO) {
            if (apiKey.isBlank() || apiKey == "AIzaSy_YOUR_MAPS_API_KEY_HERE") {
                return@withContext emptyList()
            }

            try {
                val urlString =
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lat,$lng&radius=2000&type=cafe&key=$apiKey"
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val responseCode = connection.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    return@withContext emptyList()
                }

                val jsonText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonText)
                val status = jsonObject.optString("status")

                if (status != "OK" && status != "ZERO_RESULTS") {
                    return@withContext emptyList()
                }

                val resultsArray = jsonObject.optJSONArray("results") ?: return@withContext emptyList()
                val spots = mutableListOf<NearbyCafeSpot>()

                for (i in 0 until resultsArray.length()) {
                    val placeObj = resultsArray.getJSONObject(i)
                    val placeId = placeObj.optString("place_id", "place_$i")
                    val name = placeObj.optString("name", "Cafe")
                    val address = placeObj.optString("vicinity", "Nearby")
                    val rating = placeObj.optDouble("rating", 0.0).toFloat()
                    val userRatingsTotal = placeObj.optInt("user_ratings_total", 0)

                    val geometryObj = placeObj.optJSONObject("geometry")
                    val locationObj = geometryObj?.optJSONObject("location")
                    val cafeLat = locationObj?.optDouble("lat") ?: continue
                    val cafeLng = locationObj?.optDouble("lng") ?: continue

                    val openingHoursObj = placeObj.optJSONObject("opening_hours")
                    val openNow = openingHoursObj?.optBoolean("open_now", true) ?: true

                    val dist = calculateDistanceKm(lat, lng, cafeLat, cafeLng)

                    val typesArray = placeObj.optJSONArray("types")
                    val tagsList = mutableListOf<String>()
                    if (typesArray != null) {
                        for (j in 0 until typesArray.length()) {
                            val type = typesArray.getString(j)
                            if (type != "cafe" && type != "establishment" && type != "food" && type != "point_of_interest") {
                                tagsList.add(type.replace("_", " ").capitalizeWords())
                            }
                        }
                    }

                    spots.add(
                        NearbyCafeSpot(
                            id = placeId,
                            name = name,
                            address = address,
                            distanceKm = dist,
                            rating = rating,
                            reviewCount = userRatingsTotal,
                            lat = cafeLat,
                            lng = cafeLng,
                            isOpenNow = openNow,
                            tags = tagsList.take(3)
                        )
                    )
                }

                spots.sortedBy { it.distanceKm }
            } catch (e: Exception) {
                emptyList()
            }
        }

    private fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}
