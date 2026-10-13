package com.aftertaste.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffee_beans")
data class Bean(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val roaster: String,
    val name: String,
    val origin: String,
    val process: String = "Washed",
    val variety: String? = null,
    val elevation: String? = null,
    val roastDate: Long = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L, // Default ~7 days rested
    val weightTotal: Float = 250f,
    val weightRemaining: Float = 250f,
    val tastingNotes: String = "",
    val status: String = "ACTIVE", // ACTIVE, FINISHED, WISHLIST
    val notes: String? = null,
    val photoUri: String? = null
)
