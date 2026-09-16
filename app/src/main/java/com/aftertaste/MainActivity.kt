package com.aftertaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aftertaste.data.local.AppDatabase
import com.aftertaste.repository.CafeRepositoryImpl
import com.aftertaste.ui.navigation.MainAppScreen
import com.aftertaste.ui.onboarding.OnboardingScreen
import com.aftertaste.ui.theme.AfterTasteTheme
import com.aftertaste.ui.viewmodel.CafeViewModel
import com.aftertaste.ui.viewmodel.CafeViewModelFactory
import com.aftertaste.util.OnboardingPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AfterTasteTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    val context = LocalContext.current
    var showOnboarding by remember {
        mutableStateOf(!OnboardingPreferences.isOnboardingCompleted(context))
    }

    // Initialize Database, Repository, and ViewModel
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember {
        CafeRepositoryImpl(
            database.cafeVisitDao(),
            database.orderedItemDao(),
            database.tagDao(),
            database.photoDao(),
            database.wishlistCafeDao(),
        )
    }
    val viewModel: CafeViewModel = viewModel(factory = CafeViewModelFactory(repository))

    if (showOnboarding) {
        OnboardingScreen(
            onFinishOnboarding = {
                showOnboarding = false
            }
        )
    } else {
        MainAppScreen(
            viewModel = viewModel,
            onResetOnboarding = {
                OnboardingPreferences.setOnboardingCompleted(context, false)
                showOnboarding = true
            }
        )
    }
}
