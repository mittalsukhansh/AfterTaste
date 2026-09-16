package com.aftertaste.repository

import com.aftertaste.data.local.entity.Cafe
import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Photo
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe
import kotlinx.coroutines.flow.Flow

interface CafeRepository {
    // Visit Operations
    fun getAllVisits(): Flow<List<CafeVisitWithDetails>>
    fun getVisitById(visitId: Long): Flow<CafeVisitWithDetails?>
    fun getVisitsForCafe(cafeName: String): Flow<List<CafeVisitWithDetails>>
    fun searchVisits(query: String): Flow<List<CafeVisitWithDetails>>
    suspend fun saveVisit(
        visit: CafeVisit,
        items: List<OrderedItem> = emptyList(),
        tags: List<Tag> = emptyList(),
        photos: List<Photo> = emptyList()
    ): Long
    suspend fun deleteVisit(visitId: Long)

    // Aggregate Cafe List
    fun getAllCafes(): Flow<List<Cafe>>

    // Wishlist Operations
    fun getAllWishlistCafes(): Flow<List<WishlistCafe>>
    fun isWishlistCafe(cafeName: String): Flow<Boolean>
    suspend fun addToWishlist(wishlistCafe: WishlistCafe): Long
    suspend fun removeFromWishlist(id: Long)
    suspend fun removeFromWishlistByName(cafeName: String)

    // Tags
    fun getAllTags(): Flow<List<String>>
}