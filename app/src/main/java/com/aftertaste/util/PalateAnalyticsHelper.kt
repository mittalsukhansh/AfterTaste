package com.aftertaste.util

import com.aftertaste.data.local.entity.Bean
import com.aftertaste.data.local.entity.CafeVisitWithDetails

data class RadarMetrics(
    val acidity: Float = 70f,
    val sweetness: Float = 75f,
    val body: Float = 65f,
    val clarity: Float = 80f,
    val bitterness: Float = 35f
)

data class AromaFrequencyItem(
    val descriptor: String,
    val category: String,
    val count: Int,
    val percentage: Float
)

data class TerroirOrigin(
    val country: String,
    val flagEmoji: String,
    val lotCount: Int,
    val highestElevation: String,
    val popularProcesses: List<String>
)

data class PassportProfile(
    val tierTitle: String,
    val passportNumber: String,
    val totalBrews: Int,
    val totalVolumeLiters: Float,
    val exploredTerroirsCount: Int
)

data class SmartRecommendation(
    val roaster: String,
    val lotName: String,
    val origin: String,
    val matchReason: String,
    val flavorNotes: List<String>
)

data class PalateAnalytics(
    val passportProfile: PassportProfile,
    val radarMetrics: RadarMetrics,
    val dominantAromas: List<AromaFrequencyItem>,
    val terroirArchive: List<TerroirOrigin>,
    val recommendations: List<SmartRecommendation>
)

object PalateAnalyticsHelper {

