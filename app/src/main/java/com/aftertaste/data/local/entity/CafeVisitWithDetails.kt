package com.aftertaste.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CafeVisitWithDetails(
    @Embedded val visit: CafeVisit,

    @Relation(
        parentColumn = "id",
        entityColumn = "visitId"
    )
    val items: List<OrderedItem> = emptyList(),

    @Relation(
        parentColumn = "id",
        entityColumn = "visitId"
    )
    val tags: List<Tag> = emptyList(),

    @Relation(
        parentColumn = "id",
        entityColumn = "visitId"
    )
    val photos: List<Photo> = emptyList()
)