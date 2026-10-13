package com.aftertaste.repository

import com.aftertaste.data.local.dao.BeanDao
import com.aftertaste.data.local.dao.CafeVisitDao
import com.aftertaste.data.local.dao.OrderedItemDao
import com.aftertaste.data.local.dao.PhotoDao
import com.aftertaste.data.local.dao.TagDao
import com.aftertaste.data.local.dao.WishlistCafeDao
import com.aftertaste.data.local.entity.Bean
import com.aftertaste.data.local.entity.Cafe
import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Photo
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow

class CafeRepositoryImpl(
    private val cafeVisitDao: CafeVisitDao,
    private val orderedItemDao: OrderedItemDao,
    private val tagDao: TagDao,
    private val photoDao: PhotoDao,
    private val wishlistCafeDao: WishlistCafeDao,
    private val beanDao: BeanDao? = null
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

    override fun getAllBeans(): Flow<List<Bean>> {
        return beanDao?.getAllBeans() ?: emptyFlow()
    }

    override fun getBeansByStatus(status: String): Flow<List<Bean>> {
        return beanDao?.getBeansByStatus(status) ?: emptyFlow()
    }

    override fun getBeanById(id: Long): Flow<Bean?> {
        return beanDao?.getBeanById(id) ?: emptyFlow()
    }

    override suspend fun saveBean(bean: Bean): Long {
        return beanDao?.insertBean(bean) ?: 0L
    }

    override suspend fun deleteBean(id: Long) {
        beanDao?.deleteBeanById(id)
    }

    override suspend fun deductBeanWeight(id: Long, grams: Float) {
        val bean = beanDao?.getBeanByIdSync(id) ?: return
        val newWeight = (bean.weightRemaining - grams).coerceAtLeast(0f)
        val newStatus = if (newWeight <= 0f) "FINISHED" else bean.status
        beanDao.updateBean(bean.copy(weightRemaining = newWeight, status = newStatus))
    }

    override suspend fun preseedBeansIfEmpty() {
        if (beanDao == null) return
        val count = beanDao.getBeanCount()
        if (count == 0) {
            val now = System.currentTimeMillis()
            val dayMs = 24 * 60 * 60 * 1000L
            val initialBeans = listOf(
                Bean(
                    roaster = "Sey Coffee",
                    name = "Rodrigo Sanchez • Purple Caturra",
                    origin = "Colombia",
                    process = "Cold Aerobic Fermentation",
                    variety = "Purple Caturra",
                    elevation = "1750m",
                    roastDate = now - 9 * dayMs, // 9 days off roast (Peak Window)
                    weightTotal = 250f,
                    weightRemaining = 160f,
                    tastingNotes = "Raspberry Jam, Pink Grapefruit, Floral Honey",
                    status = "ACTIVE",
                    notes = "Exceptional clarity on V60 (1:16.5 ratio). Sweet, sparkling acidity."
                ),
                Bean(
                    roaster = "Tim Wendelboe",
                    name = "Caballero • Geisha Lot 4",
                    origin = "Honduras",
                    process = "Washed",
                    variety = "Geisha",
                    elevation = "1600m",
                    roastDate = now - 14 * dayMs, // 14 days off roast (Peak Window)
                    weightTotal = 250f,
                    weightRemaining = 90f,
                    tastingNotes = "Jasmine, Bergamot, White Peach, Black Tea",
                    status = "ACTIVE",
                    notes = "Classic Nordic light roast profile. Silky body with floral finish."
                ),
                Bean(
                    roaster = "April Coffee Copenhagen",
                    name = "Volcan Azul • San Roque",
                    origin = "Costa Rica",
                    process = "Natural Anaerobic",
                    variety = "San Roque",
                    elevation = "1550m",
                    roastDate = now - 3 * dayMs, // 3 days off roast (Degassing)
                    weightTotal = 250f,
                    weightRemaining = 250f,
                    tastingNotes = "Strawberry Compote, Passion Fruit, Cacao Nibs",
                    status = "ACTIVE",
                    notes = "Resting window recommendation: best from day 10 onward."
                ),
                Bean(
                    roaster = "Onyx Coffee Lab",
                    name = "Tropical Weather",
                    origin = "Ethiopia & Colombia",
                    process = "Washed & Natural Blend",
                    variety = "Heirloom & Caturra",
                    elevation = "1900-2100m",
                    roastDate = now - 28 * dayMs, // 28 days off roast (Past Peak / Finished)
                    weightTotal = 340f,
                    weightRemaining = 0f,
                    tastingNotes = "Candied Plum, Dark Chocolate, Berry Jam",
                    status = "FINISHED",
                    notes = "Finished bag. Worked wonders as both batch brew and espresso."
                ),
                Bean(
                    roaster = "La Cabra",
                    name = "Worka Sakaro",
                    origin = "Ethiopia",
                    process = "Washed",
                    variety = "Kurume & Dega",
                    elevation = "2000-2200m",
                    roastDate = now,
                    weightTotal = 250f,
                    weightRemaining = 250f,
                    tastingNotes = "Meyer Lemon, Earl Grey, Apricot",
                    status = "WISHLIST",
                    notes = "High-altitude lot to order next season."
                )
            )
            beanDao.insertBeans(initialBeans)
        }
    }
}