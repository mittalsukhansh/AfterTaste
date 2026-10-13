package com.aftertaste.ui.vault

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.data.local.entity.Bean
import com.aftertaste.ui.components.CoffeeBeanIcon
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class BeanVaultTab(val label: String, val status: String) {
    ACTIVE("Active Bags", "ACTIVE"),
    FINISHED("Finished", "FINISHED"),
    WISHLIST("Wishlist", "WISHLIST")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeanVaultScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onNavigateToBrewWithBean: (Bean) -> Unit = {}
) {
    val allBeans by viewModel.beans.collectAsState()
    var selectedTab by remember { mutableStateOf(BeanVaultTab.ACTIVE) }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var beanToEdit by remember { mutableStateOf<Bean?>(null) }
    var showScanSimModal by remember { mutableStateOf(false) }

    val filteredBeans = remember(allBeans, selectedTab) {
        allBeans.filter { it.status == selectedTab.status }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    beanToEdit = null
                    showAddEditDialog = true
                },
                containerColor = TerracottaAccent,
                contentColor = EspressoText,
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Bean Bag")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Bag", fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = CoffeeClay
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header: AI Bag Scanner Trigger & Vault Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Bean & Roaster Vault",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ParchmentCream
                    )
                    Text(
                        text = "Monitor resting windows, lot origins & bag inventory",
                        style = MaterialTheme.typography.labelSmall,
                        color = ParchmentCream.copy(alpha = 0.75f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TerracottaAccent,
                    modifier = Modifier.clickable { showScanSimModal = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "AI Bag Scanner",
                            tint = EspressoText,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "AI Scanner",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Segment Control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ParchmentCream.copy(alpha = 0.2f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BeanVaultTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) ParchmentCream else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) EspressoText else ParchmentCream
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bean List
            if (filteredBeans.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCream)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CoffeeBeanIcon(modifier = Modifier.size(44.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No ${selectedTab.label.lowercase()} found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap '+ Add Bag' or use the AI Scanner to catalog whole-bean bags.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EspressoText.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredBeans, key = { it.id }) { bean ->
                        BeanCard(
                            bean = bean,
                            onDeductDose = { doseGrams ->
                                viewModel.deductBeanWeight(bean.id, doseGrams)
                            },
                            onEdit = {
                                beanToEdit = bean
                                showAddEditDialog = true
                            },
                            onDelete = {
                                viewModel.deleteBean(bean.id)
                            },
                            onBrew = {
                                onNavigateToBrewWithBean(bean)
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        }
    }

    // Add / Edit Bean Dialog
    if (showAddEditDialog) {
        AddEditBeanDialog(
            bean = beanToEdit,
            initialStatus = selectedTab.status,
            onDismiss = { showAddEditDialog = false },
            onSave = { updatedBean ->
                viewModel.saveBean(updatedBean)
                showAddEditDialog = false
            }
        )
    }

    // AI OCR Scanner Simulation Modal
    if (showScanSimModal) {
        AiOcrScannerDialog(
            onDismiss = { showScanSimModal = false },
            onBagDetected = { detectedBean ->
                showScanSimModal = false
                beanToEdit = detectedBean
                showAddEditDialog = true
            }
        )
    }
}

@Composable
fun BeanCard(
    bean: Bean,
    onDeductDose: (Float) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onBrew: () -> Unit
) {
    val now = System.currentTimeMillis()
    val daysOffRoast = ((now - bean.roastDate) / (1000 * 60 * 60 * 24L)).coerceAtLeast(0)

    val (restBadgeText, restBadgeColor, restBadgeTextColor) = when {
        daysOffRoast < 5 -> Triple("${daysOffRoast}d • Degassing Window", Color(0xFFF59E0B), EspressoText)
        daysOffRoast <= 21 -> Triple("${daysOffRoast}d Rested • Peak Window", Color(0xFF059669), Color.White)
        else -> Triple("${daysOffRoast}d • Past Peak Window", Color(0xFF78716C), Color.White)
    }

    val progressFraction = if (bean.weightTotal > 0f) {
        (bean.weightRemaining / bean.weightTotal).coerceIn(0f, 1f)
    } else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Roaster & Resting Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bean.roaster.uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.labelSmall,
                    color = TerracottaAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = restBadgeColor
                ) {
                    Text(
                        text = restBadgeText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = restBadgeTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Lot Name & Origin
            Text(
                text = bean.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = EspressoText
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bean.origin,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = EspressoText.copy(alpha = 0.8f)
                )
                Text("•", color = CoffeeOutline)
                Text(
                    text = bean.process,
                    style = MaterialTheme.typography.bodySmall,
                    color = EspressoText.copy(alpha = 0.7f)
                )
                if (!bean.variety.isNullOrBlank()) {
                    Text("•", color = CoffeeOutline)
                    Text(
                        text = bean.variety,
                        style = MaterialTheme.typography.bodySmall,
                        color = EspressoText.copy(alpha = 0.7f)
                    )
                }
            }

            // Tasting Notes Tags
            if (bean.tastingNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    bean.tastingNotes.split(",", ";").take(3).forEach { note ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CoffeeClay.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = note.trim(),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = EspressoText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Inventory Depletion Meter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Inventory Depletion",
                    style = MaterialTheme.typography.labelSmall,
                    color = EspressoText.copy(alpha = 0.6f)
                )
                Text(
                    text = "${bean.weightRemaining.toInt()}g / ${bean.weightTotal.toInt()}g",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progressFraction < 0.2f) Color(0xFFEF4444) else TerracottaAccent,
                trackColor = CoffeeOutline.copy(alpha = 0.25f),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Quick Dose deduction (-15g, -18g) & Edit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (bean.status == "ACTIVE" && bean.weightRemaining > 0f) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = { onDeductDose(15f) },
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EspressoText)
                        ) {
                            Text("-15g Dose", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }

                        OutlinedButton(
                            onClick = { onDeductDose(18f) },
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EspressoText)
                        ) {
                            Text("-18g Dose", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CoffeeClay, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditBeanDialog(
    bean: Bean?,
    initialStatus: String,
    onDismiss: () -> Unit,
    onSave: (Bean) -> Unit
) {
    val context = LocalContext.current
    var roaster by remember { mutableStateOf(bean?.roaster ?: "") }
    var name by remember { mutableStateOf(bean?.name ?: "") }
    var origin by remember { mutableStateOf(bean?.origin ?: "") }
    var process by remember { mutableStateOf(bean?.process ?: "Washed") }
    var variety by remember { mutableStateOf(bean?.variety ?: "") }
    var elevation by remember { mutableStateOf(bean?.elevation ?: "") }
    var roastDate by remember { mutableLongStateOf(bean?.roastDate ?: System.currentTimeMillis()) }
    var weightTotal by remember { mutableStateOf(bean?.weightTotal?.toInt()?.toString() ?: "250") }
    var weightRemaining by remember { mutableStateOf(bean?.weightRemaining?.toInt()?.toString() ?: "250") }
    var tastingNotes by remember { mutableStateOf(bean?.tastingNotes ?: "") }
    var status by remember { mutableStateOf(bean?.status ?: initialStatus) }
    var notes by remember { mutableStateOf(bean?.notes ?: "") }

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (bean == null) "Add Bean Bag" else "Edit Bean Bag",
                fontWeight = FontWeight.Bold,
                color = EspressoText
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = roaster,
                    onValueChange = { roaster = it },
                    label = { Text("Roastery (e.g. Sey, April, Wendelboe)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TerracottaAccent)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Lot Name (e.g. Caballero Geisha)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TerracottaAccent)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = origin,
                        onValueChange = { origin = it },
                        label = { Text("Origin") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TerracottaAccent)
                    )
                    OutlinedTextField(
                        value = process,
                        onValueChange = { process = it },
                        label = { Text("Process") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TerracottaAccent)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weightTotal,
                        onValueChange = { weightTotal = it },
                        label = { Text("Total (g)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = weightRemaining,
                        onValueChange = { weightRemaining = it },
                        label = { Text("Left (g)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                // Roast Date Trigger
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoffeeOutline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val cal = Calendar.getInstance().apply { timeInMillis = roastDate }
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    val newCal = Calendar.getInstance().apply { set(year, month, day) }
                                    roastDate = newCal.timeInMillis
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Roast Date: ${dateFormatter.format(Date(roastDate))}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EspressoText
                        )
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(16.dp))
                    }
                }

                OutlinedTextField(
                    value = tastingNotes,
                    onValueChange = { tastingNotes = it },
                    label = { Text("Tasting Notes (comma-separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (roaster.isNotBlank() && name.isNotBlank()) {
                        val total = weightTotal.toFloatOrNull() ?: 250f
                        val remaining = weightRemaining.toFloatOrNull() ?: total
                        val updated = (bean ?: Bean(roaster = roaster, name = name, origin = origin)).copy(
                            roaster = roaster.trim(),
                            name = name.trim(),
                            origin = origin.trim(),
                            process = process.trim(),
                            variety = variety.trim().ifBlank { null },
                            elevation = elevation.trim().ifBlank { null },
                            roastDate = roastDate,
                            weightTotal = total,
                            weightRemaining = remaining,
                            tastingNotes = tastingNotes.trim(),
                            status = status,
                            notes = notes.trim().ifBlank { null }
                        )
                        onSave(updated)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaAccent, contentColor = EspressoText)
            ) {
                Text("Save to Vault", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = CoffeeClay)
            }
        },
        containerColor = ParchmentCream,
        shape = RoundedCornerShape(22.dp)
    )
}

@Composable
fun AiOcrScannerDialog(
    onDismiss: () -> Unit,
    onBagDetected: (Bean) -> Unit
) {
    val presets = listOf(
        Bean(
            roaster = "Sey Coffee (Brooklyn, NY)",
            name = "El Paraiso • Diego Bermudez",
            origin = "Colombia",
            process = "Thermal Shock Anaerobic",
            variety = "Castillo",
            elevation = "1930m",
            roastDate = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000L,
            weightTotal = 250f,
            weightRemaining = 250f,
            tastingNotes = "Peach Yogurt, Lychee, Candied Strawberry",
            status = "ACTIVE",
            notes = "AI OCR Extracted Lot #0821"
        ),
        Bean(
            roaster = "Tim Wendelboe (Oslo)",
            name = "Nanguala • Organic Washed",
            origin = "Honduras",
            process = "Washed",
            variety = "Catuai & Pacas",
            elevation = "1500m",
            roastDate = System.currentTimeMillis() - 11 * 24 * 60 * 60 * 1000L,
            weightTotal = 250f,
            weightRemaining = 250f,
            tastingNotes = "Red Apple, Brown Sugar, Clean Chocolate",
            status = "ACTIVE",
            notes = "AI OCR Extracted Lot #0414"
        ),
        Bean(
            roaster = "The Barn (Berlin)",
            name = "Buku Abel",
            origin = "Ethiopia",
            process = "Natural",
            variety = "Heirloom",
            elevation = "2100-2300m",
            roastDate = System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L,
            weightTotal = 250f,
            weightRemaining = 250f,
            tastingNotes = "Blueberry, Jasmine, Meyer Lemon",
            status = "ACTIVE",
            notes = "AI OCR Extracted Harvest 2026"
        )
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = TerracottaAccent, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("AI Bag Label Scanner", fontWeight = FontWeight.Bold, color = EspressoText)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Point camera at boutique coffee bag label to automatically extract roaster, origin, process, variety & roast date.",
                    style = MaterialTheme.typography.bodySmall,
                    color = EspressoText.copy(alpha = 0.75f)
                )

                Text(
                    text = "Select a simulated scan to preview extraction:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText
                )

                presets.forEach { preset ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBagDetected(preset) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CoffeeClay.copy(alpha = 0.12f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(preset.roaster, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = EspressoText)
                            Text(preset.name, style = MaterialTheme.typography.bodySmall, color = TerracottaAccent, fontWeight = FontWeight.SemiBold)
                            Text(preset.tastingNotes, style = MaterialTheme.typography.labelSmall, color = EspressoText.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = CoffeeClay)
            }
        },
        containerColor = ParchmentCream,
        shape = RoundedCornerShape(22.dp)
    )
}
