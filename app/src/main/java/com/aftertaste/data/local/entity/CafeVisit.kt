package com.aftertaste.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cafe_visits")
data class CafeVisit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cafeName: String,
    val location: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val visitDate: Long = System.currentTimeMillis(),
    val pricePaid: Double? = null,
    val overallRating: Float = 0f,
    val tasteRating: Float? = null,
    val ambienceRating: Float? = null,
    val seatingRating: Float? = null,
    val wifiRating: Float? = null,
    val serviceRating: Float? = null,
    val noiseLevel: String? = null,
    val wouldReturn: Boolean = true,
    val notes: String? = null,
    val powerOutlets: Boolean? = null,
    val crowdLevel: String? = null,
    val wifiPassword: String? = null,
    val seatingType: String? = null
)