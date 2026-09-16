package com.aftertaste.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aftertaste.data.local.dao.CafeVisitDao
import com.aftertaste.data.local.dao.OrderedItemDao
import com.aftertaste.data.local.dao.PhotoDao
import com.aftertaste.data.local.dao.TagDao
import com.aftertaste.data.local.dao.WishlistCafeDao
import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Photo
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe

@Database(
    entities = [
        CafeVisit::class,
        OrderedItem::class,
        Tag::class,
        Photo::class,
        WishlistCafe::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cafeVisitDao(): CafeVisitDao
    abstract fun orderedItemDao(): OrderedItemDao
    abstract fun tagDao(): TagDao
    abstract fun photoDao(): PhotoDao
    abstract fun wishlistCafeDao(): WishlistCafeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aftertaste_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}