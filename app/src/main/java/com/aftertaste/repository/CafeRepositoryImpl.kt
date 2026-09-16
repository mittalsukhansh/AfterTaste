package com.aftertaste.repository

import com.aftertaste.data.local.dao.CafeVisitDao
import com.aftertaste.data.local.dao.OrderedItemDao
import com.aftertaste.data.local.dao.PhotoDao
import com.aftertaste.data.local.dao.TagDao
import com.aftertaste.data.local.dao.WishlistCafeDao
import com.aftertaste.data.local.entity.Cafe
import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Photo
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CafeRepositoryImpl(
    private val cafeVisitDao: CafeVisitDao,
    private val orderedItemDao: OrderedItemDao,
    private val tagDao: TagDao,
    private val photoDao: PhotoDao,
    private val wishlistCafeDao: WishlistCafeDao
) : CafeRepository {

    override fun getAllVisits(): Flow<List<CafeVisitWithDetails>> {
        return cafeVisitDao.getAllVisitsWithDetails()
    }

    override fun getVisitById(visitId: Long): Flow<CafeVisitWithDetails?> {
        return cafeVisitDao.getVisitWithDetailsById(visitId)
    }

    override fun getVisitsForCafe(cafeName: String): Flow<List<CafeVisitWithDetails>> {
        return cafeVisitDao.getVisitsForCafe(cafeName)
    }

    override fun searchVisits(query: String): Flow<List<CafeVisitWithDetails>> {
        return cafeVisitDao.searchVisits(query)
    }

    override suspend fun saveVisit(
        visit: CafeVisit,
        items: List<OrderedItem>,
        tags: List<Tag>,
        photos: List<Photo>
    ): Long {
        val visitId = cafeVisitDao.insertVisit(visit)

        if (visit.id != 0L) {
            orderedItemDao.deleteItemsForVisit(visit.id)
            tagDao.deleteTagsForVisit(visit.id)
            photoDao.deletePhotosForVisit(visit.id)
        }

        val itemsToSave = items.map { it.copy(id = 0, visitId = visitId) }
        val tagsToSave = tags.map { it.copy(id = 0, visitId = visitId) }
        val photosToSave = photos.map { it.copy(id = 0, visitId = visitId) }

        if (itemsToSave.isNotEmpty()) orderedItemDao.insertItems(itemsToSave)
        if (tagsToSave.isNotEmpty()) tagDao.insertTags(tagsToSave)
        if (photosToSave.isNotEmpty()) photoDao.insertPhotos(photosToSave)

        // Remove from wishlist when visit is saved
        wishlistCafeDao.deleteWishlistCafeByName(visit.cafeName)

        return visitId
    }

    override suspend fun deleteVisit(visitId: Long) {
        cafeVisitDao.deleteVisitById(visitId)
    }

    override fun getAllCafes(): Flow<List<Cafe>> {
        return combine(
            cafeVisitDao.getAllVisitsWithDetails(),
            wishlistCafeDao.getAllWishlistCafes()
        ) { visits, wishlist ->
            val cafeMap = mutableMapOf<String, MutableList<CafeVisitWithDetails>>()
            for (v in visits) {
                val key = v.visit.cafeName.trim().lowercase()
                cafeMap.getOrPut(key) { mutableListOf() }.add(v)
            }

            val wishlistNames = wishlist.map { it.cafeName.trim().lowercase() }.toSet()
            val cafeList = mutableListOf<Cafe>()

            for ((_, vList) in cafeMap) {
                val firstVisit = vList.first()
                val cafeName = firstVisit.visit.cafeName
                val location = firstVisit.visit.location
                val visitCount = vList.size
                val avgRating = vList.map { it.visit.overallRating }.average().toFloat()
                val lastVisited = vList.maxOf { it.visit.visitDate }
                val isWishlist = wishlistNames.contains(cafeName.trim().lowercase())
                val coverPhoto = vList.flatMap { it.photos }.firstOrNull()?.uri

                cafeList.add(
                    Cafe(
                        cafeName = cafeName,
                        location = location,
                        visitCount = visitCount,
                        avgOverallRating = avgRating,
                        lastVisited = lastVisited,
                        isWishlist = isWishlist,
                        coverPhotoUri = coverPhoto
                    )
                )
            }

            for (w in wishlist) {
                val key = w.cafeName.trim().lowercase()
                if (!cafeMap.containsKey(key)) {
                    cafeList.add(
                        Cafe(
                            cafeName = w.cafeName,
                            location = w.location,
                            visitCount = 0,
                            avgOverallRating = 0f,
                            lastVisited = 0L,
                            isWishlist = true,
                            coverPhotoUri = null
                        )
                    )
                }
            }

            cafeList.sortedByDescending { it.lastVisited }
        }
    }

    override fun getAllWishlistCafes(): Flow<List<WishlistCafe>> {
        return wishlistCafeDao.getAllWishlistCafes()
    }

    override fun isWishlistCafe(cafeName: String): Flow<Boolean> {
        return wishlistCafeDao.isWishlistCafe(cafeName)
    }

    override suspend fun addToWishlist(wishlistCafe: WishlistCafe): Long {
        return wishlistCafeDao.insertWishlistCafe(wishlistCafe)
    }

    override suspend fun removeFromWishlist(id: Long) {
        wishlistCafeDao.deleteWishlistCafeById(id)
    }

    override suspend fun removeFromWishlistByName(cafeName: String) {
        wishlistCafeDao.deleteWishlistCafeByName(cafeName)
    }

    override fun getAllTags(): Flow<List<String>> {
        return tagDao.getAllDistinctTags()
    }
}