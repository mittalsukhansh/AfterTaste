package com.aftertaste.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aftertaste.R
import com.aftertaste.ui.theme.ParchmentCream

@Composable
fun EspressoMachineIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(ParchmentCream),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_espresso_machine),
            contentDescription = "Espresso Machine Illustration",
            modifier = Modifier.fillMaxSize(0.85f)
        )
    }
}

@Composable
fun CoffeeBagIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(ParchmentCream),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_coffee_bag),
            contentDescription = "Coffee Bag Illustration",
            modifier = Modifier.fillMaxSize(0.85f)
        )
    }
}

@Composable
fun CoffeeJarIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(ParchmentCream),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_coffee_jar),
            contentDescription = "Coffee Jar Illustration",
            modifier = Modifier.fillMaxSize(0.85f)
        )
    }
}

@Composable
fun CoffeeBeanIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_coffee_bean),
        contentDescription = "Coffee Bean Icon",
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun CoffeeCupIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_coffee_cup),
        contentDescription = "Coffee Cup Icon",
        modifier = modifier.size(24.dp)
    )
}
