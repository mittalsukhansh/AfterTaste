package com.aftertaste.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aftertaste.data.local.entity.OrderedItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: OrderedItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<OrderedItem>)

    @Update
    suspend fun updateItem(item: OrderedItem)

    @Delete
    suspend fun deleteItem(item: OrderedItem)

    @Query("DELETE FROM ordered_items WHERE visitId = :visitId")
    suspend fun deleteItemsForVisit(visitId: Long)

    @Query("SELECT * FROM ordered_items WHERE visitId = :visitId")
    fun getItemsForVisit(visitId: Long): Flow<List<OrderedItem>>
}