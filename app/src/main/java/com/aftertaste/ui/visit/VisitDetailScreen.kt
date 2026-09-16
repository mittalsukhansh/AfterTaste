package com.aftertaste.ui.visit

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aftertaste.ui.components.CoffeeBeanRatingBar
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.components.EspressoMachineIllustration
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VisitDetailScreen(
    visitId: Long,
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onEditVisit: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    val visitDetailsState by viewModel.getVisitById(visitId).collectAsState(initial = null)
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Coffee Passport Log",
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
                    IconButton(onClick = { onEditVisit(visitId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Visit", tint = ParchmentCream)
                    }
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Visit", tint = ParchmentCream)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoffeeClay
                )
            )
        },
        containerColor = CoffeeClay
    ) { innerPadding ->
        val details = visitDetailsState
        if (details == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading visit details...", color = ParchmentCream)
            }
            return@Scaffold
        }

        val v = details.visit
        val dateFormatter = remember { SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()) }
        val totalSpend = details.items.sumOf { it.itemPrice ?: 0.0 }
        val priceToQualityRatio = if (totalSpend > 0) v.overallRating / totalSpend else 0.0

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Polaroid / Passport Card Main Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ParchmentCream),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Hero Photo / Coffee Illustration
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CoffeeClay.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (details.photos.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(details.photos) { photo ->
                                    AsyncImage(
                                        model = photo.uri,
                                        contentDescription = "Visit Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(260.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                EspressoMachineIllustration(modifier = Modifier.size(120.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "AfterTaste Passport Log",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = EspressoText.copy(alpha = 0.6f)
                                )
                            }
                        }

                        // Passport Stamp Badge Overlay
                        PassportStampBadge(
                            dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(v.visitDate)),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .rotate(12f)
                        )
                    }

                    // 2. Cafe Info Header
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = v.cafeName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText,
                            textAlign = TextAlign.Center
                        )

                        if (v.location.isNotBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = v.location,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = EspressoText.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Text(
                            text = dateFormatter.format(Date(v.visitDate)),
                            style = MaterialTheme.typography.bodySmall,
                            color = EspressoText.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // 3. Overall Rating & Sub-Scores
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CoffeeClay.copy(alpha = 0.08f))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Overall Experience",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )

                        CoffeeBeanRatingBar(
                            rating = v.overallRating,
                            isSelectable = false,
                            beanSize = 32.dp,
                            beanPadding = 6.dp
                        )

                        Text(
                            text = String.format(Locale.getDefault(), "%.1f / 5.0 Beans", v.overallRating),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TerracottaAccent
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Sub-scores breakdown
                        val subScores = listOfNotNull(
                            v.tasteRating?.let { "Taste" to it },
                            v.ambienceRating?.let { "Ambience" to it },
                            v.seatingRating?.let { "Seating" to it },
                            v.wifiRating?.let { "Wifi" to it },
                            v.serviceRating?.let { "Service" to it }
                        )

                        if (subScores.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                subScores.forEach { (label, score) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(label, style = MaterialTheme.typography.bodySmall, color = EspressoText, fontWeight = FontWeight.SemiBold)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            CoffeeBeanRatingBar(rating = score, isSelectable = false, beanSize = 14.dp, beanPadding = 2.dp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(String.format(Locale.getDefault(), "%.1f", score), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = EspressoText)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4. Vibe Tags
                    if (details.tags.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Atmosphere & Vibes",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                details.tags.forEach { tag ->
                                    Surface(
                                        shape = CircleShape,
                                        color = TerracottaAccent.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "#${tag.tagName}",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = EspressoText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 5. Practical Details
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CoffeeClay.copy(alpha = 0.08f))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Practical Details",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )

                        if (!v.wifiPassword.isNullOrBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Wifi, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Wifi: ${v.wifiPassword}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = EspressoText)
                                }

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Wifi Password", v.wifiPassword)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Wifi Password copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy Password", tint = TerracottaAccent, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Outlets: ${if (v.powerOutlets == true) "Available" else "None"}", style = MaterialTheme.typography.bodySmall, color = EspressoText)
                            Text("Seating: ${v.seatingType ?: "N/A"}", style = MaterialTheme.typography.bodySmall, color = EspressoText)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Noise: ${v.noiseLevel ?: "N/A"}", style = MaterialTheme.typography.bodySmall, color = EspressoText)
                            Text("Crowd: ${v.crowdLevel ?: "N/A"}", style = MaterialTheme.typography.bodySmall, color = EspressoText)
                        }

                        if (v.wouldReturn) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TerracottaAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Would visit again!", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TerracottaAccent)
                            }
                        }
                    }

                    // 6. Ordered Items Table & Price-to-Quality Ratio
                    if (details.items.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Ordered Items",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )

                            details.items.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.itemName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = EspressoText
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (item.itemPrice != null) {
                                            Text(
                                                text = String.format(Locale.getDefault(), "$%.2f", item.itemPrice),
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Bold,
                                                color = TerracottaAccent
                                            )
                                        }
                                        if (item.itemRating != null) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            CoffeeBeanRatingBar(
                                                rating = item.itemRating,
                                                isSelectable = false,
                                                beanSize = 14.dp,
                                                beanPadding = 2.dp
                                            )
                                        }
                                    }
                                }
                            }

                            // Spend & Ratio Summary
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = TerracottaAccent.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = String.format(Locale.getDefault(), "Total Spend: $%.2f", totalSpend),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )

                                    Text(
                                        text = String.format(Locale.getDefault(), "Ratio: %.2f pts/$", priceToQualityRatio),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TerracottaAccent
                                    )
                                }
                            }
                        }
                    }

                    // 7. Journal Notes Card
                    if (!v.notes.isNullOrBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CoffeeClay.copy(alpha = 0.08f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "Journal Notes",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = CoffeeClay
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = v.notes,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = EspressoText,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onEditVisit(visitId) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ParchmentCream)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit Log")
                }

                Button(
                    onClick = { showDeleteConfirmDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Delete")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Visit Log?", fontWeight = FontWeight.Bold, color = EspressoText) },
            text = { Text("Are you sure you want to delete this visit log from your Coffee Passport?", color = EspressoText) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteVisit(visitId)
                        showDeleteConfirmDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = EspressoText)
                }
            },
            containerColor = ParchmentCream,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun PassportStampBadge(
    dateStr: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(TerracottaAccent.copy(alpha = 0.9f))
                .border(2.dp, ParchmentCream, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = ParchmentCream,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "AFTERTASTE",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EspressoText,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "PASSPORT",
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText
                )
                Text(
                    text = dateStr,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EspressoText
                )
            }
        }
    }
}
