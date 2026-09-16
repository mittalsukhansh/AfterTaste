package com.aftertaste.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.ui.components.CoffeeBeanIcon
import com.aftertaste.ui.components.CoffeeBeanRatingBar
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.util.CoffeeBadge
import com.aftertaste.util.StatsHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onNavigateToAddVisit: () -> Unit = {},
    onNavigateToWishlist: () -> Unit = {},
    onNavigateToMapView: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToVisitDetail: (Long) -> Unit = {}
) {
    val visits by viewModel.visits.collectAsState()
    val stats = remember(visits) { StatsHelper.calculateStats(visits) }

    var selectedBadgeForModal by remember { mutableStateOf<CoffeeBadge?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CoffeeClay
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Quick Action Bar
            QuickActionBar(
                onNavigateToAddVisit = onNavigateToAddVisit,
                onNavigateToWishlist = onNavigateToWishlist,
                onNavigateToMapView = onNavigateToMapView,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToSettings = onNavigateToSettings
            )

            // 2. Key Stats Summary Cards Carousel/Row
            Text(
                text = "Passport Overview",
                style = MaterialTheme.typography.titleLarge,
                color = ParchmentCream,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                item {
                    StatSummaryCard(
                        title = "Visited Cafes",
                        value = "${stats.totalCafes}",
                        subtitle = "${stats.totalVisits} Total Visits",
                        icon = Icons.Default.FreeBreakfast,
                        containerColor = ParchmentCream
                    )
                }

                item {
                    StatSummaryCard(
                        title = "Total Spend",
                        value = String.format(Locale.getDefault(), "$%.2f", stats.totalSpend),
                        subtitle = "Coffee Journal",
                        icon = Icons.Default.Payments,
                        containerColor = ParchmentCream
                    )
                }

                item {
                    StatSummaryCard(
                        title = "Visits This Month",
                        value = "${stats.visitsThisMonth}",
                        subtitle = "Current Month",
                        icon = Icons.Default.DateRange,
                        containerColor = ParchmentCream
                    )
                }

                item {
                    val favName = stats.favoriteCafe?.first ?: "None Yet"
                    val favRating = stats.favoriteCafe?.second?.let { String.format(Locale.getDefault(), "%.1f ⭐", it) } ?: "--"
                    StatSummaryCard(
                        title = "Favorite Cafe",
                        value = favName,
                        subtitle = favRating,
                        icon = Icons.Default.Star,
                        containerColor = ParchmentCream
                    )
                }

                item {
                    StatSummaryCard(
                        title = "Weekly Streak",
                        value = "${stats.streakWeeks} Wks",
                        subtitle = if (stats.streakWeeks > 0) "Active Streak! 🔥" else "Visit a cafe this week",
                        icon = Icons.Default.Whatshot,
                        containerColor = ParchmentCream
                    )
                }
            }

            // 3. Digital Coffee Passport Stamps / Badges Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Passport Stamps & Badges",
                    style = MaterialTheme.typography.titleLarge,
                    color = ParchmentCream,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = CircleShape,
                    color = TerracottaAccent.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "${stats.badges.count { it.isUnlocked }}/${stats.badges.size} Unlocked",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ParchmentCream
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                stats.badges.forEach { badge ->
                    PassportBadgeItem(
                        badge = badge,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedBadgeForModal = badge }
                    )
                }
            }

            // 4. Recent Visit Logs Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Visit Logs",
                    style = MaterialTheme.typography.titleLarge,
                    color = ParchmentCream,
                    fontWeight = FontWeight.Bold
                )

                if (visits.size > 3) {
                    Text(
                        text = "See All (${visits.size})",
                        style = MaterialTheme.typography.labelMedium,
                        color = TerracottaAccent,
                        modifier = Modifier.clickable { onNavigateToSearch() }
                    )
                }
            }

            if (visits.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCream)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CoffeeCupIcon(modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No visits logged yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap '+ Add Visit' to start tracking your coffee experiences!",
                            style = MaterialTheme.typography.bodySmall,
                            color = EspressoText.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                val recentVisits = visits.take(3)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    recentVisits.forEach { visitDetails ->
                        DashboardVisitCard(
                            visitDetails = visitDetails,
                            onClick = { onNavigateToVisitDetail(visitDetails.visit.id) }
                        )
                    }
                }
            }
        }
    }

    // Modal for badge details
    selectedBadgeForModal?.let { badge ->
        BadgeDetailDialog(
            badge = badge,
            onDismiss = { selectedBadgeForModal = null }
        )
    }
}

