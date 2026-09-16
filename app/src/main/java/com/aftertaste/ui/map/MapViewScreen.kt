package com.aftertaste.ui.map

import android.Manifest
import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.aftertaste.BuildConfig
import com.aftertaste.ui.components.CoffeeBeanIcon
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.util.GeofenceManager
import com.aftertaste.util.LocationHelper
import com.aftertaste.util.NearbyCafeSpot
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
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
    val latLng: LatLng
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

    val cafes by viewModel.cafes.collectAsState()
    val wishlistCafes by viewModel.wishlist.collectAsState()

    var selectedFilter by remember { mutableStateOf(MapFilter.NEARBY) }
    var selectedPin by remember { mutableStateOf<MapPinItem?>(null) }

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

    // Function to load real location and Places API
    val loadLocationAndPlaces = {
        coroutineScope.launch {
            if (LocationHelper.hasLocationPermission(context)) {
                isLoadingLocation = true
                val loc = LocationHelper.getUserLocation(context)
                userLocation = loc
                isLoadingLocation = false

                if (loc != null) {
                    isLoadingPlaces = true
                    val apiKey = BuildConfig.MAPS_API_KEY
                    val spots = LocationHelper.fetchNearbyCafes(loc.latitude, loc.longitude, apiKey)
                    nearbySpots = spots
                    isLoadingPlaces = false

                    // Register geofences for nearby cafes
                    GeofenceManager.registerCafeGeofences(context, spots)
                }
            }
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            loadLocationAndPlaces()
        }
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(userLocation) {
        userLocation?.let { loc ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(loc.latitude, loc.longitude),
                14.5f
            )
        }
    }

    // Convert nearby spots, visited cafes, and wishlist to real LatLng map pins
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
                        latLng = LatLng(spot.lat, spot.lng)
                    )
                )
            }
        }

        if (selectedFilter == MapFilter.VISITED) {
            for (c in cafes) {
                if (c.visitCount > 0) {
                    val userLat = userLocation?.latitude ?: 0.0
                    val userLng = userLocation?.longitude ?: 0.0
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
                            latLng = LatLng(userLat, userLng)
                        )
                    )
                }
            }
        }

        if (selectedFilter == MapFilter.WISHLIST) {
            for (w in wishlistCafes) {
                val userLat = userLocation?.latitude ?: 0.0
                val userLng = userLocation?.longitude ?: 0.0
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
                        latLng = LatLng(userLat, userLng)
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

            // Map Container / States
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    !hasPermission -> {
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
                                        text = "Grant location permission to discover real nearby cafes on Google Maps.",
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
                    }

                    isLoadingLocation || isLoadingPlaces -> {
                        // State 2: Loading State
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(CoffeeClay),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(color = TerracottaAccent)
                                Text(
                                    text = if (isLoadingLocation) "Getting your location..." else "Searching nearby cafes...",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ParchmentCream,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    userLocation == null -> {
                        // State 3: Location Unavailable
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(CoffeeClay),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = ParchmentCream)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CoffeeCupIcon(modifier = Modifier.size(48.dp))
                                    Text(
                                        text = "Location Unavailable",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )
                                    Text(
                                        text = "Ensure location/GPS is enabled on your device.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = EspressoText.copy(alpha = 0.7f),
                                        textAlign = TextAlign.Center
                                    )
                                    Button(
                                        onClick = { loadLocationAndPlaces() },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = TerracottaAccent,
                                            contentColor = EspressoText
                                        ),
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Retry", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        // State 4: Real Google Map
                        Box(modifier = Modifier.fillMaxSize()) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                properties = MapProperties(
                                    isMyLocationEnabled = true
                                ),
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = true,
                                    myLocationButtonEnabled = true
                                )
                            ) {
                                mapPins.forEach { pin ->
                                    Marker(
                                        state = MarkerState(position = pin.latLng),
                                        title = pin.name,
                                        snippet = "${pin.location} • ${String.format(Locale.getDefault(), "%.1f km", pin.distanceKm)}",
                                        onClick = {
                                            selectedPin = pin
                                            true
                                        }
                                    )
                                }
                            }

                            // Current Location Overlay Badge
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
                                            text = "GPS: (${String.format(Locale.getDefault(), "%.3f", userLocation?.latitude)}, ${String.format(Locale.getDefault(), "%.3f", userLocation?.longitude)})",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = ParchmentCream,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // Empty State Overlay if no cafes found
                            if (mapPins.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                        .align(Alignment.Center)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(18.dp),
                                        color = ParchmentCream.copy(alpha = 0.95f),
                                        shadowElevation = 8.dp
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            CoffeeCupIcon(modifier = Modifier.size(42.dp))
                                            Text(
                                                text = "No cafes found nearby.",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = EspressoText
                                            )
                                            Text(
                                                text = "Places API returned 0 results around your location.",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = EspressoText.copy(alpha = 0.7f),
                                                textAlign = TextAlign.Center
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
