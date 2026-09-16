package com.aftertaste.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aftertaste.data.local.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<Tag>)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("DELETE FROM tags WHERE visitId = :visitId")
    suspend fun deleteTagsForVisit(visitId: Long)

    @Query("SELECT * FROM tags WHERE visitId = :visitId")
    fun getTagsForVisit(visitId: Long): Flow<List<Tag>>

    @Query("SELECT DISTINCT tagName FROM tags ORDER BY tagName ASC")
    fun getAllDistinctTags(): Flow<List<String>>
}