package com.aftertaste.ui.search

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.ui.components.CoffeeBeanRatingBar
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchFilterScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onNavigateToVisitDetail: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val allVisits by viewModel.visits.collectAsState()
    val availableTags by viewModel.allTags.collectAsState()

    var minRatingThreshold by remember { mutableFloatStateOf(0f) }
    var filterPowerOutlets by remember { mutableStateOf(false) }
    var filterGoodWifi by remember { mutableStateOf(false) }
    var filterWouldReturnOnly by remember { mutableStateOf(false) }
    val selectedNoiseLevels = remember { mutableStateListOf<String>() }
    val selectedVibeTags = remember { mutableStateListOf<String>() }

    var showFiltersPanel by remember { mutableStateOf(true) }

    // Client-side filtering across visits
    val filteredVisits = remember(
        allVisits,
        minRatingThreshold,
        filterPowerOutlets,
        filterGoodWifi,
        filterWouldReturnOnly,
        selectedNoiseLevels.toList(),
        selectedVibeTags.toList()
    ) {
        allVisits.filter { details ->
            val v = details.visit

            // Rating check
            if (v.overallRating < minRatingThreshold) return@filter false

            // Power outlets check
            if (filterPowerOutlets && v.powerOutlets != true) return@filter false

            // Good wifi check (wifiRating >= 4.0)
            if (filterGoodWifi && (v.wifiRating ?: 0f) < 4.0f) return@filter false

            // Would return check
            if (filterWouldReturnOnly && !v.wouldReturn) return@filter false

            // Noise level check
            if (selectedNoiseLevels.isNotEmpty()) {
                val noise = v.noiseLevel ?: ""
                if (!selectedNoiseLevels.any { it.equals(noise, ignoreCase = true) }) return@filter false
            }

            // Vibe tags check
            if (selectedVibeTags.isNotEmpty()) {
                val visitTagNames = details.tags.map { it.tagName.lowercase() }
                val matchesAllTags = selectedVibeTags.all { tag ->
                    visitTagNames.contains(tag.lowercase())
                }
                if (!matchesAllTags) return@filter false
            }

            true
        }
    }

    val handleGoogleWebSearch = { queryText: String ->
        val query = if (queryText.isNotBlank()) queryText else "cafes near me"
        val url = "https://www.google.com/search?q=${Uri.encode(query)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CoffeeClay
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Item 1: Search Text Field & Google Search Action Bar
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search cafes, Tim Hortons, Starbucks, notes...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = TerracottaAccent)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = CoffeeClay)
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TerracottaAccent,
                            unfocusedBorderColor = CoffeeOutline,
                            focusedContainerColor = ParchmentCream,
                            unfocusedContainerColor = ParchmentCream,
                            focusedTextColor = EspressoText,
                            unfocusedTextColor = EspressoText
                        )
                    )

                    if (searchQuery.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TerracottaAccent,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { handleGoogleWebSearch(searchQuery) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Search, contentDescription = null, tint = EspressoText, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Search Google for '$searchQuery'",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = EspressoText, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Item 2: Collapsible Filters Panel Card
            item {
                AnimatedVisibility(visible = showFiltersPanel) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Refine & Filter",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )

                            // Rating Threshold Slider
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Minimum Rating",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = EspressoText
                                    )
                                    Text(
                                        text = if (minRatingThreshold > 0f) String.format(Locale.getDefault(), ">= %.1f ★", minRatingThreshold) else "Any Rating",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TerracottaAccent
                                    )
                                }
                                Slider(
                                    value = minRatingThreshold,
                                    onValueChange = { minRatingThreshold = it },
                                    valueRange = 0f..5f,
                                    steps = 9,
                                    colors = SliderDefaults.colors(
                                        thumbColor = TerracottaAccent,
                                        activeTrackColor = TerracottaAccent,
                                        inactiveTrackColor = CoffeeOutline
                                    )
                                )
                            }

                            // Feature Toggle Chips
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                FilterChip(
                                    selected = filterPowerOutlets,
                                    onClick = { filterPowerOutlets = !filterPowerOutlets },
                                    label = { Text("Power Outlets") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Power, contentDescription = null, modifier = Modifier.size(16.dp))
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TerracottaAccent,
                                        selectedLabelColor = EspressoText
                                    )
                                )

                                FilterChip(
                                    selected = filterGoodWifi,
                                    onClick = { filterGoodWifi = !filterGoodWifi },
                                    label = { Text("Good Wifi (4.0+)") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(16.dp))
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TerracottaAccent,
                                        selectedLabelColor = EspressoText
                                    )
                                )

                                FilterChip(
                                    selected = filterWouldReturnOnly,
                                    onClick = { filterWouldReturnOnly = !filterWouldReturnOnly },
                                    label = { Text("Would Return Only") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TerracottaAccent,
                                        selectedLabelColor = EspressoText
                                    )
                                )
                            }

                            // Noise Level Filter
                            Column {
                                Text(
                                    text = "Noise Level",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EspressoText.copy(alpha = 0.7f)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Low", "Medium", "High").forEach { level ->
                                        val isSelected = selectedNoiseLevels.contains(level)
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                if (isSelected) selectedNoiseLevels.remove(level) else selectedNoiseLevels.add(level)
                                            },
                                            label = { Text(level) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = TerracottaAccent,
                                                selectedLabelColor = EspressoText
                                            )
                                        )
                                    }
                                }
                            }

                            // Vibe Tags Filter
                            if (availableTags.isNotEmpty()) {
                                Column {
                                    Text(
                                        text = "Vibe Tags Multi-select",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = EspressoText.copy(alpha = 0.7f)
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        availableTags.forEach { tag ->
                                            val isSelected = selectedVibeTags.contains(tag)
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = {
                                                    if (isSelected) selectedVibeTags.remove(tag) else selectedVibeTags.add(tag)
                                                },
                                                label = { Text("#$tag") },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = TerracottaAccent,
                                                    selectedLabelColor = EspressoText
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Item 3: Results Counter Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Results",
                        style = MaterialTheme.typography.titleMedium,
                        color = ParchmentCream,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        shape = CircleShape,
                        color = TerracottaAccent
                    ) {
                        Text(
                            text = "${filteredVisits.size} Matches Found",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                    }
                }
            }

            // Item 4: Empty State or Results List
            if (filteredVisits.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = ParchmentCream)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TerracottaAccent,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = if (searchQuery.isBlank()) "No visits logged yet" else "No local logs for '$searchQuery'",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )
                            Text(
                                text = "Search Google Web to discover menus, photos, or add '$searchQuery' to your coffee wishlist.",
                                style = MaterialTheme.typography.bodySmall,
                                color = EspressoText.copy(alpha = 0.7f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Button(
                                onClick = { handleGoogleWebSearch(searchQuery) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TerracottaAccent,
                                    contentColor = EspressoText
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Search '$searchQuery' on Google 🔍", fontWeight = FontWeight.Bold)
                            }

                            if (searchQuery.isNotBlank()) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.addToWishlist(cafeName = searchQuery, location = "")
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = TerracottaAccent)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add '$searchQuery' to Wishlist", fontWeight = FontWeight.Bold, color = EspressoText)
                                }
                            }
                        }
                    }
                }
            } else {
                items(filteredVisits, key = { it.visit.id }) { visitDetails ->
                    SearchResultVisitCard(
                        visitDetails = visitDetails,
                        onClick = { onNavigateToVisitDetail(visitDetails.visit.id) }
                    )
                }
            }

            // Bottom Spacing Item for Navigation Bar Clearance
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
fun SearchResultVisitCard(
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
                }

                Text(
                    text = dateFormatter.format(Date(v.visitDate)),
                    style = MaterialTheme.typography.labelSmall,
                    color = EspressoText.copy(alpha = 0.5f)
                )
            }

            // Sub info (items and tags)
            if (detailsHasExtraInfo(visitDetails)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (visitDetails.items.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CoffeeClay.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${visitDetails.items.size} items ordered",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = EspressoText
                            )
                        }
                    }

                    if (visitDetails.tags.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TerracottaAccent.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = visitDetails.tags.take(2).joinToString(" ") { "#${it.tagName}" },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = EspressoText
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CoffeeBeanRatingBar(
                    rating = v.overallRating,
                    isSelectable = false,
                    beanSize = 14.dp,
                    beanPadding = 2.dp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", v.overallRating),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaAccent
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Details",
                        tint = CoffeeClay,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private fun detailsHasExtraInfo(details: CafeVisitWithDetails): Boolean {
    return details.items.isNotEmpty() || details.tags.isNotEmpty()
}
