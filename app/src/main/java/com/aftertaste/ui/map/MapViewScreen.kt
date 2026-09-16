package com.aftertaste.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.aftertaste.ui.components.CoffeeBeanIcon
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.util.GeofenceManager
import com.aftertaste.util.LocationHelper
import com.aftertaste.util.NearbyCafeSpot
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale

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
    val reviewCount: Int,
    val geoPoint: GeoPoint
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onNavigateToCafeDetail: (cafeName: String) -> Unit = {},
    onConvertToVisit: (cafeName: String, location: String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Configure OSMDroid
    DisposableEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
        onDispose { }
    }

    val cafes by viewModel.cafes.collectAsState()
    val wishlistCafes by viewModel.wishlist.collectAsState()

    var selectedFilter by remember { mutableStateOf(MapFilter.NEARBY) }
    var selectedPin by remember { mutableStateOf<MapPinItem?>(null) }

    var searchCoordinateQuery by remember { mutableStateOf("") }
    var hasPermission by remember { mutableStateOf(LocationHelper.hasLocationPermission(context)) }
    var isLoadingLocation by remember { mutableStateOf(false) }
    var isLoadingPlaces by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<android.location.Location?>(null) }
    var nearbySpots by remember { mutableStateOf<List<NearbyCafeSpot>>(emptyList()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        hasPermission = granted
    }

    val loadLocationAndPlaces = {
        coroutineScope.launch {
            if (LocationHelper.hasLocationPermission(context)) {
                isLoadingLocation = true
                val loc = LocationHelper.getUserLocation(context)
                userLocation = loc
                isLoadingLocation = false

                if (loc != null) {
                    isLoadingPlaces = true
                    val spots = LocationHelper.fetchNearbyCafes(loc.latitude, loc.longitude)
                    nearbySpots = spots
                    isLoadingPlaces = false

                    GeofenceManager.registerCafeGeofences(context, spots)
                }
            }
        }
    }

    // Helper to jump to custom coordinates input e.g. "34.155, 62.391" or "62391 34155"
    val jumpToCustomCoordinates = { inputStr: String ->
        coroutineScope.launch {
            val numbers = Regex("[-+]?\\d*\\.?\\d+").findAll(inputStr).map { it.value.toDoubleOrNull() }.filterNotNull().toList()
            if (numbers.size >= 2) {
                var lat = numbers[0]
                var lng = numbers[1]

                // Handle integer digits like 34155 -> 34.155, 62391 -> 62.391
                if (lat > 90 || lat < -90) {
                    lat = lat / 1000.0
                }
                if (lng > 180 || lng < -180) {
                    lng = lng / 1000.0
                }

                val customLoc = android.location.Location("custom").apply {
                    latitude = lat
                    longitude = lng
                }
                userLocation = customLoc

                isLoadingPlaces = true
                val spots = LocationHelper.fetchNearbyCafes(lat, lng)
                nearbySpots = spots
                isLoadingPlaces = false
            }
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            loadLocationAndPlaces()
        }
    }

    // Convert nearby spots, visited cafes, and wishlist to GeoPoint map pins
    val mapPins = remember(cafes, wishlistCafes, nearbySpots, selectedFilter) {
        val pins = mutableListOf<MapPinItem>()

        if (selectedFilter == MapFilter.NEARBY) {
            nearbySpots.forEach { spot ->
                pins.add(
                    MapPinItem(
                        id = spot.id,
                        name = spot.name,
                        location = spot.address,
                        distanceKm = spot.distanceKm,
                        isWishlist = false,
                        isNearbySpot = true,
                        rating = spot.rating,
                        reviewCount = spot.reviewCount,
                        geoPoint = GeoPoint(spot.lat, spot.lng)
                    )
                )
            }
        }

        if (selectedFilter == MapFilter.VISITED) {
            for (c in cafes) {
                if (c.visitCount > 0) {
                    val userLat = userLocation?.latitude ?: 34.155
                    val userLng = userLocation?.longitude ?: 62.391
                    pins.add(
                        MapPinItem(
                            id = "cafe_${c.cafeName}",
                            name = c.cafeName,
                            location = c.location,
                            distanceKm = 0.0,
                            isWishlist = false,
                            isNearbySpot = false,
                            rating = c.avgOverallRating,
                            reviewCount = c.visitCount,
                            geoPoint = GeoPoint(userLat, userLng)
                        )
                    )
                }
            }
        }

        if (selectedFilter == MapFilter.WISHLIST) {
            for (w in wishlistCafes) {
                val userLat = userLocation?.latitude ?: 34.155
                val userLng = userLocation?.longitude ?: 62.391
                pins.add(
                    MapPinItem(
                        id = "wish_${w.id}_${w.cafeName}",
                        name = w.cafeName,
                        location = w.location,
                        distanceKm = 0.0,
                        isWishlist = true,
                        isNearbySpot = false,
                        rating = 0f,
                        reviewCount = 0,
                        geoPoint = GeoPoint(userLat, userLng)
                    )
                )
            }
        }

        pins
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CoffeeClay
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Coordinate / Location Search Field Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchCoordinateQuery,
                    onValueChange = {
                        searchCoordinateQuery = it
                        if (it.length >= 5) {
                            jumpToCustomCoordinates(it)
                        }
                    },
                    placeholder = { Text("Jump to coords e.g. 34.155, 62.391", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TerracottaAccent)
                    },
                    trailingIcon = {
                        if (searchCoordinateQuery.isNotEmpty()) {
                            IconButton(onClick = { searchCoordinateQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = CoffeeClay)
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
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
            }

            // Filter Chips Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
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

            // Map Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (!hasPermission) {
                    // State 1: Permission Required
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ParchmentCream),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = CoffeeClay),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocationOff,
                                    contentDescription = null,
                                    tint = TerracottaAccent,
                                    modifier = Modifier.size(52.dp)
                                )
                                Text(
                                    text = "Location Permission Needed",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ParchmentCream,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Grant location permission to discover real nearby cafes on OpenStreetMap.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ParchmentCream.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TerracottaAccent,
                                        contentColor = EspressoText
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text("Grant Location Permission", fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", context.packageName, null)
                                        )
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ParchmentCream.copy(alpha = 0.2f),
                                        contentColor = ParchmentCream
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text("Open App Settings", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    // OpenStreetMap View
                    AndroidView(
                        factory = { ctx ->
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(true)
                                controller.setZoom(15.5)
                            }
                        },
                        update = { mapView ->
                            mapView.overlays.clear()

                            val loc = userLocation
                            val centerGeoPoint = if (loc != null) GeoPoint(loc.latitude, loc.longitude) else GeoPoint(34.155, 62.391)
                            mapView.controller.animateTo(centerGeoPoint)

                            // User position marker
                            val userMarker = Marker(mapView).apply {
                                position = centerGeoPoint
                                title = "Target Location 📍"
                                snippet = "(${String.format(Locale.getDefault(), "%.3f", centerGeoPoint.latitude)}, ${String.format(Locale.getDefault(), "%.3f", centerGeoPoint.longitude)})"
                                icon = createCoffeeMarkerDrawable(context, "#C05A3E", "📍")
                            }
                            mapView.overlays.add(userMarker)

                            mapPins.forEach { pin ->
                                val cafeMarker = Marker(mapView).apply {
                                    position = pin.geoPoint
                                    title = pin.name
                                    snippet = "${pin.location} • ${String.format(Locale.getDefault(), "%.1f km", pin.distanceKm)}"
                                    icon = createCoffeeMarkerDrawable(
                                        context,
                                        if (pin.isWishlist) "#F4ECE1" else "#2A1810",
                                        if (pin.isWishlist) "♡" else "☕"
                                    )
                                    setOnMarkerClickListener { _, _ ->
                                        selectedPin = pin
                                        true
                                    }
                                }
                                mapView.overlays.add(cafeMarker)
                            }

                            mapView.invalidate()
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Floating GPS Status Pill Overlay
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
                                if (isLoadingLocation || isLoadingPlaces) {
                                    CircularProgressIndicator(
                                        color = TerracottaAccent,
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isLoadingLocation) "Detecting Location..." else "Searching Nearby Cafes...",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ParchmentCream,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else if (userLocation != null) {
                                    Icon(Icons.Default.NearMe, contentDescription = null, tint = TerracottaAccent, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Coords: (${String.format(Locale.getDefault(), "%.3f", userLocation?.latitude)}, ${String.format(Locale.getDefault(), "%.3f", userLocation?.longitude)})",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ParchmentCream,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = TerracottaAccent, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Tap to Refresh GPS Location",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ParchmentCream,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable { loadLocationAndPlaces() }
                                    )
                                }
                            }
                        }
                    }

                    // Location Refresh Ring Floating Button (Top Right Ring)
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = TerracottaAccent,
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .clickable { loadLocationAndPlaces() }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (isLoadingLocation || isLoadingPlaces) {
                                    CircularProgressIndicator(
                                        color = EspressoText,
                                        modifier = Modifier.size(22.dp),
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh Location Ring",
                                        tint = EspressoText,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
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
                        text = if (pin.distanceKm > 0) "Distance: ${String.format(Locale.getDefault(), "%.1f km away", pin.distanceKm)}" else "Rating: ${String.format(Locale.getDefault(), "%.1f ★", pin.rating)}",
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

// Helper to create custom colored marker drawable for OpenStreetMap
private fun createCoffeeMarkerDrawable(context: Context, colorHex: String, symbol: String): Drawable {
    val px = (36 * context.resources.displayMetrics.density).toInt()
    val bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.parseColor(colorHex)
        style = Paint.Style.FILL
    }
    canvas.drawCircle(px / 2f, px / 2f, px / 2f - 2f, paint)

    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    canvas.drawCircle(px / 2f, px / 2f, px / 2f - 3f, strokePaint)

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = px * 0.45f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(symbol, px / 2f, px / 2f + (px * 0.15f), textPaint)

    return BitmapDrawable(context.resources, bitmap)
}
