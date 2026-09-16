package com.aftertaste.data.local.entity

/**
 * Derived aggregate object representing a cafe across all visits & wishlist status.
 */
data class Cafe(
    val cafeName: String,
    val location: String = "",
    val visitCount: Int = 0,
    val avgOverallRating: Float = 0f,
    val lastVisited: Long = 0L,
    val isWishlist: Boolean = false,
    val coverPhotoUri: String? = null
)