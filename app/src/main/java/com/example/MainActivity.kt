package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.VictoryScreen
import com.example.ui.theme.TebakHewanTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            TebakHewanTheme {
                val gameViewModel: GameViewModel = viewModel()
                val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()

                AnimatedContent(
                    targetState = uiState.currentScreen,
                    transitionSpec = {
                        (fadeIn(tween(260)) + scaleIn(initialScale = 0.96f, animationSpec = tween(260)))
                            .togetherWith(fadeOut(tween(180)))
                    },
                    label = "screen_transition"
                ) { screen ->
                    when (screen) {
                        AppScreen.Home -> HomeScreen(
                            uiState = uiState,
                            onToggleSound = gameViewModel::toggleSound,
                            onSelectDifficulty = gameViewModel::selectDifficulty,
                            onSelectHabitat = gameViewModel::selectHabitat,
                            onStartMode = { mode -> gameViewModel.startMode(mode) }
                        )

                        AppScreen.Quiz -> QuizScreen(
                            uiState = uiState,
                            onBackHome = gameViewModel::navigateHome,
                            onToggleSound = gameViewModel::toggleSound,
                            onReplayQuestion = gameViewModel::replayCurrentQuestion,
                            onAnswerSelected = gameViewModel::onAnswerSelected,
                            onNextQuestion = gameViewModel::advanceToNextQuestion
                        )

                        AppScreen.Learn -> LearnScreen(
                            uiState = uiState,
                            onBackHome = gameViewModel::navigateHome,
                            onToggleSound = gameViewModel::toggleSound,
                            onFilterHabitat = gameViewModel::onLearnFilterSelected,
                            onAnimalTapped = gameViewModel::onLearnAnimalTapped
                        )

                        AppScreen.Victory -> VictoryScreen(
                            uiState = uiState,
                            onPlayAgain = gameViewModel::playAgain,
                            onGoHome = gameViewModel::navigateHome
                        )
                    }
                }
            }
        }
    }
}
