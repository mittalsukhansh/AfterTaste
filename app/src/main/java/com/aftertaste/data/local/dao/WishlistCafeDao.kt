package com.aftertaste.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aftertaste.data.local.entity.WishlistCafe
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistCafeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistCafe(cafe: WishlistCafe): Long

    @Update
    suspend fun updateWishlistCafe(cafe: WishlistCafe)

    @Delete
    suspend fun deleteWishlistCafe(cafe: WishlistCafe)

    @Query("DELETE FROM wishlist_cafes WHERE id = :id")
    suspend fun deleteWishlistCafeById(id: Long)

    @Query("DELETE FROM wishlist_cafes WHERE LOWER(cafeName) = LOWER(:cafeName)")
    suspend fun deleteWishlistCafeByName(cafeName: String)

    @Query("SELECT * FROM wishlist_cafes WHERE id = :id")
    fun getWishlistCafeById(id: Long): Flow<WishlistCafe?>

    @Query("SELECT * FROM wishlist_cafes ORDER BY addedDate DESC")
    fun getAllWishlistCafes(): Flow<List<WishlistCafe>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_cafes WHERE LOWER(cafeName) = LOWER(:cafeName))")
    fun isWishlistCafe(cafeName: String): Flow<Boolean>
}