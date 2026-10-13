package com.aftertaste.data.local.entity

/**
 * Gamification & Tasting Passport Milestone Badges.
 */
data class PassportBadge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val currentProgress: Int,
    val targetGoal: Int,
    val isUnlocked: Boolean
)

object PassportBadgeCatalog {
    val defaultBadges = listOf(
        PassportBadge(
            id = "badge_origin_explorer",
            title = "Origin Explorer",
            description = "Taste specialty coffees from 5 distinct coffee-growing countries",
            iconEmoji = "🌍",
            currentProgress = 4,
            targetGoal = 5,
            isUnlocked = false
        ),
        PassportBadge(
            id = "badge_anaerobic_master",
            title = "Anaerobic Master",
            description = "Log 3 high-sweetness anaerobic thermal shock or carbonic maceration lots",
            iconEmoji = "🧪",
            currentProgress = 3,
            targetGoal = 3,
            isUnlocked = true
        ),
        PassportBadge(
            id = "badge_roastery_nomad",
            title = "Roastery Nomad",
            description = "Explore 10 unique independent specialty roasteries",
            iconEmoji = "☕",
            currentProgress = 7,
            targetGoal = 10,
            isUnlocked = false
        ),
        PassportBadge(
            id = "badge_hopping_streak",
            title = "Hopping Streak",
            description = "Visit and log at least 1 specialty cafe 3 consecutive weekends",
            iconEmoji = "🔥",
            currentProgress = 3,
            targetGoal = 3,
            isUnlocked = true
        )
    )
}
