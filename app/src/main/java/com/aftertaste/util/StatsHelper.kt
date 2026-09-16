package com.aftertaste.util

import com.aftertaste.data.local.entity.CafeVisitWithDetails
import java.util.Calendar

data class CoffeeBadge(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val iconName: String
)

data class DashboardStats(
    val totalVisits: Int,
    val totalCafes: Int,
    val totalSpend: Double,
    val visitsThisMonth: Int,
    val favoriteCafe: Pair<String, Float>?,
    val streakWeeks: Int,
    val badges: List<CoffeeBadge>
)

object StatsHelper {
    fun calculateStats(visits: List<CafeVisitWithDetails>): DashboardStats {
        val totalVisits = visits.size

        val cafesGrouped = visits.groupBy { it.visit.cafeName.trim().lowercase() }
        val totalCafes = cafesGrouped.size

        val totalSpend = visits.sumOf { details ->
            val visitPrice = details.visit.pricePaid
            if (visitPrice != null && visitPrice > 0) {
                visitPrice
            } else {
                details.items.sumOf { it.itemPrice ?: 0.0 }
            }
        }

        val currentCal = Calendar.getInstance()
        val currentYear = currentCal.get(Calendar.YEAR)
        val currentMonth = currentCal.get(Calendar.MONTH)

        val visitsThisMonth = visits.count { details ->
            val cal = Calendar.getInstance().apply { timeInMillis = details.visit.visitDate }
            cal.get(Calendar.YEAR) == currentYear && cal.get(Calendar.MONTH) == currentMonth
        }

        val favoriteCafe: Pair<String, Float>? = if (cafesGrouped.isEmpty()) {
            null
        } else {
            cafesGrouped.map { (_, visitList) ->
                val cafeName = visitList.first().visit.cafeName
                val avgRating = visitList.map { it.visit.overallRating }.average().toFloat()
                val count = visitList.size
                Triple(cafeName, avgRating, count)
            }.sortedWith(
                compareByDescending<Triple<String, Float, Int>> { it.second }
                    .thenByDescending { it.third }
            ).firstOrNull()?.let { Pair(it.first, it.second) }
        }

        val streakWeeks = calculateStreakWeeks(visits)
        val badges = calculateBadges(visits, totalCafes)

        return DashboardStats(
            totalVisits = totalVisits,
            totalCafes = totalCafes,
            totalSpend = totalSpend,
            visitsThisMonth = visitsThisMonth,
            favoriteCafe = favoriteCafe,
            streakWeeks = streakWeeks,
            badges = badges
        )
    }

    private fun calculateStreakWeeks(visits: List<CafeVisitWithDetails>): Int {
        if (visits.isEmpty()) return 0

        val weekIds = visits.map { details ->
            val cal = Calendar.getInstance().apply { timeInMillis = details.visit.visitDate }
            cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.WEEK_OF_YEAR)
        }.distinct().sortedDescending()

        if (weekIds.isEmpty()) return 0

        val currentCal = Calendar.getInstance()
        var currentWeekId = currentCal.get(Calendar.YEAR) * 100 + currentCal.get(Calendar.WEEK_OF_YEAR)

        if (!weekIds.contains(currentWeekId)) {
            currentCal.add(Calendar.WEEK_OF_YEAR, -1)
            currentWeekId = currentCal.get(Calendar.YEAR) * 100 + currentCal.get(Calendar.WEEK_OF_YEAR)
            if (!weekIds.contains(currentWeekId)) {
                return 0
            }
        }

        var streak = 0
        val checkCal = currentCal.clone() as Calendar

        while (true) {
            val weekId = checkCal.get(Calendar.YEAR) * 100 + checkCal.get(Calendar.WEEK_OF_YEAR)
            if (weekIds.contains(weekId)) {
                streak++
                checkCal.add(Calendar.WEEK_OF_YEAR, -1)
            } else {
                break
            }
        }

        return streak
    }

    private fun calculateBadges(visits: List<CafeVisitWithDetails>, distinctCafes: Int): List<CoffeeBadge> {
        val totalVisits = visits.size

        val hasFirstSip = totalVisits >= 1
        val hasExplorer = distinctCafes >= 3
        val hasRoastMaster = visits.any { it.visit.overallRating >= 4.5f } || totalVisits >= 5
        val hasWifiHunter = visits.any {
            (it.visit.wifiRating ?: 0f) >= 4.0f || (it.visit.powerOutlets == true)
        }

        return listOf(
            CoffeeBadge(
                id = "first_sip",
                title = "First Sip",
                description = "Logged your very first cafe visit.",
                isUnlocked = hasFirstSip,
                iconName = "LocalCoffee"
            ),
            CoffeeBadge(
                id = "explorer",
                title = "Neighbourhood Explorer",
                description = "Explored 3 or more distinct coffee spots.",
                isUnlocked = hasExplorer,
                iconName = "Explore"
            ),
            CoffeeBadge(
                id = "roast_master",
                title = "Roast Master",
                description = "Found top-tier coffee (4.5+ rating) or logged 5+ visits.",
                isUnlocked = hasRoastMaster,
                iconName = "WorkspacePremium"
            ),
            CoffeeBadge(
                id = "wifi_hunter",
                title = "Wifi Hunter",
                description = "Discovered a high-speed wifi & power outlet study cafe.",
                isUnlocked = hasWifiHunter,
                iconName = "Wifi"
            )
        )
    }
}
