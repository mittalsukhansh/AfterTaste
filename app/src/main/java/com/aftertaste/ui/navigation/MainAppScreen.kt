package com.aftertaste.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.ui.cafe.CafeDetailScreen
import com.aftertaste.ui.cafe.CafeListScreen
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.dashboard.DashboardScreen
import com.aftertaste.ui.map.MapViewScreen
import com.aftertaste.ui.search.SearchFilterScreen
import com.aftertaste.ui.settings.SettingsScreen
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.ui.visit.AddEditVisitScreen
import com.aftertaste.ui.visit.VisitDetailScreen
import com.aftertaste.ui.wishlist.WishlistScreen

sealed class BottomTab(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomTab("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Directory : BottomTab("directory", "Cafes", Icons.Default.FreeBreakfast)
    object MapView : BottomTab("map", "Map", Icons.Default.Map)
    object Wishlist : BottomTab("wishlist", "Wishlist", Icons.Default.Bookmark)
    object Search : BottomTab("search", "Search", Icons.Default.Search)
    object Settings : BottomTab("settings", "Settings", Icons.Default.Settings)
}

sealed class AppDestination {
    data class Tab(val tab: BottomTab) : AppDestination()
    data class CafeDetail(val cafeName: String) : AppDestination()
    data class AddEditVisit(
        val visitId: Long? = null,
        val prefilledCafeName: String? = null,
        val prefilledLocation: String? = null
    ) : AppDestination()
    data class VisitDetail(val visitId: Long) : AppDestination()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfterTasteTopAppBar(
    currentDestination: AppDestination,
    onBackClick: (() -> Unit)? = null
) {
    val subtitleDash = when (currentDestination) {
        is AppDestination.Tab -> when (currentDestination.tab) {
            BottomTab.Dashboard -> "dashboard"
            BottomTab.Directory -> "directory"
            BottomTab.MapView -> "map"
            BottomTab.Wishlist -> "wishlist"
            BottomTab.Search -> "search"
            BottomTab.Settings -> "settings"
        }
        is AppDestination.CafeDetail -> "cafe details"
        is AppDestination.AddEditVisit -> "log visit"
        is AppDestination.VisitDetail -> "visit details"
    }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CoffeeCupIcon(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "aftertaste",
                    style = AfterTasteTitleStyle,
                    fontSize = 26.sp,
                    color = ParchmentCream
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "— $subtitleDash",
                    style = MaterialTheme.typography.titleMedium,
                    color = TerracottaAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ParchmentCream
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CoffeeClay
        )
    )
}

@Composable
fun MainAppScreen(
    viewModel: CafeViewModel,
    onResetOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentDestination by remember {
        mutableStateOf<AppDestination>(AppDestination.Tab(BottomTab.Dashboard))
    }

    val bottomTabs = remember {
        listOf(
            BottomTab.Dashboard,
            BottomTab.Directory,
            BottomTab.MapView,
            BottomTab.Wishlist,
            BottomTab.Search,
            BottomTab.Settings
        )
    }

    val isTabDestination = currentDestination is AppDestination.Tab
    val activeTab = (currentDestination as? AppDestination.Tab)?.tab

    val onBackClick: (() -> Unit)? = if (!isTabDestination) {
        {
            currentDestination = AppDestination.Tab(
                when (currentDestination) {
                    is AppDestination.CafeDetail -> BottomTab.Directory
                    else -> BottomTab.Dashboard
                }
            )
        }
    } else null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AfterTasteTopAppBar(
                currentDestination = currentDestination,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            if (isTabDestination) {
                NavigationBar(
                    containerColor = CoffeeClay,
                    contentColor = ParchmentCream,
                    tonalElevation = 8.dp
                ) {
                    bottomTabs.forEach { tab ->
                        val selected = activeTab == tab
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                currentDestination = AppDestination.Tab(tab)
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EspressoText,
                                selectedTextColor = TerracottaAccent,
                                indicatorColor = TerracottaAccent,
                                unselectedIconColor = ParchmentCream.copy(alpha = 0.6f),
                                unselectedTextColor = ParchmentCream.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        },
        containerColor = CoffeeClay
    ) { innerPadding ->
        when (val dest = currentDestination) {
            is AppDestination.Tab -> {
                when (dest.tab) {
                    BottomTab.Dashboard -> {
                        DashboardScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToAddVisit = {
                                currentDestination = AppDestination.AddEditVisit()
                            },
                            onNavigateToWishlist = {
                                currentDestination = AppDestination.Tab(BottomTab.Wishlist)
                            },
                            onNavigateToMapView = {
                                currentDestination = AppDestination.Tab(BottomTab.MapView)
                            },
                            onNavigateToSearch = {
                                currentDestination = AppDestination.Tab(BottomTab.Search)
                            },
                            onNavigateToSettings = {
                                currentDestination = AppDestination.Tab(BottomTab.Settings)
                            },
                            onNavigateToVisitDetail = { visitId ->
                                currentDestination = AppDestination.VisitDetail(visitId)
                            }
                        )
                    }
                    BottomTab.Directory -> {
                        CafeListScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding),
                            onCafeSelected = { cafeName ->
                                currentDestination = AppDestination.CafeDetail(cafeName)
                            },
                            onAddVisitClicked = {
                                currentDestination = AppDestination.AddEditVisit()
                            }
                        )
                    }
                    BottomTab.MapView -> {
                        MapViewScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToCafeDetail = { cafeName ->
                                currentDestination = AppDestination.CafeDetail(cafeName)
                            },
                            onConvertToVisit = { cafeName, location ->
                                currentDestination = AppDestination.AddEditVisit(
                                    prefilledCafeName = cafeName,
                                    prefilledLocation = location
                                )
                            }
                        )
                    }
                    BottomTab.Wishlist -> {
                        WishlistScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding),
                            onConvertToVisit = { cafeName, location ->
                                currentDestination = AppDestination.AddEditVisit(
                                    prefilledCafeName = cafeName,
                                    prefilledLocation = location
                                )
                            }
                        )
                    }
                    BottomTab.Search -> {
                        SearchFilterScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToVisitDetail = { visitId ->
                                currentDestination = AppDestination.VisitDetail(visitId)
                            }
                        )
                    }
                    BottomTab.Settings -> {
                        SettingsScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding),
                            onResetOnboarding = onResetOnboarding
                        )
                    }
                }
            }
            is AppDestination.CafeDetail -> {
                CafeDetailScreen(
                    cafeName = dest.cafeName,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding),
                    onNavigateBack = {
                        currentDestination = AppDestination.Tab(BottomTab.Directory)
                    },
                    onVisitSelected = { visitId ->
                        currentDestination = AppDestination.VisitDetail(visitId)
                    },
                    onAddVisitForCafe = { cafeName ->
                        currentDestination = AppDestination.AddEditVisit(prefilledCafeName = cafeName)
                    }
                )
            }
            is AppDestination.AddEditVisit -> {
                AddEditVisitScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding),
                    visitId = dest.visitId,
                    prefilledCafeName = dest.prefilledCafeName,
                    prefilledLocation = dest.prefilledLocation,
                    onNavigateBack = {
                        currentDestination = AppDestination.Tab(BottomTab.Dashboard)
                    }
                )
            }
            is AppDestination.VisitDetail -> {
                VisitDetailScreen(
                    visitId = dest.visitId,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding),
                    onNavigateBack = {
                        currentDestination = AppDestination.Tab(BottomTab.Dashboard)
                    },
                    onEditVisit = { visitId ->
                        currentDestination = AppDestination.AddEditVisit(visitId = visitId)
                    }
                )
            }
        }
    }
}
