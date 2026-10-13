package com.aftertaste.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aftertaste.data.local.entity.Bean
import kotlinx.coroutines.flow.Flow

@Dao
interface BeanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBean(bean: Bean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeans(beans: List<Bean>)

    @Update
    suspend fun updateBean(bean: Bean)

    @Delete
    suspend fun deleteBean(bean: Bean)

    @Query("DELETE FROM coffee_beans WHERE id = :id")
    suspend fun deleteBeanById(id: Long)

    @Query("SELECT * FROM coffee_beans ORDER BY roastDate DESC")
    fun getAllBeans(): Flow<List<Bean>>

    @Query("SELECT * FROM coffee_beans WHERE status = :status ORDER BY roastDate DESC")
    fun getBeansByStatus(status: String): Flow<List<Bean>>

    @Query("SELECT * FROM coffee_beans WHERE id = :id")
    fun getBeanById(id: Long): Flow<Bean?>

    @Query("SELECT * FROM coffee_beans WHERE id = :id")
    suspend fun getBeanByIdSync(id: Long): Bean?

    @Query("SELECT COUNT(*) FROM coffee_beans")
    suspend fun getBeanCount(): Int

    @Query("UPDATE coffee_beans SET weightRemaining = :weight WHERE id = :id")
    suspend fun updateWeightRemaining(id: Long, weight: Float)
}
