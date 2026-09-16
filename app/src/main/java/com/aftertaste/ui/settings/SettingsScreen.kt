package com.aftertaste.ui.settings

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aftertaste.ui.components.CoffeeCupIcon
import com.aftertaste.ui.theme.AfterTasteTitleStyle
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.util.BackupManager
import com.aftertaste.util.DataExporter
import com.aftertaste.util.OnboardingPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: CafeViewModel,
    modifier: Modifier = Modifier,
    onResetOnboarding: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val visits by viewModel.visits.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()
    val accountUser by viewModel.userAccount.collectAsState()

    var showRestoreDialog by remember { mutableStateOf(false) }
    var restoreJsonText by remember { mutableStateOf("") }
    var restoreErrorMessage by remember { mutableStateOf<String?>(null) }

    val dateFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    // Create Document SAF Launcher
    val createCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            val csvContent = DataExporter.generateFullCsvExport(visits, wishlist)
            val success = DataExporter.writeStringToUri(context, it, csvContent)
            if (success) {
                Toast.makeText(context, "CSV exported successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to export CSV file.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val createJsonBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            val jsonContent = BackupManager.createBackupJson(visits, wishlist)
            val success = DataExporter.writeStringToUri(context, it, jsonContent)
            if (success) {
                Toast.makeText(context, "JSON Backup saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to save JSON backup.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
            // Google Account & Cloud Sync Section
            Text(
                text = "Google Account & Cloud Backup",
                style = MaterialTheme.typography.titleMedium,
                color = ParchmentCream,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ParchmentCream),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (accountUser.isSignedIn) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(TerracottaAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = EspressoText,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = accountUser.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )
                                    Text(
                                        text = accountUser.email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = EspressoText.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.signOut() }) {
                                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Sign Out", tint = CoffeeClay)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TerracottaAccent.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = TerracottaAccent, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Cloud Sync: Synced at ${dateFormatter.format(Date(accountUser.lastSyncedAt))}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = EspressoText
                                    )
                                }

                                TextButton(onClick = { viewModel.syncCloudData() }) {
                                    Icon(Icons.Default.CloudSync, contentDescription = null, tint = TerracottaAccent, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Sync", color = TerracottaAccent, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = CoffeeClay, modifier = Modifier.size(42.dp))
                            Text(
                                text = "Sign in to backup your coffee visits",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EspressoText
                            )
                            Text(
                                text = "Connect your Google ID to safely store your coffee journal, ratings, and wishlist across devices.",
                                style = MaterialTheme.typography.bodySmall,
                                color = EspressoText.copy(alpha = 0.7f)
                            )

                            Button(
                                onClick = { viewModel.signInWithGoogle() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TerracottaAccent,
                                    contentColor = EspressoText
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sign in with Google", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Data Export & Backup Section
            Text(
                text = "Data Backup & Export",
                style = MaterialTheme.typography.titleMedium,
                color = ParchmentCream,
                fontWeight = FontWeight.Bold
            )

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
                    SettingsOptionRow(
                        icon = Icons.Default.FileDownload,
                        title = "Export Data to CSV",
                        subtitle = "Save visits and wishlist as a spreadsheet CSV file.",
                        onClick = {
                            createCsvLauncher.launch("aftertaste_export_${System.currentTimeMillis()}.csv")
                        }
                    )

                    SettingsOptionRow(
                        icon = Icons.Default.Share,
                        title = "Share CSV via App",
                        subtitle = "Send CSV file via Email, Messaging, or Drive.",
                        onClick = {
                            val csvContent = DataExporter.generateFullCsvExport(visits, wishlist)
                            val shareIntent = DataExporter.createShareIntent(context, csvContent, "AfterTaste Cafe Export")
                            context.startActivity(shareIntent)
                        }
                    )

                    SettingsOptionRow(
                        icon = Icons.Default.Backup,
                        title = "Save JSON Backup",
                        subtitle = "Full JSON backup file containing all visits, items, and wishlist.",
                        onClick = {
                            createJsonBackupLauncher.launch("aftertaste_backup_${System.currentTimeMillis()}.json")
                        }
                    )

                    SettingsOptionRow(
                        icon = Icons.Default.FileUpload,
                        title = "Restore Data from JSON",
                        subtitle = "Import a JSON backup to restore your coffee journal.",
                        onClick = {
                            restoreJsonText = ""
                            restoreErrorMessage = null
                            showRestoreDialog = true
                        }
                    )
                }
            }

            // App Preferences Section
            Text(
                text = "App Preferences",
                style = MaterialTheme.typography.titleMedium,
                color = ParchmentCream,
                fontWeight = FontWeight.Bold
            )

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
                    SettingsOptionRow(
                        icon = Icons.Default.Refresh,
                        title = "Replay Onboarding Tour",
                        subtitle = "Review the welcome screens & coffee passport introduction.",
                        onClick = {
                            OnboardingPreferences.setOnboardingCompleted(context, false)
                            onResetOnboarding()
                        }
                    )
                }
            }

            // About & Stats Summary Section
            Text(
                text = "About AfterTaste",
                style = MaterialTheme.typography.titleMedium,
                color = ParchmentCream,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ParchmentCream),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CoffeeCupIcon(modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "aftertaste v1.0",
                                style = AfterTasteTitleStyle,
                                fontSize = 28.sp,
                                color = EspressoText
                            )
                            Text(
                                text = "Your Artisanal Coffee Passport",
                                style = MaterialTheme.typography.bodySmall,
                                color = TerracottaAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Logged Visit Records:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EspressoText.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${visits.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Wishlist Cafes:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EspressoText.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${wishlist.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                    }
                }
            }
        }
    }

    // Restore JSON Backup Dialog
    if (showRestoreDialog) {
        AlertDialog(
            onDismissRequest = { showRestoreDialog = false },
            title = {
                Text(text = "Restore Data from JSON", color = EspressoText, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Paste your JSON backup data content below to restore visit logs & wishlist cafes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = EspressoText.copy(alpha = 0.8f)
                    )

                    OutlinedTextField(
                        value = restoreJsonText,
                        onValueChange = {
                            restoreJsonText = it
                            restoreErrorMessage = null
                        },
                        placeholder = { Text("{\"version\": 1, \"visits\": [...]}") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TerracottaAccent,
                            unfocusedBorderColor = CoffeeOutline
                        )
                    )

                    restoreErrorMessage?.let { err ->
                        Text(
                            text = err,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedData = BackupManager.parseBackupJson(restoreJsonText)
                        if (parsedData != null) {
                            coroutineScope.launch {
                                for (d in parsedData.visits) {
                                    viewModel.saveVisit(d.visit, d.items, d.tags, d.photos)
                                }
                                for (w in parsedData.wishlist) {
                                    viewModel.addToWishlist(w.cafeName, w.location, w.notes)
                                }
                                showRestoreDialog = false
                                Toast.makeText(context, "Data restored successfully!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            restoreErrorMessage = "Invalid JSON backup format. Please check the text and try again."
                        }
                    },
                    enabled = restoreJsonText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TerracottaAccent,
                        contentColor = EspressoText
                    )
                ) {
                    Text("Restore Now", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreDialog = false }) {
                    Text("Cancel", color = CoffeeClay)
                }
            },
            containerColor = ParchmentCream,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun SettingsOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CoffeeClay.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CoffeeClay,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EspressoText
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = EspressoText.copy(alpha = 0.7f)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = CoffeeClay,
            modifier = Modifier.size(20.dp)
        )
    }
}
