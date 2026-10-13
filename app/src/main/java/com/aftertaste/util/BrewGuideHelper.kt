package com.aftertaste.util

/**
 * Interactive Barista Brew Guide & Ratio Engine.
 */
enum class BrewMethod(val displayName: String, val defaultDoseGrams: Float, val waterRatio: Float, val idealTempCelsius: Int) {
    V60_POUR_OVER("V60 Pour Over", 15.0f, 16.66f, 93),
    AEROPRESS("AeroPress", 18.0f, 11.11f, 88),
    FRENCH_PRESS("French Press", 20.0f, 15.0f, 94),
    MOKA_POT("Moka Pot", 18.0f, 10.0f, 96),
    COLD_BREW("Cold Brew", 60.0f, 8.0f, 20)
}

data class BrewStage(
    val stageName: String,
    val durationSeconds: Int,
    val instruction: String,
    val targetWaterGrams: Int
)

object BrewGuideHelper {
    fun calculateWaterYield(doseGrams: Float, method: BrewMethod): Int {
        return (doseGrams * method.waterRatio).toInt()
    }

    fun getStagesForMethod(method: BrewMethod, doseGrams: Float): List<BrewStage> {
        val totalWater = calculateWaterYield(doseGrams, method)
        return when (method) {
            BrewMethod.V60_POUR_OVER -> listOf(
                BrewStage("Bloom", 45, "Pour ${(doseGrams * 3.3).toInt()}g water in spiral; swirl gently to degas.", (doseGrams * 3.3).toInt()),
                BrewStage("First Pour", 45, "Continuous gentle circular pour up to ${(totalWater * 0.6).toInt()}g.", (totalWater * 0.6).toInt()),
                BrewStage("Second Pour", 45, "Gentle center pour up to total ${totalWater}g.", totalWater),
                BrewStage("Drawdown", 45, "Allow clean flat bed drawdown.", totalWater)
            )
            BrewMethod.AEROPRESS -> listOf(
                BrewStage("Inverted Pour", 30, "Pour ${totalWater}g water rapidly and stir 10s.", totalWater),
                BrewStage("Steep", 60, "Attach rinsed filter cap and steep.", totalWater),
                BrewStage("Plunge", 30, "Slow steady plunge for 30 seconds until hiss.", totalWater)
            )
            BrewMethod.FRENCH_PRESS -> listOf(
                BrewStage("Saturate", 45, "Saturate all grounds with ${totalWater}g water.", totalWater),
                BrewStage("Immersion", 165, "Place lid on top and let extract.", totalWater),
                BrewStage("Plunge", 30, "Break crust, skim foam, plunge gently.", totalWater)
            )
            BrewMethod.MOKA_POT -> listOf(
                BrewStage("Low Flame", 150, "Heat on low flame until first stream emerges.", totalWater),
                BrewStage("Golden Flow", 45, "Collect golden honey crema in chamber.", totalWater),
                BrewStage("Quench", 30, "Cool pot base with cold water to halt extraction.", totalWater)
            )
            BrewMethod.COLD_BREW -> listOf(
                BrewStage("Coarse Grind Steep", 64800, "Slow room-temp steep for 18 hours.", totalWater)
            )
        }
    }
}
