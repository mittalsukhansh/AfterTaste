package com.aftertaste

import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.data.local.entity.WishlistCafe
import com.aftertaste.util.BackupManager
import com.aftertaste.util.DataExporter
import com.aftertaste.util.StatsHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StatsAndExportTest {

    @Test
    fun testStatsCalculationWithSampleVisits() {
        val now = System.currentTimeMillis()
        val visit1 = CafeVisitWithDetails(
            visit = CafeVisit(id = 1, cafeName = "Blue Bottle", location = "SOMA", pricePaid = 12.50, overallRating = 4.8f, wifiRating = 4.5f, visitDate = now),
            items = listOf(OrderedItem(id = 1, visitId = 1, itemName = "Latte", itemPrice = 5.50)),
            tags = listOf(Tag(id = 1, visitId = 1, tagName = "Cozy"))
        )
        val visit2 = CafeVisitWithDetails(
            visit = CafeVisit(id = 2, cafeName = "Blue Bottle", location = "SOMA", pricePaid = 8.00, overallRating = 4.2f, wifiRating = 4.0f, visitDate = now),
            items = listOf(OrderedItem(id = 2, visitId = 2, itemName = "Espresso", itemPrice = 4.00)),
            tags = emptyList()
        )
        val visit3 = CafeVisitWithDetails(
            visit = CafeVisit(id = 3, cafeName = "Sightglass", location = "Mission", pricePaid = 15.00, overallRating = 5.0f, powerOutlets = true, visitDate = now),
            items = listOf(OrderedItem(id = 3, visitId = 3, itemName = "Pour Over", itemPrice = 6.00)),
            tags = listOf(Tag(id = 2, visitId = 3, tagName = "Artisanal"))
        )

        val visits = listOf(visit1, visit2, visit3)
        val stats = StatsHelper.calculateStats(visits)

        assertEquals(3, stats.totalVisits)
        assertEquals(2, stats.totalCafes)
        assertEquals(35.50, stats.totalSpend, 0.01)
        assertEquals("Sightglass", stats.favoriteCafe?.first)
        assertEquals(5.0f, stats.favoriteCafe?.second ?: 0f, 0.01f)

        val firstSipBadge = stats.badges.find { it.id == "first_sip" }
        val explorerBadge = stats.badges.find { it.id == "explorer" }
        val roastMasterBadge = stats.badges.find { it.id == "roast_master" }
        val wifiHunterBadge = stats.badges.find { it.id == "wifi_hunter" }

        assertTrue(firstSipBadge?.isUnlocked == true)
        assertTrue(explorerBadge?.isUnlocked == false) // 2 cafes < 3
        assertTrue(roastMasterBadge?.isUnlocked == true) // 5.0 rating >= 4.5
        assertTrue(wifiHunterBadge?.isUnlocked == true) // wifi rating / power outlets true
    }

    @Test
    fun testCsvExportFormatting() {
        val visit = CafeVisitWithDetails(
            visit = CafeVisit(id = 10, cafeName = "Ritual Coffee", location = "Valencia St", overallRating = 4.5f, notes = "Great cold brew"),
            items = listOf(OrderedItem(id = 1, visitId = 10, itemName = "Cold Brew", itemPrice = 5.0)),
            tags = listOf(Tag(id = 1, visitId = 10, tagName = "WorkFriendly"))
        )
        val wishlist = WishlistCafe(id = 1, cafeName = "Verve Coffee", location = "Market St", notes = "Try espresso")

        val visitsCsv = DataExporter.exportVisitsToCsv(listOf(visit))
        val wishlistCsv = DataExporter.exportWishlistToCsv(listOf(wishlist))
        val fullCsv = DataExporter.generateFullCsvExport(listOf(visit), listOf(wishlist))

        assertTrue(visitsCsv.contains("Ritual Coffee"))
        assertTrue(visitsCsv.contains("Cold Brew ($5.0)"))
        assertTrue(visitsCsv.contains("WorkFriendly"))

        assertTrue(wishlistCsv.contains("Verve Coffee"))
        assertTrue(wishlistCsv.contains("Try espresso"))

        assertTrue(fullCsv.contains("=== AFTERTASTE CAFE VISITS ==="))
        assertTrue(fullCsv.contains("=== AFTERTASTE WISHLIST CAFES ==="))
    }

    @Test
    fun testBackupManagerJsonSerializationAndParsing() {
        val visit = CafeVisitWithDetails(
            visit = CafeVisit(id = 1, cafeName = "Stumptown", location = "Downtown", overallRating = 4.6f, notes = "Smooth roast"),
            items = listOf(OrderedItem(id = 1, visitId = 1, itemName = "Latte", itemPrice = 4.75)),
            tags = listOf(Tag(id = 1, visitId = 1, tagName = "OatMilk"))
        )
        val wishlist = WishlistCafe(id = 2, cafeName = "Four Barrel", location = "Valencia", notes = "Must try pour over")

        val jsonString = BackupManager.createBackupJson(listOf(visit), listOf(wishlist))
        assertNotNull(jsonString)

        val parsedBackup = BackupManager.parseBackupJson(jsonString)
        assertNotNull(parsedBackup)
        assertEquals(1, parsedBackup?.visits?.size)
        assertEquals(1, parsedBackup?.wishlist?.size)

        val parsedVisit = parsedBackup?.visits?.first()
        assertEquals("Stumptown", parsedVisit?.visit?.cafeName)
        assertEquals(4.6f, parsedVisit?.visit?.overallRating ?: 0f, 0.01f)
        assertEquals("Latte", parsedVisit?.items?.first()?.itemName)
        assertEquals("OatMilk", parsedVisit?.tags?.first()?.tagName)

        val parsedWishlist = parsedBackup?.wishlist?.first()
        assertEquals("Four Barrel", parsedWishlist?.cafeName)
        assertEquals("Valencia", parsedWishlist?.location)
    }
}
