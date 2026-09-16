package com.aftertaste.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.ui.components.CoffeeBeanIcon
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.util.LocationHelper
import com.aftertaste.util.NearbyCafeSpot
import java.util.Locale
import kotlin.math.abs

enum class MapFilter {
    NEARBY, VISITED, WISHLIST
}

data class MapPinItem(
    val id: String,
    val name: String,
    val location: String,
    val distanceKm: Double,
    val isWishlist: Boolean,
    val isNearbySpot: Boolean,
    val rating: Float,
    val visitCount: Int,
    val normalizedX: Float,
    val normalizedY: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onNavigateToCafeDetail: (cafeName: String) -> Unit = {},
    onConvertToVisit: (cafeName: String, location: String) -> Unit = { _, _ -> }
) {
    val cafes by viewModel.cafes.collectAsState()
    val wishlistCafes by viewModel.wishlist.collectAsState()

    var selectedFilter by remember { mutableStateOf(MapFilter.NEARBY) }
    var selectedPin by remember { mutableStateOf<MapPinItem?>(null) }

    // Dynamically fetch nearby cafes around user location when screen loads
    val nearbySpots = remember { LocationHelper.fetchNearbyCafes() }

    // Map gesture state
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    // Convert nearby spots, visited cafes, and wishlist to map pins
    val mapPins = remember(cafes, wishlistCafes, nearbySpots, selectedFilter) {
        val pins = mutableListOf<MapPinItem>()

        if (selectedFilter == MapFilter.NEARBY) {
            nearbySpots.forEachIndexed { index, spot ->
                pins.add(
                    MapPinItem(
                        id = spot.id,
                        name = spot.name,
                        location = spot.address,
                        distanceKm = spot.distanceKm,
                        isWishlist = false,
                        isNearbySpot = true,
                        rating = spot.rating,
                        visitCount = 0,
                        normalizedX = 0.2f + (index % 3) * 0.28f,
                        normalizedY = 0.2f + (index / 3) * 0.22f
                    )
                )
            }
        }

        if (selectedFilter == MapFilter.VISITED) {
            for (c in cafes) {
                if (c.visitCount > 0) {
                    val coords = generateNormalizedCoords(c.cafeName, c.location)
                    pins.add(
                        MapPinItem(
                            id = "cafe_${c.cafeName}",
                            name = c.cafeName,
                            location = c.location,
                            distanceKm = 0.8,
                            isWishlist = false,
                            isNearbySpot = false,
                            rating = c.avgOverallRating,
                            visitCount = c.visitCount,
                            normalizedX = coords.first,
                            normalizedY = coords.second
                        )
                    )
                }
            }
        }

        if (selectedFilter == MapFilter.WISHLIST) {
            for (w in wishlistCafes) {
                val coords = generateNormalizedCoords(w.cafeName, w.location)
                pins.add(
                    MapPinItem(
                        id = "wish_${w.id}_${w.cafeName}",
                        name = w.cafeName,
                        location = w.location,
                        distanceKm = 1.2,
                        isWishlist = true,
                        isNearbySpot = false,
                        rating = 0f,
                        visitCount = 0,
                        normalizedX = coords.first,
                        normalizedY = coords.second
                    )
                )
            }
        }

        pins
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Map, contentDescription = null, tint = TerracottaAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "aftertaste map",
                            style = AfterTasteTitleStyle,
                            fontSize = 30.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoffeeClay
                )
            )
        },
        containerColor = CoffeeClay
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = selectedFilter == MapFilter.NEARBY,
                    onClick = { selectedFilter = MapFilter.NEARBY },
                    label = { Text("Nearby Cafes 📍 (${nearbySpots.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TerracottaAccent,
                        selectedLabelColor = EspressoText,
                        containerColor = ParchmentCream.copy(alpha = 0.2f),
                        labelColor = ParchmentCream
                    )
                )

                FilterChip(
                    selected = selectedFilter == MapFilter.VISITED,
                    onClick = { selectedFilter = MapFilter.VISITED },
                    label = { Text("Visited ☕") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TerracottaAccent,
                        selectedLabelColor = EspressoText,
                        containerColor = ParchmentCream.copy(alpha = 0.2f),
                        labelColor = ParchmentCream
                    )
                )

                FilterChip(
                    selected = selectedFilter == MapFilter.WISHLIST,
                    onClick = { selectedFilter = MapFilter.WISHLIST },
                    label = { Text("Wishlist ♡") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TerracottaAccent,
                        selectedLabelColor = EspressoText,
                        containerColor = ParchmentCream.copy(alpha = 0.2f),
                        labelColor = ParchmentCream
                    )
                )
            }

            // Interactive Canvas Map Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE8DFC8))
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            zoomScale = (zoomScale * zoom).coerceIn(0.8f, 3.0f)
                            panOffsetX = (panOffsetX + pan.x).coerceIn(-500f, 500f)
                            panOffsetY = (panOffsetY + pan.y).coerceIn(-500f, 500f)
                        }
                    }
            ) {
                // Background map grid pattern
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val gridStep = 80f * zoomScale
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                    var x = panOffsetX % gridStep
                    while (x < size.width) {
                        drawLine(
                            color = Color(0xFFC7B89F),
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f,
                            pathEffect = pathEffect
                        )
                        x += gridStep
                    }

                    var y = panOffsetY % gridStep
                    while (y < size.height) {
                        drawLine(
                            color = Color(0xFFC7B89F),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f,
                            pathEffect = pathEffect
                        )
                        y += gridStep
                    }
                }

                // Current Location Badge Overlay
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CoffeeClay.copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.NearMe, contentDescription = null, tint = TerracottaAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Location: Live GPS Detected",
                                style = MaterialTheme.typography.labelMedium,
                                color = ParchmentCream,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Zoom & Recenter Controls
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { zoomScale = (zoomScale * 1.25f).coerceAtMost(3.0f) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CoffeeClay)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = ParchmentCream)
                    }

                    IconButton(
                        onClick = { zoomScale = (zoomScale / 1.25f).coerceAtLeast(0.8f) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CoffeeClay)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = ParchmentCream)
                    }

                    IconButton(
                        onClick = {
                            zoomScale = 1.0f
                            panOffsetX = 0f
                            panOffsetY = 0f
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(TerracottaAccent)
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Reset View", tint = EspressoText)
                    }
                }

                // Map Pins Display
                if (mapPins.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ParchmentCream.copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = "No map pins available for current filter",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = EspressoText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    mapPins.forEach { pin ->
                        val isSelected = selectedPin?.id == pin.id

                        val pinX = (pin.normalizedX * 800f * zoomScale + panOffsetX).toInt()
                        val pinY = (pin.normalizedY * 1200f * zoomScale + panOffsetY).toInt()

                        Box(
                            modifier = Modifier
                                .offset { IntOffset(pinX, pinY) }
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedPin = pin }
                        ) {
                            MapPinComposable(
                                pin = pin,
                                isSelected = isSelected
                            )
                        }
                    }
                }

                // Floating Preview Card when pin is selected
                selectedPin?.let { pin ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        FloatingCafeMapCard(
                            pin = pin,
                            onClose = { selectedPin = null },
                            onNavigateToCafeDetail = {
                                if (pin.isNearbySpot || pin.isWishlist) {
                                    onConvertToVisit(pin.name, pin.location)
                                } else {
                                    onNavigateToCafeDetail(pin.name)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MapPinComposable(
    pin: MapPinItem,
    isSelected: Boolean
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) TerracottaAccent else if (pin.isNearbySpot) TerracottaAccent.copy(alpha = 0.9f) else if (pin.isWishlist) ParchmentCream else CoffeeClay,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 3.dp else 1.5.dp,
            color = if (isSelected) EspressoText else if (pin.isWishlist) TerracottaAccent else ParchmentCream
        ),
        shadowElevation = if (isSelected) 8.dp else 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pin.isWishlist) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = TerracottaAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = pin.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText,
                    fontSize = 11.sp
                )
            } else {
                CoffeeCupIcon(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${pin.name} ",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (pin.isNearbySpot) EspressoText else ParchmentCream,
                    fontSize = 11.sp
                )
                Text(
                    text = String.format(Locale.getDefault(), "%.1fkm", pin.distanceKm),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (pin.isNearbySpot) EspressoText else TerracottaAccent,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun FloatingCafeMapCard(
    pin: MapPinItem,
    onClose: () -> Unit,
    onNavigateToCafeDetail: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (pin.isWishlist) TerracottaAccent.copy(alpha = 0.2f) else CoffeeClay.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pin.isWishlist) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, tint = TerracottaAccent)
                        } else {
                            CoffeeBeanIcon(modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = pin.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        if (pin.location.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = pin.location,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EspressoText.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = CoffeeClay.copy(alpha = 0.15f),
                    modifier = Modifier.clickable(onClick = onClose)
                ) {
                    Text(
                        text = "✕",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = EspressoText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = TerracottaAccent.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Distance: ${String.format(Locale.getDefault(), "%.1f km away", pin.distanceKm)}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = EspressoText,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onNavigateToCafeDetail,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TerracottaAccent,
                        contentColor = EspressoText
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (pin.isNearbySpot || pin.isWishlist) "Log Visit Here" else "View Details",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

private fun generateNormalizedCoords(cafeName: String, location: String): Pair<Float, Float> {
    val hash = abs((cafeName + location).hashCode())
    val x = 0.15f + ((hash % 70) / 100f)
    val y = 0.15f + (((hash / 70) % 70) / 100f)
    return Pair(x, y)
}
