package com.aftertaste.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_cafes")
data class WishlistCafe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cafeName: String,
    val location: String = "",
    val notes: String? = null,
    val addedDate: Long = System.currentTimeMillis()
)