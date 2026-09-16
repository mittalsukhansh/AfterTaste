package com.aftertaste.util

import android.content.Context
import android.location.Location
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
    val tags: List<String> = listOf("Artisan", "Espresso", "WiFi")
)

object LocationHelper {

    // Default reference center location (or detected position)
    const val DEFAULT_LAT = 37.7749
    const val DEFAULT_LNG = -122.4194

    // Dynamic nearby cafe templates generated dynamically around user's location
    private val CAFE_NAME_TEMPLATES = listOf(
        Pair("Artisan Roasters", "124 Main St"),
        Pair("Velvet Espresso Bar", "45 Coffee Way"),
        Pair("The Grind & Leaf", "88 Park Ave"),
        Pair("Cafe Arabica", "310 Station Square"),
        Pair("Blue Bottle Corner", "204 Market St"),
        Pair("Morning Brew Lounge", "15 High St"),
        Pair("Matcha & Mocha", "51 Urban Alley"),
        Pair("Craft Coffee Lab", "92 Boulevard"),
        Pair("Crema & Sugar", "118 Pine St"),
        Pair("Espresso Symphony", "77 Elm St")
    )

    fun fetchNearbyCafes(userLat: Double = DEFAULT_LAT, userLng: Double = DEFAULT_LNG): List<NearbyCafeSpot> {
        return CAFE_NAME_TEMPLATES.mapIndexed { index, (name, address) ->
            // Distribute cafes dynamically around user's position
            val angle = (index * 36) * (Math.PI / 180.0)
            val radiusKm = 0.2 + (index * 0.25)

            // Convert distance offset to lat/lng degrees approx
            val latOffset = (radiusKm / 111.0) * cos(angle)
            val lngOffset = (radiusKm / (111.0 * cos(Math.toRadians(userLat)))) * sin(angle)

            val cafeLat = userLat + latOffset
            val cafeLng = userLng + lngOffset

            val dist = calculateDistanceKm(userLat, userLng, cafeLat, cafeLng)

            NearbyCafeSpot(
                id = "nearby_${index}_${name.lowercase().replace(" ", "_")}",
                name = name,
                address = address,
                distanceKm = dist,
                rating = 4.2f + ((index % 8) * 0.1f),
                reviewCount = 35 + (index * 22),
                lat = cafeLat,
                lng = cafeLng,
                isOpenNow = index % 5 != 4,
                tags = when (index % 4) {
                    0 -> listOf("Pour Over", "Single Origin", "Cozy")
                    1 -> listOf("Espresso Bar", "Outdoor Seating", "WiFi")
                    2 -> listOf("Pastries", "Matcha", "Quiet Work")
                    else -> listOf("Specialty Beans", "Cold Brew", "Pet Friendly")
                }
            )
        }.sortedBy { it.distanceKm }
    }

    private fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Radius of Earth in Km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