@Composable
fun QuickActionBar(
    onNavigateToAddVisit: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToMapView: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickActionButton(
                icon = Icons.Default.Add,
                label = "+ Add",
                onClick = onNavigateToAddVisit,
                isPrimary = true
            )

            QuickActionButton(
                icon = Icons.Default.Bookmark,
                label = "Wishlist",
                onClick = onNavigateToWishlist
            )

            QuickActionButton(
                icon = Icons.Default.Map,
                label = "Map",
                onClick = onNavigateToMapView
            )

            QuickActionButton(
                icon = Icons.Default.Search,
                label = "Search",
                onClick = onNavigateToSearch
            )

            QuickActionButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = onNavigateToSettings
            )
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isPrimary: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(if (isPrimary) TerracottaAccent else CoffeeClay.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isPrimary) EspressoText else CoffeeClay,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = EspressoText
        )
    }
}

@Composable
fun StatSummaryCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TerracottaAccent,
                    modifier = Modifier.size(22.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = EspressoText.copy(alpha = 0.6f),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = EspressoText,
                maxLines = 1
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TerracottaAccent,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PassportBadgeItem(
    badge: CoffeeBadge,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (badge.iconName) {
        "LocalCoffee", "FreeBreakfast" -> Icons.Default.FreeBreakfast
        "Explore" -> Icons.Default.Explore
        "WorkspacePremium" -> Icons.Default.WorkspacePremium
        "Wifi" -> Icons.Default.Wifi
        else -> Icons.Default.FreeBreakfast
    }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) ParchmentCream else ParchmentCream.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (badge.isUnlocked) 4.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.isUnlocked) TerracottaAccent else CoffeeClay.copy(alpha = 0.2f)
                    )
                    .border(
                        width = 2.dp,
                        color = if (badge.isUnlocked) TerracottaAccent else CoffeeClay.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = badge.title,
                    tint = if (badge.isUnlocked) EspressoText else EspressoText.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = badge.title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (badge.isUnlocked) EspressoText else EspressoText.copy(alpha = 0.5f),
                maxLines = 1
            )
        }
    }
}

@Composable
fun DashboardVisitCard(
    visitDetails: CafeVisitWithDetails,
    onClick: () -> Unit
) {
    val v = visitDetails.visit
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CoffeeClay.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    CoffeeBeanIcon(modifier = Modifier.size(22.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = v.cafeName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )
                    if (v.location.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = v.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = EspressoText.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Text(
                        text = dateFormatter.format(Date(v.visitDate)),
                        style = MaterialTheme.typography.labelSmall,
                        color = EspressoText.copy(alpha = 0.5f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                CoffeeBeanRatingBar(
                    rating = v.overallRating,
                    isSelectable = false,
                    beanSize = 14.dp,
                    beanPadding = 2.dp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Details",
                    tint = CoffeeClay,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun BadgeDetailDialog(
    badge: CoffeeBadge,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got It", color = TerracottaAccent, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = TerracottaAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(badge.title, color = EspressoText, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (badge.isUnlocked) TerracottaAccent.copy(alpha = 0.2f) else CoffeeClay.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (badge.isUnlocked) "UNLOCKED STAMP" else "LOCKED STAMP",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )
                }
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = EspressoText
                )
            }
        },
        containerColor = ParchmentCream,
        shape = RoundedCornerShape(20.dp)
    )
}
