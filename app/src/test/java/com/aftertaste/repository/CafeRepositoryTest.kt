package com.aftertaste.repository

import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Photo
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CafeRepositoryTest {

    @Test
    fun testCafeVisitEntityCreation() {
        val visit = CafeVisit(
            id = 1,
            cafeName = "Bean Scene",
            location = "Downtown",
            overallRating = 4.5f,
            wouldReturn = true
        )
        assertEquals("Bean Scene", visit.cafeName)
        assertEquals("Downtown", visit.location)
        assertEquals(4.5f, visit.overallRating)
        assertEquals(true, visit.wouldReturn)
    }

    @Test
    fun testOrderedItemEntityCreation() {
        val item = OrderedItem(
            id = 1,
            visitId = 1,
            itemName = "Iced Vanilla Latte",
            itemPrice = 5.50,
            itemRating = 5.0f
        )
        assertEquals("Iced Vanilla Latte", item.itemName)
        assertEquals(5.50, item.itemPrice!!, 0.01)
        assertEquals(5.0f, item.itemRating!!, 0.01f)
    }

    @Test
    fun testTagEntityCreation() {
        val tag = Tag(
            id = 1,
            visitId = 1,
            tagName = "Quiet"
        )
        assertEquals("Quiet", tag.tagName)
    }

    @Test
    fun testPhotoEntityCreation() {
        val photo = Photo(
            id = 1,
            visitId = 1,
            uri = "content://media/external/images/media/1"
        )
        assertEquals("content://media/external/images/media/1", photo.uri)
    }

    @Test
    fun testWishlistCafeEntityCreation() {
        val wishlist = WishlistCafe(
            id = 1,
            cafeName = "Roast & Co",
            location = "Uptown",
            notes = "Must try espresso"
        )
        assertEquals("Roast & Co", wishlist.cafeName)
        assertNotNull(wishlist.addedDate)
    }
}