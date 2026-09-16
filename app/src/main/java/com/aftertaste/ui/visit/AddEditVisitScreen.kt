package com.aftertaste.ui.visit

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aftertaste.data.local.entity.CafeVisit
import com.aftertaste.data.local.entity.OrderedItem
import com.aftertaste.data.local.entity.Photo
import com.aftertaste.data.local.entity.Tag
import com.aftertaste.ui.components.CoffeeBeanRatingBar
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

// Preset Vibe Tags
private val PRESET_TAGS = listOf(
    "WorkFriendly",
    "Cozy",
    "Quiet",
    "OatMilk",
    "GoodOutlets",
    "PetFriendly",
    "Artisanal",
    "GreatEspresso",
    "Spacious",
    "FastWifi"
)

private val ROAST_PROFILES = listOf("Light Roast", "Medium Roast", "Dark Roast", "Omni Roast")
private val BREW_METHODS = listOf("Espresso", "Pour Over", "Cold Brew", "Aeropress", "French Press")
private val FOOD_SUGGESTIONS = listOf("Pizza", "Almond Croissant", "Cheesecake", "Avocado Toast", "Bagel", "Sandwich")

private val SEATING_TYPES = listOf("Couch", "Bar", "Outdoor", "Mixed")
private val NOISE_LEVELS = listOf("Low", "Medium", "High")
private val CROWD_LEVELS = listOf("Empty", "Moderate", "Packed")

