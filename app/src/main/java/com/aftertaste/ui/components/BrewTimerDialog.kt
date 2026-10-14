package com.aftertaste.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.util.BrewGuideHelper
import com.aftertaste.util.BrewMethod
import com.aftertaste.util.BrewStage
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrewTimerDialog(
    onDismiss: () -> Unit,
    onLogBrew: (methodName: String, doseGrams: Float, waterGrams: Float, brewSeconds: Int) -> Unit = { _, _, _, _ -> }
) {
    val supportedMethods = remember {
        listOf(
            BrewMethod.V60_POUR_OVER,
            BrewMethod.AEROPRESS,
            BrewMethod.FRENCH_PRESS,
            BrewMethod.MOKA_POT
        )
    }

    var selectedMethod by remember { mutableStateOf(BrewMethod.V60_POUR_OVER) }
    var doseGrams by remember { mutableIntStateOf(15) }

    val stages: List<BrewStage> = remember(selectedMethod, doseGrams) {
        BrewGuideHelper.getStagesForMethod(selectedMethod, doseGrams.toFloat())
    }
    val totalWater: Int = remember(selectedMethod, doseGrams) {
        BrewGuideHelper.calculateWaterYield(doseGrams.toFloat(), selectedMethod)
    }

    var isRunning by remember { mutableStateOf(false) }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var secondsRemainingInStep by remember(stages, currentStepIndex) {
        val stageSecs = stages.getOrNull(currentStepIndex)?.durationSeconds ?: 30
        mutableIntStateOf(stageSecs)
    }
    var totalElapsedSeconds by remember { mutableIntStateOf(0) }

    // Active Countdown Timer Loop
    LaunchedEffect(isRunning, secondsRemainingInStep) {
        if (isRunning && secondsRemainingInStep > 0) {
            delay(1000L)
            secondsRemainingInStep -= 1
            totalElapsedSeconds += 1
        } else if (isRunning && secondsRemainingInStep == 0) {
            if (currentStepIndex < stages.size - 1) {
                currentStepIndex += 1
                secondsRemainingInStep = stages[currentStepIndex].durationSeconds
            } else {
                isRunning = false
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.96f),
        shape = RoundedCornerShape(24.dp),
        containerColor = ParchmentCream,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = TerracottaAccent.copy(alpha = 0.2f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = TerracottaAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Smart Pour Timer",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        Text(
                            text = "Interactive Dial-In",
                            style = MaterialTheme.typography.labelSmall,
                            color = EspressoText.copy(alpha = 0.6f)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = EspressoText)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Method Selector Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(supportedMethods) { method ->
                        val isSelected = method == selectedMethod
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) TerracottaAccent else CoffeeClay.copy(alpha = 0.12f),
                            modifier = Modifier.clickable {
                                selectedMethod = method
                                currentStepIndex = 0
                                isRunning = false
                                secondsRemainingInStep = BrewGuideHelper.getStagesForMethod(method, doseGrams.toFloat()).first().durationSeconds
                                totalElapsedSeconds = 0
                            }
                        ) {
                            Text(
                                text = method.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) EspressoText else EspressoText.copy(alpha = 0.8f),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Ratio & Dose Summary Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CoffeeClay.copy(alpha = 0.08f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Coffee Dose: ${doseGrams}g",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = EspressoText
                        )
                        Text(
                            text = "Ratio 1:${String.format(Locale.getDefault(), "%.1f", selectedMethod.waterRatio)} • Water: ${totalWater}g @ ${selectedMethod.idealTempCelsius}°C",
                            style = MaterialTheme.typography.labelSmall,
                            color = EspressoText.copy(alpha = 0.7f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilledTonalButton(
                            onClick = { if (doseGrams > 10) doseGrams -= 1 },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("-1g", style = MaterialTheme.typography.labelSmall)
                        }
                        FilledTonalButton(
                            onClick = { if (doseGrams < 40) doseGrams += 1 },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("+1g", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                // Main Countdown & Stage Card
                val currentStage = stages.getOrNull(currentStepIndex)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = EspressoText),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentStage?.stageName?.uppercase(Locale.getDefault()) ?: "READY",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TerracottaAccent,
                            letterSpacing = 1.5.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Large Digital Clock
                        val mins = secondsRemainingInStep / 60
                        val secs = secondsRemainingInStep % 60
                        Text(
                            text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = ParchmentCream
                        )

                        Text(
                            text = currentStage?.instruction ?: "Tap Start to begin pour",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentCream.copy(alpha = 0.85f),
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress Bar across stages
                        val progress = if (stages.isNotEmpty()) {
                            (currentStepIndex.toFloat() + (1f - secondsRemainingInStep.toFloat() / (currentStage?.durationSeconds ?: 1).toFloat())) / stages.size.toFloat()
                        } else 0f

                        LinearProgressIndicator(
                            progress = { progress.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = TerracottaAccent,
                            trackColor = ParchmentCream.copy(alpha = 0.2f),
                            strokeCap = StrokeCap.Round
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Stage ${currentStepIndex + 1} of ${stages.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ParchmentCream.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Total: ${totalElapsedSeconds / 60}m ${totalElapsedSeconds % 60}s",
                                style = MaterialTheme.typography.labelSmall,
                                color = ParchmentCream.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Controls Row: Reset, Play/Pause, Skip Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            isRunning = false
                            currentStepIndex = 0
                            secondsRemainingInStep = stages.first().durationSeconds
                            totalElapsedSeconds = 0
                        }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = EspressoText)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { isRunning = !isRunning },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TerracottaAccent,
                            contentColor = EspressoText
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pause" else "Start"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRunning) "Pause" else "Start Brew",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = {
                            if (currentStepIndex < stages.size - 1) {
                                currentStepIndex += 1
                                secondsRemainingInStep = stages[currentStepIndex].durationSeconds
                            }
                        },
                        enabled = currentStepIndex < stages.size - 1
                    ) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Next Step", tint = EspressoText)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLogBrew(
                        selectedMethod.displayName,
                        doseGrams.toFloat(),
                        totalWater.toFloat(),
                        totalElapsedSeconds
                    )
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EspressoText,
                    contentColor = ParchmentCream
                )
            ) {
                Text("Log to Vault", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close", color = EspressoText)
            }
        }
    )
}