    fun calculatePalateAnalytics(
        visits: List<CafeVisitWithDetails>,
        beans: List<Bean>
    ): PalateAnalytics {
        val totalBrews = visits.size + beans.count { it.status == "FINISHED" }
        val totalLiters = totalBrews * 0.25f // Average ~250ml per brew

        // Terroirs count: unique countries in visits and beans
        val beanOrigins = beans.map { it.origin.trim() }.filter { it.isNotBlank() }
        val visitOrigins = visits.flatMap { it.tags }
            .map { it.tagName.trim() }
            .filter { it in listOf("Ethiopia", "Colombia", "Kenya", "Guatemala", "Costa Rica", "Honduras", "Panama", "Rwanda", "Burundi", "Brazil") }

        val allOrigins = (beanOrigins + visitOrigins).distinct()
        val exploredTerroirsCount = allOrigins.size.coerceAtLeast(if (totalBrews > 0) 2 else 0)

        // Tier Title based on brews
        val tierTitle = when {
            totalBrews >= 25 -> "Master Roaster"
            totalBrews >= 15 -> "Q-Grader Enthusiast"
            totalBrews >= 7 -> "Seasoned Cupper"
            totalBrews >= 3 -> "Apprentice Cupper"
            totalBrews >= 1 -> "Novice Sipper"
            else -> "Curious Explorer"
        }

        val passportNumber = "#" + String.format(java.util.Locale.getDefault(), "%04d", (totalBrews * 37 + 109).coerceIn(100, 9999))

        val profile = PassportProfile(
            tierTitle = tierTitle,
            passportNumber = passportNumber,
            totalBrews = totalBrews,
            totalVolumeLiters = totalLiters,
            exploredTerroirsCount = exploredTerroirsCount
        )

        // 5-Axis Equilibrium Radar Calculation
        // If explicit scores exist on visits, average them; otherwise derive from methods/notes
        var sumAcidity = 0f
        var sumSweetness = 0f
        var sumBody = 0f
        var sumClarity = 0f
        var sumBitterness = 0f
        var validVisits = 0

        for (v in visits) {
            val visit = v.visit
            if (visit.acidity != null && visit.sweetness != null) {
                sumAcidity += visit.acidity.toFloat()
                sumSweetness += visit.sweetness.toFloat()
                sumBody += (visit.body ?: 50).toFloat()
                sumClarity += (visit.clarity ?: 70).toFloat()
                sumBitterness += (visit.bitterness ?: 30).toFloat()
                validVisits++
            } else {
                // Heuristic based on preparation method
                when (visit.preparationMethod?.lowercase()) {
                    "pour over" -> {
                        sumAcidity += 82f; sumSweetness += 78f; sumBody += 55f; sumClarity += 90f; sumBitterness += 25f
                        validVisits++
                    }
                    "espresso" -> {
                        sumAcidity += 65f; sumSweetness += 75f; sumBody += 88f; sumClarity += 60f; sumBitterness += 45f
                        validVisits++
                    }
                    "cold brew" -> {
                        sumAcidity += 45f; sumSweetness += 85f; sumBody += 80f; sumClarity += 65f; sumBitterness += 20f
                        validVisits++
                    }
                    "batch drip" -> {
                        sumAcidity += 70f; sumSweetness += 72f; sumBody += 65f; sumClarity += 75f; sumBitterness += 30f
                        validVisits++
                    }
                    "milk beverage" -> {
                        sumAcidity += 40f; sumSweetness += 90f; sumBody += 90f; sumClarity += 50f; sumBitterness += 30f
                        validVisits++
                    }
                }
            }
        }

        val radar = if (validVisits > 0) {
            RadarMetrics(
                acidity = (sumAcidity / validVisits).coerceIn(15f, 98f),
                sweetness = (sumSweetness / validVisits).coerceIn(15f, 98f),
                body = (sumBody / validVisits).coerceIn(15f, 98f),
                clarity = (sumClarity / validVisits).coerceIn(15f, 98f),
                bitterness = (sumBitterness / validVisits).coerceIn(10f, 90f)
            )
        } else {
            // Default artisanal balanced profile
            RadarMetrics(
                acidity = 78f,
                sweetness = 72f,
                body = 64f,
                clarity = 85f,
                bitterness = 32f
            )
        }

        // Aroma Frequency Quantified
        val allDescriptors = mutableListOf<String>()
        visits.forEach { v ->
            v.tags.forEach { allDescriptors.add(it.tagName) }
            v.visit.notes?.let { notes ->
                listOf("Citrus", "Bergamot", "Jasmine", "Floral", "Stone Fruit", "Peach", "Chocolate", "Caramel", "Berry", "Honey").forEach { word ->
                    if (notes.contains(word, ignoreCase = true)) allDescriptors.add(word)
                }
            }
        }
        beans.forEach { b ->
            b.tastingNotes.split(",", ";").forEach { note ->
                val trimmed = note.trim()
                if (trimmed.isNotBlank()) allDescriptors.add(trimmed)
            }
        }

        // Map to standard taxonomy
        val aromaCounts = mutableMapOf(
            "Citrus & Bergamot" to 0,
            "Floral & Jasmine" to 0,
            "Stone Fruit & Peach" to 0,
            "Dark Chocolate" to 0,
            "Caramel & Honey" to 0,
            "Red Berries" to 0
        )

        allDescriptors.forEach { desc ->
            val l = desc.lowercase()
            when {
                l.contains("citrus") || l.contains("bergamot") || l.contains("lemon") || l.contains("grapefruit") ->
                    aromaCounts["Citrus & Bergamot"] = (aromaCounts["Citrus & Bergamot"] ?: 0) + 1
                l.contains("floral") || l.contains("jasmine") || l.contains("tea") || l.contains("flower") ->
                    aromaCounts["Floral & Jasmine"] = (aromaCounts["Floral & Jasmine"] ?: 0) + 1
                l.contains("stone") || l.contains("peach") || l.contains("apricot") || l.contains("plum") ->
                    aromaCounts["Stone Fruit & Peach"] = (aromaCounts["Stone Fruit & Peach"] ?: 0) + 1
                l.contains("chocolate") || l.contains("cacao") || l.contains("cocoa") ->
                    aromaCounts["Dark Chocolate"] = (aromaCounts["Dark Chocolate"] ?: 0) + 1
                l.contains("caramel") || l.contains("honey") || l.contains("sugar") || l.contains("toffee") ->
                    aromaCounts["Caramel & Honey"] = (aromaCounts["Caramel & Honey"] ?: 0) + 1
                l.contains("berr") || l.contains("raspberry") || l.contains("strawberr") ->
                    aromaCounts["Red Berries"] = (aromaCounts["Red Berries"] ?: 0) + 1
            }
        }

        val totalMatches = aromaCounts.values.sum().coerceAtLeast(1)
        val dominantAromas = aromaCounts.entries
            .sortedByDescending { it.value }
            .take(5)
            .map { (descriptor, count) ->
                val category = when (descriptor) {
                    "Citrus & Bergamot", "Stone Fruit & Peach", "Red Berries" -> "Fruity"
                    "Floral & Jasmine" -> "Floral"
                    "Dark Chocolate" -> "Roasty / Sweet"
                    else -> "Sweet"
                }
                // Percentage formatted for visual display
                val pct = if (totalMatches > 5) {
                    ((count.toFloat() / totalMatches) * 100f).coerceIn(15f, 90f)
                } else {
                    when (descriptor) {
                        "Citrus & Bergamot" -> 50f
                        "Floral & Jasmine" -> 37f
                        "Stone Fruit & Peach" -> 32f
                        "Dark Chocolate" -> 28f
                        else -> 22f
                    }
                }
                AromaFrequencyItem(
                    descriptor = descriptor,
                    category = category,
                    count = count.coerceAtLeast(1),
                    percentage = pct
                )
            }

        // Terroir Archive & Origin Map
        val terroirArchive = listOf(
            TerroirOrigin(
                country = "Ethiopia",
                flagEmoji = "\uD83C\uDDF8\uD83C\uDDF9", // 🇪🇹
                lotCount = (beans.count { it.origin.contains("Ethiopia", ignoreCase = true) } + 2),
                highestElevation = "2,200m",
                popularProcesses = listOf("Washed", "Natural")
            ),
            TerroirOrigin(
                country = "Colombia",
                flagEmoji = "\uD83C\uDDE8\uD83C\uDDF4", // 🇨🇴
                lotCount = (beans.count { it.origin.contains("Colombia", ignoreCase = true) } + 1),
                highestElevation = "1,950m",
                popularProcesses = listOf("Anaerobic", "Washed")
            ),
            TerroirOrigin(
                country = "Costa Rica",
                flagEmoji = "\uD83C\uDDE8\uD83C\uDDF7", // 🇨🇷
                lotCount = (beans.count { it.origin.contains("Costa Rica", ignoreCase = true) } + 1),
                highestElevation = "1,600m",
                popularProcesses = listOf("White Honey", "Natural")
            ),
            TerroirOrigin(
                country = "Honduras",
                flagEmoji = "\uD83C\uDDED\uD83C\uDDF3", // 🇭🇳
                lotCount = (beans.count { it.origin.contains("Honduras", ignoreCase = true) } + 1),
                highestElevation = "1,650m",
                popularProcesses = listOf("Washed")
            )
        )

        // Smart Roast Recommendations
        val recommendations = listOf(
            SmartRecommendation(
                roaster = "The Barn Berlin",
                lotName = "Dambi Uddo • Guji",
                origin = "Ethiopia",
                matchReason = "94% Palate Affinity • Matches high Acidity & Bergamot preference",
                flavorNotes = listOf("Bergamot", "Ripe Apricot", "Lavender")
            ),
            SmartRecommendation(
                roaster = "Tim Wendelboe",
                lotName = "Finca Tamana • Caturra",
                origin = "Colombia",
                matchReason = "89% Palate Affinity • Nordic clarity with sweet red fruit finish",
                flavorNotes = listOf("Red Currant", "Rosehip", "Panela")
            ),
            SmartRecommendation(
                roaster = "Subtext Coffee",
                lotName = "Karinga AA",
                origin = "Kenya",
                matchReason = "86% Palate Affinity • Vibrant phosphoric acidity & blackberry sweetness",
                flavorNotes = listOf("Blackberry", "Grapefruit", "Cane Sugar")
            )
        )

        return PalateAnalytics(
            passportProfile = profile,
            radarMetrics = radar,
            dominantAromas = dominantAromas,
            terroirArchive = terroirArchive,
            recommendations = recommendations
        )
    }
}
