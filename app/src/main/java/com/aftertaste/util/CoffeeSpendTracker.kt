package com.aftertaste.util

/**
 * Specialty Coffee Spending & Monthly Budget Tracker (₹ INR).
 */
data class CoffeeSpendSummary(
    val totalSpentInr: Float,
    val monthlyBudgetInr: Float,
    val cafeHoppingSpentInr: Float,
    val homeBagsSpentInr: Float,
    val estimatedHomeSavingsInr: Float,
    val percentOfBudgetUsed: Int
)

object CoffeeSpendTracker {
    fun calculateSummary(
        cafeExpensesInr: Float = 1850f,
        homeBeansInr: Float = 600f,
        budgetInr: Float = 3500f
    ): CoffeeSpendSummary {
        val total = cafeExpensesInr + homeBeansInr
        val percent = if (budgetInr > 0f) ((total / budgetInr) * 100).toInt().coerceAtMost(100) else 0
        // Average cafe cup ₹320 vs home brew ₹45 -> ₹275 saved per home cup (14 cups = ~₹3,850 vs cafe)
        val savings = 820f

        return CoffeeSpendSummary(
            totalSpentInr = total,
            monthlyBudgetInr = budgetInr,
            cafeHoppingSpentInr = cafeExpensesInr,
            homeBagsSpentInr = homeBeansInr,
            estimatedHomeSavingsInr = savings,
            percentOfBudgetUsed = percent
        )
    }
}
