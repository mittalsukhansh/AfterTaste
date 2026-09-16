package com.aftertaste.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOutline
import com.aftertaste.ui.theme.EspressoText
import kotlin.math.roundToInt

@Composable
fun CoffeeBeanRatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    onRatingChanged: (Float) -> Unit = {},
    maxBeans: Int = 5,
    beanSize: Dp = 32.dp,
    beanPadding: Dp = 6.dp,
    isSelectable: Boolean = true,
    activeColor: Color = CoffeeClay,
    inactiveColor: Color = CoffeeOutline.copy(alpha = 0.35f),
    lineColor: Color = EspressoText,
) {
    Row(
        modifier = modifier
            .pointerInput(isSelectable, maxBeans) {
                if (!isSelectable) return@pointerInput
                detectTapGestures { offset ->
                    val totalWidth = size.width.toFloat()
                    val newRating = calculateRatingFromX(offset.x, totalWidth, maxBeans)
                    onRatingChanged(newRating)
                }
            }
            .pointerInput(isSelectable, maxBeans) {
                if (!isSelectable) return@pointerInput
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    val totalWidth = size.width.toFloat()
                    val newRating = calculateRatingFromX(change.position.x, totalWidth, maxBeans)
                    onRatingChanged(newRating)
                }
            },
        horizontalArrangement = Arrangement.spacedBy(beanPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 1..maxBeans) {
            val fillFraction = when {
                rating >= i -> 1.0f
                rating >= (i - 0.5f) -> 0.5f
                else -> 0.0f
            }

            SingleCoffeeBeanCanvas(
                fillFraction = fillFraction,
                size = beanSize,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                lineColor = lineColor,
            )
        }
    }
}

@Composable
private fun SingleCoffeeBeanCanvas(
    fillFraction: Float,
    size: Dp,
    activeColor: Color,
    inactiveColor: Color,
    lineColor: Color
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Bean oval geometry parameters (slight oval tilting)
        val insetX = w * 0.1f
        val insetY = h * 0.05f
        val beanWidth = w - (insetX * 2)
        val beanHeight = h - (insetY * 2)

        // Function to draw bean body
        fun drawBeanBody(color: Color) {
            drawOval(
                color = color,
                topLeft = Offset(insetX, insetY),
                size = Size(beanWidth, beanHeight)
            )
        }

        // 1. Draw Inactive background bean
        drawBeanBody(inactiveColor)

        // 2. Draw Active bean clipped to fillFraction width
        if (fillFraction > 0f) {
            clipRect(
                left = 0f,
                top = 0f,
                right = w * fillFraction,
                bottom = h
            ) {
                drawBeanBody(activeColor)
            }
        }

        // 3. Draw Center Crease Line
        val creasePath = Path().apply {
            moveTo(w * 0.5f, insetY + (beanHeight * 0.1f))
            cubicTo(
                w * 0.35f, insetY + (beanHeight * 0.35f),
                w * 0.65f, insetY + (beanHeight * 0.65f),
                w * 0.5f, insetY + (beanHeight * 0.9f)
            )
        }

        val strokeWidth = (w * 0.07f).coerceAtLeast(2f)
        val lineAlpha = if (fillFraction > 0f) 0.85f else 0.4f

        drawPath(
            path = creasePath,
            color = lineColor.copy(alpha = lineAlpha),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

private fun calculateRatingFromX(x: Float, totalWidth: Float, maxBeans: Int): Float {
    if (totalWidth <= 0f) return 1.0f
    val ratio = (x / totalWidth).coerceIn(0f, 1f)
    val rawRating = ratio * maxBeans
    val stepRating = (rawRating * 2).roundToInt() / 2f
    return stepRating.coerceIn(0.5f, maxBeans.toFloat())
}

@Preview(showBackground = true)
@Composable
fun CoffeeBeanRatingBarPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        CoffeeBeanRatingBar(rating = 0.0f)
        CoffeeBeanRatingBar(rating = 2.5f)
        CoffeeBeanRatingBar(rating = 4.5f)
        CoffeeBeanRatingBar(rating = 5.0f)
    }
}
