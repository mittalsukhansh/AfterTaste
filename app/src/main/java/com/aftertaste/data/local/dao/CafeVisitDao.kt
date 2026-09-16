package com.aftertaste.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface CafeVisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: CafeVisit): Long

    @Update
    suspend fun updateVisit(visit: CafeVisit)

    @Delete
    suspend fun deleteVisit(visit: CafeVisit)

    @Query("DELETE FROM cafe_visits WHERE id = :visitId")
    suspend fun deleteVisitById(visitId: Long)

    @Query("SELECT * FROM cafe_visits WHERE id = :visitId")
    fun getVisitById(visitId: Long): Flow<CafeVisit?>

    @Transaction
    @Query("SELECT * FROM cafe_visits WHERE id = :visitId")
    fun getVisitWithDetailsById(visitId: Long): Flow<CafeVisitWithDetails?>

    @Transaction
    @Query("SELECT * FROM cafe_visits WHERE id = :visitId")
    suspend fun getVisitWithDetailsByIdSync(visitId: Long): CafeVisitWithDetails?

    @Query("SELECT * FROM cafe_visits ORDER BY visitDate DESC")
    fun getAllVisits(): Flow<List<CafeVisit>>

    @Transaction
    @Query("SELECT * FROM cafe_visits ORDER BY visitDate DESC")
    fun getAllVisitsWithDetails(): Flow<List<CafeVisitWithDetails>>

    @Transaction
    @Query("SELECT * FROM cafe_visits WHERE LOWER(cafeName) = LOWER(:cafeName) ORDER BY visitDate DESC")
    fun getVisitsForCafe(cafeName: String): Flow<List<CafeVisitWithDetails>>

    @Transaction
    @Query("""
        SELECT DISTINCT cv.* FROM cafe_visits cv
        LEFT JOIN ordered_items oi ON cv.id = oi.visitId
        LEFT JOIN tags t ON cv.id = t.visitId
        WHERE LOWER(cv.cafeName) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(cv.location) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(cv.notes) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(oi.itemName) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(t.tagName) LIKE '%' || LOWER(:query) || '%'
        ORDER BY cv.visitDate DESC
    """)
    fun searchVisits(query: String): Flow<List<CafeVisitWithDetails>>

    @Query("SELECT DISTINCT cafeName FROM cafe_visits ORDER BY cafeName ASC")
    fun getDistinctCafeNames(): Flow<List<String>>
}