package com.aftertaste.ui.analytics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.WorkspacePremium
import com.aftertaste.util.CoffeeSpendTracker
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.components.FlavorRadarChart
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.util.AromaFrequencyItem
import com.aftertaste.util.PalateAnalyticsHelper
import com.aftertaste.util.SmartRecommendation
import com.aftertaste.util.TerroirOrigin
import java.util.Locale

@Composable
fun SensoryAnalyticsScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier
) {
    val visits by viewModel.visits.collectAsState()
    val beans by viewModel.beans.collectAsState()

    val analytics = remember(visits, beans) {
        PalateAnalyticsHelper.calculatePalateAnalytics(visits, beans)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CoffeeClay)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Enthusiast Coffee Passport Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = ParchmentCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = TerracottaAccent.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = TerracottaAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "COFFEE PASSPORT",
                                style = MaterialTheme.typography.labelSmall,
                                letterSpacing = 1.2.sp,
                                color = EspressoText.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = analytics.passportProfile.tierTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EspressoText
                    ) {
                        Text(
                            text = analytics.passportProfile.passportNumber,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ParchmentCream,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${analytics.passportProfile.totalBrews}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        Text(
                            text = "Cups Logged",
                            style = MaterialTheme.typography.labelSmall,
                            color = EspressoText.copy(alpha = 0.6f)
                        )
                    }

                    Column {
                        Text(
                            text = String.format(Locale.getDefault(), "%.1fL", analytics.passportProfile.totalVolumeLiters),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        Text(
                            text = "Brewed Volume",
                            style = MaterialTheme.typography.labelSmall,
                            color = EspressoText.copy(alpha = 0.6f)
                        )
                    }

                    Column {
                        Text(
                            text = "${analytics.passportProfile.exploredTerroirsCount}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        Text(
                            text = "Origins Explored",
                            style = MaterialTheme.typography.labelSmall,
                            color = EspressoText.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        // 2. Flavor Equilibrium Radar (5-Axis Sensory Polygon)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Flavor Equilibrium Radar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ParchmentCream
                )
                Text(
                    text = "5-Axis Palate",
                    style = MaterialTheme.typography.labelSmall,
                    color = TerracottaAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }

            FlavorRadarChart(metrics = analytics.radarMetrics)
        }

        // 3. Dominant Aroma Frequency
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = ParchmentCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dominant Aroma Frequency",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )
                    Text(
                        text = "Sensory Descriptors",
                        style = MaterialTheme.typography.labelSmall,
                        color = TerracottaAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                analytics.dominantAromas.forEach { aroma ->
                    AromaFrequencyRow(aroma = aroma)
                }
            }
        }

        // 4. Terroir Archive & Origin Map
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = ParchmentCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = null,
                            tint = TerracottaAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Terroir Archive",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                    }
                    Text(
                        text = "${analytics.terroirArchive.size} Origins",
                        style = MaterialTheme.typography.labelSmall,
                        color = EspressoText.copy(alpha = 0.6f)
                    )
                }

                analytics.terroirArchive.forEach { origin ->
                    TerroirOriginRow(origin = origin)
                }
            }
        }

        // 5. Specialty Coffee Spending & Monthly Budget Tracker (₹ INR)
        val spendSummary = remember(visits) {
            val totalCafe = visits.sumOf { v -> v.items.sumOf { it.itemPrice ?: 0.0 } }.toFloat()
            CoffeeSpendTracker.calculateSummary(
                cafeExpensesInr = if (totalCafe > 0f) totalCafe else 1850f,
                homeBeansInr = 600f,
                budgetInr = 3500f
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = ParchmentCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = TerracottaAccent.copy(alpha = 0.2f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = TerracottaAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "COFFEE SPEND & BUDGET",
                                style = MaterialTheme.typography.labelSmall,
                                letterSpacing = 1.sp,
                                color = EspressoText.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Monthly Budget Pulse",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (spendSummary.percentOfBudgetUsed > 85) Color(0xFFDC2626) else TerracottaAccent
                    ) {
                        Text(
                            text = "${spendSummary.percentOfBudgetUsed}% Used",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ParchmentCream,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = String.format(Locale.getDefault(), "₹%.0f", spendSummary.totalSpentInr),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "of ₹%.0f monthly limit", spendSummary.monthlyBudgetInr),
                        style = MaterialTheme.typography.bodyMedium,
                        color = EspressoText.copy(alpha = 0.6f)
                    )
                }

                LinearProgressIndicator(
                    progress = { (spendSummary.percentOfBudgetUsed / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = TerracottaAccent,
                    trackColor = CoffeeClay.copy(alpha = 0.15f),
                    strokeCap = StrokeCap.Round
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Cafe Hopping", style = MaterialTheme.typography.labelSmall, color = EspressoText.copy(alpha = 0.6f))
                        Text(String.format(Locale.getDefault(), "₹%.0f", spendSummary.cafeHoppingSpentInr), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = EspressoText)
                    }
                    Column {
                        Text("Vault Bags", style = MaterialTheme.typography.labelSmall, color = EspressoText.copy(alpha = 0.6f))
                        Text(String.format(Locale.getDefault(), "₹%.0f", spendSummary.homeBagsSpentInr), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = EspressoText)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Home Savings", style = MaterialTheme.typography.labelSmall, color = Color(0xFF059669))
                        Text(String.format(Locale.getDefault(), "+₹%.0f saved", spendSummary.estimatedHomeSavingsInr), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                    }
                }
            }
        }

        // 6. Smart Roast Recommendations
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TerracottaAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Smart Roast Recommendations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ParchmentCream
                    )
                }
            }

            analytics.recommendations.forEach { rec ->
                RecommendationCard(recommendation = rec)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun AromaFrequencyRow(aroma: AromaFrequencyItem) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = aroma.descriptor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CoffeeClay.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = aroma.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = EspressoText.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "${aroma.percentage.toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TerracottaAccent
            )
        }

        LinearProgressIndicator(
            progress = { (aroma.percentage / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = TerracottaAccent,
            trackColor = CoffeeOutline.copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun TerroirOriginRow(origin: TerroirOrigin) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CoffeeClay.copy(alpha = 0.08f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = origin.flagEmoji, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = origin.country,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )
                    Text(
                        text = "Elevation up to ${origin.highestElevation} • ${origin.popularProcesses.joinToString(", ")}",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = EspressoText.copy(alpha = 0.65f)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = TerracottaAccent
            ) {
                Text(
                    text = "${origin.lotCount} Lots",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText
                )
            }
        }
    }
}

@Composable
fun RecommendationCard(recommendation: SmartRecommendation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recommendation.roaster.uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.labelSmall,
                    color = TerracottaAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = recommendation.origin,
                    style = MaterialTheme.typography.labelSmall,
                    color = EspressoText.copy(alpha = 0.6f)
                )
            }

            Text(
                text = recommendation.lotName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = EspressoText
            )

            Text(
                text = recommendation.matchReason,
                style = MaterialTheme.typography.bodySmall,
                color = EspressoText.copy(alpha = 0.75f)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                recommendation.flavorNotes.forEach { note ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = CoffeeClay.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = note,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = EspressoText
                        )
                    }
                }
            }
        }
    }
}
