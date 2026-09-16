package com.aftertaste

import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.OrderedItem
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.roundToInt

class VisitLoggingAndAggregationTest {

    @Test
    fun testPriceToQualityRatioCalculation() {
        val overallRating = 4.5f
        val items = listOf(
            OrderedItem(id = 1, visitId = 1, itemName = "Latte", itemPrice = 5.0),
            OrderedItem(id = 2, visitId = 1, itemName = "Crossant", itemPrice = 4.0)
        )

        val totalSpend = items.sumOf { it.itemPrice ?: 0.0 }
        val priceToQualityRatio = if (totalSpend > 0) overallRating / totalSpend else 0.0

        assertEquals(9.0, totalSpend, 0.001)
        assertEquals(0.5, priceToQualityRatio, 0.001)
    }

    @Test
    fun testCoffeeBeanRatingRoundingToHalfSteps() {
        fun calculateRatingFromX(x: Float, totalWidth: Float, maxBeans: Int = 5): Float {
            if (totalWidth <= 0f) return 1.0f
            val ratio = (x / totalWidth).coerceIn(0f, 1f)
            val rawRating = ratio * maxBeans
            val stepRating = (rawRating * 2).roundToInt() / 2f
            return stepRating.coerceIn(0.5f, maxBeans.toFloat())
        }

        // 100px total width
        assertEquals(0.5f, calculateRatingFromX(5f, 100f), 0.01f)   // raw 0.25 -> 0.5
        assertEquals(2.5f, calculateRatingFromX(50f, 100f), 0.01f)  // raw 2.5 -> 2.5
        assertEquals(4.5f, calculateRatingFromX(90f, 100f), 0.01f)  // raw 4.5 -> 4.5
        assertEquals(5.0f, calculateRatingFromX(100f, 100f), 0.01f) // raw 5.0 -> 5.0
    }

    @Test
    fun testCafeAverageRatingCalculation() {
        val visits = listOf(
            CafeVisit(id = 1, cafeName = "Blue Bottle", overallRating = 4.0f),
            CafeVisit(id = 2, cafeName = "Blue Bottle", overallRating = 5.0f),
            CafeVisit(id = 3, cafeName = "Blue Bottle", overallRating = 4.5f)
        )

        val avgRating = visits.map { it.overallRating }.average().toFloat()
        assertEquals(4.5f, avgRating, 0.01f)
    }

    @Test
    fun testSubScoreAveragesCalculation() {
        val tasteRatings = listOf(4.0f, 5.0f, 3.0f)
        val avgTaste = tasteRatings.average().toFloat()
        assertEquals(4.0f, avgTaste, 0.01f)
    }
}
