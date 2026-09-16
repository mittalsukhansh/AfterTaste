package com.aftertaste.ui.cafe

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aftertaste.data.local.entity.CafeVisitWithDetails
import com.aftertaste.ui.components.CoffeeBeanRatingBar
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeDetailScreen(
    cafeName: String,
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onVisitSelected: (Long) -> Unit = {},
    onAddVisitForCafe: (String) -> Unit = {},
) {
    val visits by viewModel.getVisitsForCafe(cafeName).collectAsState(initial = emptyList())
    val isWishlist by viewModel.isWishlistCafe(cafeName).collectAsState(initial = false)

    // Derived statistics
    val visitCount = visits.size
    val avgOverallRating = if (visits.isNotEmpty()) visits.map { it.visit.overallRating }.average().toFloat() else 0f
    val location = visits.firstOrNull { it.visit.location.isNotBlank() }?.visit?.location ?: ""
    val latestWifiPassword = visits.mapNotNull { it.visit.wifiPassword }.firstOrNull { it.isNotBlank() }
    val powerOutletsAvailable = visits.any { it.visit.powerOutlets == true }

    // Sub-scores averages
    val avgTaste = visits.mapNotNull { it.visit.tasteRating }.let { if (it.isNotEmpty()) it.average().toFloat() else 0f }
    val avgAmbience = visits.mapNotNull { it.visit.ambienceRating }.let { if (it.isNotEmpty()) it.average().toFloat() else 0f }
    val avgSeating = visits.mapNotNull { it.visit.seatingRating }.let { if (it.isNotEmpty()) it.average().toFloat() else 0f }
    val avgWifi = visits.mapNotNull { it.visit.wifiRating }.let { if (it.isNotEmpty()) it.average().toFloat() else 0f }
    val avgService = visits.mapNotNull { it.visit.serviceRating }.let { if (it.isNotEmpty()) it.average().toFloat() else 0f }

    // All photos aggregated across visits
    val allPhotos = remember(visits) { visits.flatMap { it.photos } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = cafeName,
                        style = MaterialTheme.typography.titleLarge,
                        color = ParchmentCream,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ParchmentCream
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isWishlist) {
                                viewModel.removeFromWishlistByName(cafeName)
                            } else {
                                viewModel.addToWishlist(cafeName, location)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isWishlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Wishlist",
                            tint = TerracottaAccent
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ParchmentCream)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = cafeName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )

                    if (location.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = EspressoText.copy(alpha = 0.8f)
                            )
                        }
                    }

                    if (visitCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CoffeeBeanRatingBar(
                                rating = avgOverallRating,
                                isSelectable = false,
                                beanSize = 28.dp,
                                beanPadding = 4.dp
                            )
                            Text(
                                text = String.format(Locale.getDefault(), "%.1f", avgOverallRating),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaAccent
                            )
                        }
                    }

                    // Key Practical Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatBadge(
                            label = "Visits",
                            value = visitCount.toString()
                        )

                        StatBadge(
                            label = "Wifi Pass",
                            value = latestWifiPassword ?: "None",
                            icon = Icons.Default.Wifi
                        )

                        StatBadge(
                            label = "Outlets",
                            value = if (powerOutletsAvailable) "Yes" else "No",
                            icon = Icons.Default.FlashOn
                        )
                    }

                    Button(
                        onClick = { onAddVisitForCafe(cafeName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TerracottaAccent,
                            contentColor = EspressoText
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Log Visit to $cafeName", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Sub-scores Averages Breakdown Card
            if (visitCount > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCream)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Average Sub-Scores",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )

                        SubScoreDisplayRow("Taste", avgTaste)
                        SubScoreDisplayRow("Ambience", avgAmbience)
                        SubScoreDisplayRow("Seating", avgSeating)
                        SubScoreDisplayRow("Wifi", avgWifi)
                        SubScoreDisplayRow("Service", avgService)
                    }
                }

                // Rating Trend Chart Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCream)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Rating Trend Over Time",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )

                        RatingTrendChart(
                            visits = visits,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                }
            }

            // Photo Gallery Section
            if (allPhotos.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCream)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Cafe Photo Gallery (${allPhotos.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(allPhotos) { photo ->
                                AsyncImage(
                                    model = photo.uri,
                                    contentDescription = "Cafe Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                )
                            }
                        }
                    }
                }
            }

            // Past Visits List
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ParchmentCream)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Past Visit Logs (${visits.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )

                    if (visits.isEmpty()) {
                        Text(
                            text = "No visit logs for $cafeName yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EspressoText.copy(alpha = 0.6f)
                        )
                    } else {
                        val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
                        visits.forEach { details ->
                            val v = details.visit
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(CoffeeClay.copy(alpha = 0.08f))
                                    .clickable { onVisitSelected(v.id) }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = dateFormatter.format(Date(v.visitDate)),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        CoffeeBeanRatingBar(
                                            rating = v.overallRating,
                                            isSelectable = false,
                                            beanSize = 16.dp,
                                            beanPadding = 2.dp
                                        )
                                        Text(
                                            text = String.format(Locale.getDefault(), "%.1f", v.overallRating),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = TerracottaAccent
                                        )
                                    }
                                    if (!v.notes.isNullOrBlank()) {
                                        Text(
                                            text = v.notes,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = EspressoText.copy(alpha = 0.7f),
                                            maxLines = 1
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "View Details",
                                    tint = CoffeeClay
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatBadge(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CoffeeClay.copy(alpha = 0.12f),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = EspressoText.copy(alpha = 0.6f)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = EspressoText,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SubScoreDisplayRow(label: String, score: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = EspressoText,
            modifier = Modifier.width(90.dp)
        )
        CoffeeBeanRatingBar(
            rating = score,
            isSelectable = false,
            beanSize = 18.dp,
            beanPadding = 3.dp
        )
        Text(
            text = String.format(Locale.getDefault(), "%.1f", score),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = EspressoText.copy(alpha = 0.7f),
            modifier = Modifier.width(32.dp)
        )
    }
}

@Composable
fun RatingTrendChart(
    visits: List<CafeVisitWithDetails>,
    modifier: Modifier = Modifier,
    lineColor: Color = TerracottaAccent,
    pointColor: Color = CoffeeClay,
    gridColor: Color = CoffeeOutline.copy(alpha = 0.3f)
) {
    // Sort visits chronologically
    val sortedVisits = remember(visits) { visits.sortedBy { it.visit.visitDate } }

    if (sortedVisits.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "Log visits to view rating trends!",
                style = MaterialTheme.typography.bodyMedium,
                color = EspressoText.copy(alpha = 0.6f)
            )
        }
        return
    }

    val density = LocalDensity.current
    val textPaint = remember(density) {
        android.graphics.Paint().apply {
            color = "#2C1A11".toColorInt()
            textSize = density.run { 10.sp.toPx() }
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }

    val axisTextPaint = remember(density) {
        android.graphics.Paint().apply {
            color = "#986252".toColorInt()
            textSize = density.run { 10.sp.toPx() }
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.RIGHT
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val leftMargin = 36.dp.toPx()
        val bottomMargin = 28.dp.toPx()
        val topMargin = 20.dp.toPx()
        val rightMargin = 20.dp.toPx()

        val chartW = w - leftMargin - rightMargin
        val chartH = h - topMargin - bottomMargin

        // Draw horizontal grid lines (for ratings 1..5)
        for (r in 1..5) {
            val y = topMargin + chartH - (r / 5.0f * chartH)
            drawLine(
                color = gridColor,
                start = Offset(leftMargin, y),
                end = Offset(w - rightMargin, y),
                strokeWidth = 1.dp.toPx()
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${r}★",
                leftMargin - 8.dp.toPx(),
                y + 4.dp.toPx(),
                axisTextPaint
            )
        }

        val count = sortedVisits.size
        val points = mutableListOf<Offset>()

        for (i in 0 until count) {
            val x = if (count == 1) {
                leftMargin + (chartW / 2f)
            } else {
                leftMargin + (i.toFloat() / (count - 1)) * chartW
            }
            val r = sortedVisits[i].visit.overallRating.coerceIn(0f, 5f)
            val y = topMargin + chartH - (r / 5.0f * chartH)
            points.add(Offset(x, y))
        }

        // Draw line connecting points
        if (points.size > 1) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Draw data points & values
        points.forEachIndexed { index, pt ->
            drawCircle(
                color = pointColor,
                radius = 5.dp.toPx(),
                center = pt
            )
            drawCircle(
                color = ParchmentCream,
                radius = 2.5.dp.toPx(),
                center = pt
            )

            val ratingVal = sortedVisits[index].visit.overallRating
            drawContext.canvas.nativeCanvas.drawText(
                String.format(Locale.getDefault(), "%.1f", ratingVal),
                pt.x,
                pt.y - 8.dp.toPx(),
                textPaint
            )
        }
    }
}
