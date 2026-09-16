package com.aftertaste.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ordered_items",
    foreignKeys = [
        ForeignKey(
            entity = CafeVisit::class,
            parentColumns = ["id"],
            childColumns = ["visitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["visitId"])]
)
data class OrderedItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val visitId: Long,
    val itemName: String,
    val itemPrice: Double? = null,
    val itemRating: Float? = null
)