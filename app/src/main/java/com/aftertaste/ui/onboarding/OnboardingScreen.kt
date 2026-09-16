package com.aftertaste.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aftertaste.ui.components.CoffeeBagIllustration
import com.aftertaste.ui.components.CoffeeJarIllustration
import com.aftertaste.ui.components.EspressoMachineIllustration
import com.aftertaste.ui.theme.AfterTasteTheme
import com.aftertaste.ui.theme.CoffeeClay
import com.aftertaste.ui.theme.CoffeeOnSurfaceVariant
import com.aftertaste.ui.theme.EspressoText
import com.aftertaste.ui.theme.MutedTerracotta
import com.aftertaste.ui.theme.ParchmentCream
import com.aftertaste.ui.theme.TerracottaAccent
import com.aftertaste.util.OnboardingPreferences
import kotlinx.coroutines.launch

data class OnboardingSlideData(
    val category: String,
    val headline: String,
    val description: String,
    val illustrationType: SlideIllustration
)

enum class SlideIllustration {
    ESPRESSO_MACHINE,
    COFFEE_BAG,
    COFFEE_JAR
}

val OnboardingSlides = listOf(
    OnboardingSlideData(
        category = "Discover",
        headline = "All your favorites",
        description = "Discover artisanal roasters and cozy spots nearby",
        illustrationType = SlideIllustration.ESPRESSO_MACHINE
    ),
    OnboardingSlideData(
        category = "Log Sips",
        headline = "Order your coffee you like",
        description = "Save ratings, bean notes, and photos from every visit",
        illustrationType = SlideIllustration.COFFEE_BAG
    ),
    OnboardingSlideData(
        category = "Deliver for you",
        headline = "Build your passport",
        description = "Collect digital stamps and track your coffee journey",
        illustrationType = SlideIllustration.COFFEE_JAR
    )
)

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { OnboardingSlides.size })

    val handleComplete = {
        OnboardingPreferences.setOnboardingCompleted(context, true)
        onFinishOnboarding()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = CoffeeClay
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage < OnboardingSlides.size - 1) {
                    TextButton(
                        onClick = handleComplete
                    ) {
                        Text(
                            text = "Skip",
                            color = ParchmentCream.copy(alpha = 0.85f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }

            // Pager Carousel
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                OnboardingSlideItem(
                    slide = OnboardingSlides[page],
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Navigation Row with Pill Indicators and Circular Chevron Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pagination Pill Indicators
                PillPageIndicator(
                    pageCount = OnboardingSlides.size,
                    currentPage = pagerState.currentPage
                )

                // Circular CTA action button: 48x48dp terracotta circle with right chevron
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TerracottaAccent),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (pagerState.currentPage < OnboardingSlides.size - 1) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                handleComplete()
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = if (pagerState.currentPage == OnboardingSlides.size - 1) "Get Started" else "Next",
                            tint = EspressoText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingSlideItem(
    slide: OnboardingSlideData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Category Badge / Header
            Text(
                text = slide.category.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = TerracottaAccent,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            // Center Illustration
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (slide.illustrationType) {
                    SlideIllustration.ESPRESSO_MACHINE -> EspressoMachineIllustration(modifier = Modifier.fillMaxSize())
                    SlideIllustration.COFFEE_BAG -> CoffeeBagIllustration(modifier = Modifier.fillMaxSize())
                    SlideIllustration.COFFEE_JAR -> CoffeeJarIllustration(modifier = Modifier.fillMaxSize())
                }
            }

            // Headline and Description
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = slide.headline,
                    style = MaterialTheme.typography.headlineMedium,
                    color = EspressoText,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = slide.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = CoffeeOnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
fun PillPageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage

            // Active slide = elongated pill (24x8dp, terracotta), inactive slides = small dots (8x8dp)
            val animatedWidth by animateDpAsState(
                targetValue = if (isSelected) 24.dp else 8.dp,
                animationSpec = tween(durationMillis = 300),
                label = "PillWidth"
            )

            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) TerracottaAccent else MutedTerracotta.copy(alpha = 0.5f),
                animationSpec = tween(durationMillis = 300),
                label = "PillColor"
            )

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(animatedWidth)
                    .clip(CircleShape)
                    .background(animatedColor)
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
fun OnboardingScreenPreview() {
    AfterTasteTheme {
        OnboardingScreen(onFinishOnboarding = {})
    }
}
