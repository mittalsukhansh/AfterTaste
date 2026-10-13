package com.aftertaste.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.util.RadarMetrics
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 5-Axis Flavor Equilibrium Radar (Acidity, Sweetness, Body, Clarity, Bitterness).
 * Renders a polygonal sensory web with animated data polygon fill.
 */
@Composable
fun FlavorRadarChart(
    metrics: RadarMetrics,
    modifier: Modifier = Modifier,
    fillColor: Color = TerracottaAccent.copy(alpha = 0.38f),
    lineColor: Color = TerracottaAccent,
    webColor: Color = CoffeeOutline.copy(alpha = 0.45f)
) {
    // Smooth animated scaling of metrics
    val animAcidity by animateFloatAsState(targetValue = metrics.acidity / 100f, animationSpec = tween(700), label = "acidity")
    val animSweetness by animateFloatAsState(targetValue = metrics.sweetness / 100f, animationSpec = tween(700), label = "sweetness")
    val animBody by animateFloatAsState(targetValue = metrics.body / 100f, animationSpec = tween(700), label = "body")
    val animClarity by animateFloatAsState(targetValue = metrics.clarity / 100f, animationSpec = tween(700), label = "clarity")
    val animBitterness by animateFloatAsState(targetValue = metrics.bitterness / 100f, animationSpec = tween(700), label = "bitterness")

    val axisLabels = listOf(
        "Acidity ${metrics.acidity.toInt()}%",
        "Sweetness ${metrics.sweetness.toInt()}%",
        "Body ${metrics.body.toInt()}%",
        "Clarity ${metrics.clarity.toInt()}%",
        "Bitterness ${metrics.bitterness.toInt()}%"
    )

    val density = LocalDensity.current
    val textPaint = android.graphics.Paint().apply {
        isAntiAlias = true
        textSize = with(density) { 11.sp.toPx() }
        color = android.graphics.Color.rgb(0x2C, 0x1A, 0x11)
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
        textAlign = android.graphics.Paint.Align.CENTER
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.15f)
            .clip(RoundedCornerShape(24.dp))
            .background(ParchmentCream)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f + 4.dp.toPx())
            val maxRadius = (minOf(size.width, size.height) / 2f) - 34.dp.toPx()

            val numAxes = 5
            val angleStep = (2 * PI / numAxes).toFloat()
            val startAngle = (-PI / 2).toFloat() // Start pointing top

            // 1. Draw 4 concentric polygonal web grid lines (25%, 50%, 75%, 100%)
            val steps = listOf(0.25f, 0.50f, 0.75f, 1.0f)
            for (stepRatio in steps) {
                val gridRadius = maxRadius * stepRatio
                val webPath = Path()
                for (i in 0 until numAxes) {
                    val angle = startAngle + i * angleStep
                    val x = center.x + gridRadius * cos(angle)
                    val y = center.y + gridRadius * sin(angle)
                    if (i == 0) webPath.moveTo(x, y) else webPath.lineTo(x, y)
                }
                webPath.close()
                drawPath(
                    path = webPath,
                    color = if (stepRatio == 1.0f) webColor.copy(alpha = 0.7f) else webColor,
                    style = Stroke(width = if (stepRatio == 1.0f) 1.5.dp.toPx() else 1.dp.toPx())
                )
            }

            // 2. Draw 5 radial axis spokes from center to 100% boundary
            for (i in 0 until numAxes) {
                val angle = startAngle + i * angleStep
                val spokeEnd = Offset(
                    center.x + maxRadius * cos(angle),
                    center.y + maxRadius * sin(angle)
                )
                drawLine(
                    color = webColor,
                    start = center,
                    end = spokeEnd,
                    strokeWidth = 1.dp.toPx()
                )

                // Draw Text Label outside spoke
                val labelDistance = maxRadius + 18.dp.toPx()
                val labelX = center.x + labelDistance * cos(angle)
                val labelY = center.y + labelDistance * sin(angle) + 4.dp.toPx()

                drawContext.canvas.nativeCanvas.drawText(
                    axisLabels[i],
                    labelX,
                    labelY,
                    textPaint
                )
            }

            // 3. Draw Data Polygon
            val animatedValues = listOf(
                animAcidity,
                animSweetness,
                animBody,
                animClarity,
                animBitterness
            )

            val dataPath = Path()
            val dataPoints = mutableListOf<Offset>()

            for (i in 0 until numAxes) {
                val angle = startAngle + i * angleStep
                val r = maxRadius * animatedValues[i].coerceIn(0.05f, 1.0f)
                val point = Offset(
                    center.x + r * cos(angle),
                    center.y + r * sin(angle)
                )
                dataPoints.add(point)
                if (i == 0) dataPath.moveTo(point.x, point.y) else dataPath.lineTo(point.x, point.y)
            }
            dataPath.close()

            // Fill Data Area
            drawPath(path = dataPath, color = fillColor, style = Fill)

            // Stroke Data Area
            drawPath(
                path = dataPath,
                color = lineColor,
                style = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw vertex dots
            for (pt in dataPoints) {
                drawCircle(color = ParchmentCream, radius = 5.dp.toPx(), center = pt)
                drawCircle(color = lineColor, radius = 3.5.dp.toPx(), center = pt)
            }

            // Draw center dot
            drawCircle(color = CoffeeClay.copy(alpha = 0.5f), radius = 3.dp.toPx(), center = center)
        }
    }
}