data class TempItem(
    val name: String,
    val price: Double,
    val rating: Float
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditVisitScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    visitId: Long? = null,
    prefilledCafeName: String? = null,
    prefilledLocation: String? = null,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    // State fields
    var cafeName by remember { mutableStateOf(prefilledCafeName ?: "") }
    var location by remember { mutableStateOf(prefilledLocation ?: "") }
    var visitDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var overallRating by remember { mutableFloatStateOf(4.0f) }

    // Coffee Beans & Roast
    var beanOrigin by remember { mutableStateOf("") }
    var selectedRoastProfile by remember { mutableStateOf("Medium Roast") }
    var selectedBrewMethod by remember { mutableStateOf("Espresso") }

    // Sub-scores
    var tasteRating by remember { mutableFloatStateOf(4.0f) }
    var ambienceRating by remember { mutableFloatStateOf(4.0f) }
    var seatingRating by remember { mutableFloatStateOf(4.0f) }
    var wifiRating by remember { mutableFloatStateOf(4.0f) }
    var serviceRating by remember { mutableFloatStateOf(4.0f) }

    // Tags
    val selectedTags = remember { mutableStateListOf<String>() }
    var customTagInput by remember { mutableStateOf("") }

    // Practical details
    var wifiNetwork by remember { mutableStateOf("") }
    var wifiPassword by remember { mutableStateOf("") }
    var powerOutlets by remember { mutableStateOf(true) }
    var seatingType by remember { mutableStateOf("Mixed") }
    var noiseLevel by remember { mutableStateOf("Medium") }
    var crowdLevel by remember { mutableStateOf("Moderate") }
    var wouldReturn by remember { mutableStateOf(true) }

    // Ordered items & Photos
    val orderedItems = remember { mutableStateListOf<TempItem>() }
    val photoUris = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }

    // Item Dialog state
    var showAddItemDialog by remember { mutableStateOf(false) }

    // If editing existing visit, load existing data
    if (visitId != null && visitId > 0L) {
        val existingVisitState by viewModel.getVisitById(visitId).collectAsState(initial = null)
        LaunchedEffect(existingVisitState) {
            existingVisitState?.let { details ->
                val v = details.visit
                cafeName = v.cafeName
                location = v.location
                visitDate = v.visitDate
                overallRating = v.overallRating
                tasteRating = v.tasteRating ?: 4.0f
                ambienceRating = v.ambienceRating ?: 4.0f
                seatingRating = v.seatingRating ?: 4.0f
                wifiRating = v.wifiRating ?: 4.0f
                serviceRating = v.serviceRating ?: 4.0f
                powerOutlets = v.powerOutlets ?: true
                wifiPassword = v.wifiPassword ?: ""
                seatingType = v.seatingType ?: "Mixed"
                noiseLevel = v.noiseLevel ?: "Medium"
                crowdLevel = v.crowdLevel ?: "Moderate"
                wouldReturn = v.wouldReturn
                notes = v.notes ?: ""

                selectedTags.clear()
                selectedTags.addAll(details.tags.map { it.tagName })

                orderedItems.clear()
                orderedItems.addAll(details.items.map {
                    TempItem(it.itemName, it.itemPrice ?: 0.0, it.itemRating ?: 4.0f)
                })

                photoUris.clear()
                photoUris.addAll(details.photos.map { it.uri })
            }
        }
    }

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUris.add(it.toString())
        }
    }

    // Date picker dialog
    val calendar = Calendar.getInstance().apply { timeInMillis = visitDate }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            visitDate = selectedCal.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Calculated fields
    val totalSpend = orderedItems.sumOf { it.price }
    val priceToQualityRatio = if (totalSpend > 0) overallRating / totalSpend else 0.0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (visitId != null && visitId > 0L) "Edit Visit Log" else "Log Cafe Visit",
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
            // 1. Cafe & Location Inputs Card
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
                        text = "Cafe Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )

                    OutlinedTextField(
                        value = cafeName,
                        onValueChange = { cafeName = it },
                        label = { Text("Cafe Name *") },
                        leadingIcon = { Icon(Icons.Default.Store, contentDescription = null, tint = CoffeeClay) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = customOutlinedTextFieldColors()
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location / Neighborhood") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = CoffeeClay) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = customOutlinedTextFieldColors()
                    )

                    // Visit Date Picker
                    val dateFormatter = remember { SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault()) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CoffeeClay.copy(alpha = 0.08f))
                            .clickable { datePickerDialog.show() }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = CoffeeClay)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Visit Date",
                                style = MaterialTheme.typography.labelMedium,
                                color = EspressoText.copy(alpha = 0.6f)
                            )
                            Text(
                                text = dateFormatter.format(Date(visitDate)),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = EspressoText
                            )
                        }
                    }
                }
            }

            // 2. Coffee Beans & Brew Card
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalCafe, contentDescription = null, tint = TerracottaAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Coffee Beans & Brew Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                    }

                    OutlinedTextField(
                        value = beanOrigin,
                        onValueChange = { beanOrigin = it },
                        label = { Text("Coffee Bean Variety / Origin (e.g. Ethiopia Yirgacheffe)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = customOutlinedTextFieldColors()
                    )

                    ChoiceChipsSection(
                        title = "Roast Level",
                        options = ROAST_PROFILES,
                        selectedOption = selectedRoastProfile,
                        onOptionSelected = { selectedRoastProfile = it }
                    )

                    ChoiceChipsSection(
                        title = "Brewing Method",
                        options = BREW_METHODS,
                        selectedOption = selectedBrewMethod,
                        onOptionSelected = { selectedBrewMethod = it }
                    )
                }
            }

            // 3. Overall Rating & Sub-scores Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ParchmentCream)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Overall Experience Rating",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )

                    CoffeeBeanRatingBar(
                        rating = overallRating,
                        onRatingChanged = { overallRating = it },
                        beanSize = 38.dp,
                        beanPadding = 8.dp
                    )

                    Text(
                        text = String.format(Locale.getDefault(), "%.1f / 5.0 Beans", overallRating),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaAccent
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Detailed Sub-Scores",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    SubScoreRow(label = "Taste", rating = tasteRating, onRatingChanged = { tasteRating = it })
                    SubScoreRow(label = "Ambience", rating = ambienceRating, onRatingChanged = { ambienceRating = it })
                    SubScoreRow(label = "Seating", rating = seatingRating, onRatingChanged = { seatingRating = it })
                    SubScoreRow(label = "Wifi", rating = wifiRating, onRatingChanged = { wifiRating = it })
                    SubScoreRow(label = "Service", rating = serviceRating, onRatingChanged = { serviceRating = it })
                }
            }

            // 4. Ordered Items & Food Consumed Card (e.g. Pizza, Croissant, Pastry)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Fastfood, contentDescription = null, tint = TerracottaAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Food & Items Consumed",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )
                        }

                        TextButton(onClick = { showAddItemDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = TerracottaAccent)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Item", color = TerracottaAccent, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Quick Food Suggestions
                    Text(
                        text = "Quick Food Suggestions:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = EspressoText.copy(alpha = 0.7f)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FOOD_SUGGESTIONS.forEach { foodName ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CoffeeClay.copy(alpha = 0.1f),
                                modifier = Modifier.clickable {
                                    orderedItems.add(TempItem(foodName, 8.0, 4.5f))
                                }
                            ) {
                                Text(
                                    text = "+ $foodName",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EspressoText,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (orderedItems.isEmpty()) {
                        Text(
                            text = "No food or drink items recorded yet. Tap '+ Add Item' or a food suggestion above (e.g. Pizza, Croissant) to record what you consumed.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EspressoText.copy(alpha = 0.6f)
                        )
                    } else {
                        orderedItems.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CoffeeClay.copy(alpha = 0.08f))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = String.format(Locale.getDefault(), "$%.2f", item.price),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TerracottaAccent
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        CoffeeBeanRatingBar(
                                            rating = item.rating,
                                            isSelectable = false,
                                            beanSize = 16.dp,
                                            beanPadding = 2.dp
                                        )
                                    }
                                }

                                IconButton(onClick = { orderedItems.removeAt(index) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove Item",
                                        tint = CoffeeClay
                                    )
                                }
                            }
                        }

                        // Spend & Ratio Summary Box
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = CoffeeClay.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Total Spend",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = EspressoText.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "$%.2f", totalSpend),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Price-to-Quality Ratio",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = EspressoText.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.2f pts/$", priceToQualityRatio),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TerracottaAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Vibe Tags Card
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
                        text = "Vibe & Atmosphere Tags",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val allAvailableTags = (PRESET_TAGS + selectedTags).distinct()
                        allAvailableTags.forEach { tagName ->
                            val isSelected = selectedTags.contains(tagName)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) selectedTags.remove(tagName) else selectedTags.add(tagName)
                                },
                                label = { Text("#$tagName") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TerracottaAccent,
                                    selectedLabelColor = EspressoText,
                                    containerColor = CoffeeClay.copy(alpha = 0.1f),
                                    labelColor = EspressoText
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customTagInput,
                            onValueChange = { customTagInput = it },
                            label = { Text("Add custom tag") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = customOutlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                val trimmed = customTagInput.trim().replace("#", "")
                                if (trimmed.isNotEmpty() && !selectedTags.contains(trimmed)) {
                                    selectedTags.add(trimmed)
                                    customTagInput = ""
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(TerracottaAccent)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Tag", tint = EspressoText)
                        }
                    }
                }
            }

            // 6. Practical Details Card
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
                        text = "Practical Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = wifiNetwork,
                            onValueChange = { wifiNetwork = it },
                            label = { Text("Wifi Name") },
                            leadingIcon = { Icon(Icons.Default.Wifi, contentDescription = null, tint = CoffeeClay) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = customOutlinedTextFieldColors()
                        )
                        OutlinedTextField(
                            value = wifiPassword,
                            onValueChange = { wifiPassword = it },
                            label = { Text("Wifi Password") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = customOutlinedTextFieldColors()
                        )
                    }

                    // Power Outlets Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Power Outlets Available",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = EspressoText
                        )
                        Switch(
                            checked = powerOutlets,
                            onCheckedChange = { powerOutlets = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ParchmentCream,
                                checkedTrackColor = TerracottaAccent
                            )
                        )
                    }

                    // Choice Chips: Seating Type
                    ChoiceChipsSection(
                        title = "Seating Type",
                        options = SEATING_TYPES,
                        selectedOption = seatingType,
                        onOptionSelected = { seatingType = it }
                    )

                    // Choice Chips: Noise Level
                    ChoiceChipsSection(
                        title = "Noise Level",
                        options = NOISE_LEVELS,
                        selectedOption = noiseLevel,
                        onOptionSelected = { noiseLevel = it }
                    )

                    // Choice Chips: Crowd Level
                    ChoiceChipsSection(
                        title = "Crowd Level",
                        options = CROWD_LEVELS,
                        selectedOption = crowdLevel,
                        onOptionSelected = { crowdLevel = it }
                    )

                    // Would Return Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Would Return to Cafe?",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = EspressoText
                        )
                        Switch(
                            checked = wouldReturn,
                            onCheckedChange = { wouldReturn = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ParchmentCream,
                                checkedTrackColor = TerracottaAccent
                            )
                        )
                    }
                }
            }

            // 7. Photo Attachments Card
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Photo Gallery",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )

                        TextButton(onClick = { photoPickerLauncher.launch("image/*") }) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = TerracottaAccent)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Attach Photo", color = TerracottaAccent, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (photoUris.isEmpty()) {
                        Text(
                            text = "Attach photos of your coffee, pastry, or cafe view.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EspressoText.copy(alpha = 0.6f)
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(photoUris) { uriStr ->
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    AsyncImage(
                                        model = uriStr,
                                        contentDescription = "Visit Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    IconButton(
                                        onClick = { photoUris.remove(uriStr) },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(24.dp)
                                            .background(EspressoText.copy(alpha = 0.6f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove photo",
                                            tint = ParchmentCream,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 8. Journal Notes Card
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
                        text = "Coffee Tasting & Journal Notes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EspressoText
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("Write tasting notes, barista recommendation, seating vibes...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        maxLines = 6,
                        colors = customOutlinedTextFieldColors()
                    )
                }
            }

            // CTA Button
            Button(
                onClick = {
                    if (cafeName.isBlank()) return@Button

                    val fullNotes = buildString {
                        if (beanOrigin.isNotBlank()) append("Beans: $beanOrigin | ")
                        append("Roast: $selectedRoastProfile | Brew: $selectedBrewMethod")
                        if (notes.isNotBlank()) append("\n$notes")
                    }

                    val visit = CafeVisit(
                        id = visitId ?: 0L,
                        cafeName = cafeName.trim(),
                        location = location.trim(),
                        visitDate = visitDate,
                        pricePaid = totalSpend,
                        overallRating = overallRating,
                        tasteRating = tasteRating,
                        ambienceRating = ambienceRating,
                        seatingRating = seatingRating,
                        wifiRating = wifiRating,
                        serviceRating = serviceRating,
                        noiseLevel = noiseLevel,
                        wouldReturn = wouldReturn,
                        notes = fullNotes.ifBlank { null },
                        powerOutlets = powerOutlets,
                        crowdLevel = crowdLevel,
                        wifiPassword = wifiPassword.ifBlank { null },
                        seatingType = seatingType
                    )

                    val items = orderedItems.map {
                        OrderedItem(
                            visitId = visitId ?: 0L,
                            itemName = it.name,
                            itemPrice = it.price,
                            itemRating = it.rating
                        )
                    }

                    val tags = (selectedTags + listOf(selectedRoastProfile, selectedBrewMethod)).distinct().map {
                        Tag(visitId = visitId ?: 0L, tagName = it)
                    }

                    val photos = photoUris.map {
                        Photo(visitId = visitId ?: 0L, uri = it)
                    }

                    viewModel.saveVisit(visit, items, tags, photos) {
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TerracottaAccent,
                    contentColor = EspressoText
                ),
                enabled = cafeName.isNotBlank()
            ) {
                Text(
                    text = "Save to Coffee Passport",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add Item Dialog
    if (showAddItemDialog) {
        AddItemDialog(
            onDismiss = { showAddItemDialog = false },
            onItemAdded = { name, price, rating ->
                orderedItems.add(TempItem(name, price, rating))
                showAddItemDialog = false
            }
        )
    }
}

@Composable
private fun SubScoreRow(
    label: String,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = EspressoText,
            modifier = Modifier.width(90.dp)
        )
        CoffeeBeanRatingBar(
            rating = rating,
            onRatingChanged = onRatingChanged,
            beanSize = 22.dp,
            beanPadding = 3.dp
        )
        Text(
            text = String.format(Locale.getDefault(), "%.1f", rating),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = EspressoText.copy(alpha = 0.7f),
            modifier = Modifier.width(32.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChoiceChipsSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = EspressoText
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                FilterChip(
                    selected = isSelected,
                    onClick = { onOptionSelected(option) },
                    label = { Text(option) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CoffeeClay,
                        selectedLabelColor = ParchmentCream,
                        containerColor = CoffeeClay.copy(alpha = 0.08f),
                        labelColor = EspressoText
                    )
                )
            }
        }
    }
}

@Composable
private fun AddItemDialog(
    onDismiss: () -> Unit,
    onItemAdded: (String, Double, Float) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemPriceText by remember { mutableStateOf("") }
    var itemRating by remember { mutableFloatStateOf(4.5f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Item (Food / Drink)",
                fontWeight = FontWeight.Bold,
                color = EspressoText
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name (e.g. Pizza, Cortado, Croissant)") },
                    singleLine = true,
                    colors = customOutlinedTextFieldColors()
                )

                OutlinedTextField(
                    value = itemPriceText,
                    onValueChange = { itemPriceText = it },
                    label = { Text("Price ($)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = customOutlinedTextFieldColors()
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Item Rating",
                        style = MaterialTheme.typography.labelMedium,
                        color = EspressoText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CoffeeBeanRatingBar(
                        rating = itemRating,
                        onRatingChanged = { itemRating = it },
                        beanSize = 24.dp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = itemPriceText.toDoubleOrNull() ?: 0.0
                    if (itemName.isNotBlank()) {
                        onItemAdded(itemName.trim(), price, itemRating)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = TerracottaAccent,
                    contentColor = EspressoText
                ),
                enabled = itemName.isNotBlank()
            ) {
                Text("Add Item")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = EspressoText)
            }
        },
        containerColor = ParchmentCream,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun customOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = TerracottaAccent,
    unfocusedBorderColor = CoffeeOutline,
    focusedLabelColor = TerracottaAccent,
    unfocusedLabelColor = EspressoText.copy(alpha = 0.6f),
    focusedTextColor = EspressoText,
    unfocusedTextColor = EspressoText
)
