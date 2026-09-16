package com.aftertaste.ui.cafe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aftertaste.data.local.entity.Cafe
import com.aftertaste.ui.components.CoffeeBeanRatingBar
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class CafeSortOption(val label: String) {
    RATING_DESC("Highest Rated"),
    MOST_VISITED("Most Visited"),
    MOST_RECENT("Most Recent"),
    NAME_ASC("Name (A-Z)")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CafeListScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onCafeSelected: (String) -> Unit = {},
    onAddVisitClicked: () -> Unit = {},
) {
    val cafes by viewModel.cafes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedSortOption by remember { mutableStateOf(CafeSortOption.RATING_DESC) }

    // Filter and Sort Cafes
    val filteredAndSortedCafes = remember(cafes, searchQuery, selectedSortOption) {
        val filtered = cafes.filter { cafe ->
            cafe.cafeName.contains(searchQuery, ignoreCase = true) ||
                    cafe.location.contains(searchQuery, ignoreCase = true)
        }
        when (selectedSortOption) {
            CafeSortOption.RATING_DESC -> filtered.sortedByDescending { it.avgOverallRating }
            CafeSortOption.MOST_VISITED -> filtered.sortedByDescending { it.visitCount }
            CafeSortOption.MOST_RECENT -> filtered.sortedByDescending { it.lastVisited }
            CafeSortOption.NAME_ASC -> filtered.sortedBy { it.cafeName.lowercase() }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CoffeeCupIcon(modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "aftertaste",
                            style = AfterTasteTitleStyle,
                            fontSize = 32.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoffeeClay
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddVisitClicked,
                containerColor = TerracottaAccent,
                contentColor = EspressoText,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log New Visit")
            }
        },
        containerColor = CoffeeClay
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search cafes, spots & roasters...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ParchmentCream) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerracottaAccent,
                    unfocusedBorderColor = ParchmentCream.copy(alpha = 0.4f),
                    focusedContainerColor = CoffeeClay,
                    unfocusedContainerColor = CoffeeClay,
                    focusedTextColor = ParchmentCream,
                    unfocusedTextColor = ParchmentCream,
                    focusedPlaceholderColor = ParchmentCream.copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = ParchmentCream.copy(alpha = 0.6f)
                )
            )

            // Sort Option Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CafeSortOption.entries.forEach { option ->
                    val isSelected = option == selectedSortOption
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedSortOption = option },
                        label = { Text(option.label, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TerracottaAccent,
                            selectedLabelColor = EspressoText,
                            containerColor = ParchmentCream.copy(alpha = 0.15f),
                            labelColor = ParchmentCream
                        )
                    )
                }
            }

            // Pinterest-Style Staggered Grid
            if (filteredAndSortedCafes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CoffeeCupIcon(modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isBlank()) "No cafes logged yet!" else "No cafes matching '$searchQuery'",
                            style = MaterialTheme.typography.titleMedium,
                            color = ParchmentCream,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalItemSpacing = 10.dp,
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(filteredAndSortedCafes, key = { _, c -> c.cafeName }) { index, cafe ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            PinterestCafeCard(
                                cafe = cafe,
                                index = index,
                                onClick = { onCafeSelected(cafe.cafeName) },
                                onToggleWishlist = {
                                    if (cafe.isWishlist) {
                                        viewModel.removeFromWishlistByName(cafe.cafeName)
                                    } else {
                                        viewModel.addToWishlist(cafe.cafeName, cafe.location)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PinterestCafeCard(
    cafe: Cafe,
    index: Int,
    onClick: () -> Unit,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        label = "cardScale"
    )

    // Pinterest dynamic aspect heights
    val imageHeight = remember(index) {
        when (index % 3) {
            0 -> 160.dp
            1 -> 120.dp
            else -> 140.dp
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Pinterest Image Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(CoffeeClay.copy(alpha = 0.2f))
            ) {
                if (cafe.coverPhotoUri != null) {
                    AsyncImage(
                        model = cafe.coverPhotoUri,
                        contentDescription = cafe.cafeName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CoffeeCupIcon(modifier = Modifier.size(42.dp))
                    }
                }

                // Wishlist Floating Action Icon Overlay
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ParchmentCream.copy(alpha = 0.85f))
                ) {
                    Icon(
                        imageVector = if (cafe.isWishlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Wishlist",
                        tint = TerracottaAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (cafe.avgOverallRating > 0f) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EspressoText.copy(alpha = 0.85f),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = String.format(Locale.getDefault(), "%.1f ★", cafe.avgOverallRating),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaAccent,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = cafe.cafeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText,
                    maxLines = 1
                )

                if (cafe.location.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CoffeeClay,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = cafe.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = EspressoText.copy(alpha = 0.7f),
                            maxLines = 1,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (cafe.visitCount > 0) {
                        CoffeeBeanRatingBar(
                            rating = cafe.avgOverallRating,
                            isSelectable = false,
                            beanSize = 12.dp,
                            beanPadding = 1.dp
                        )
                    } else {
                        Surface(
                            shape = CircleShape,
                            color = TerracottaAccent.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Wishlist",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaAccent,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Text(
                        text = if (cafe.visitCount > 0) "${cafe.visitCount} visit${if (cafe.visitCount > 1) "s" else ""}" else "Saved",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = CoffeeClay,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
