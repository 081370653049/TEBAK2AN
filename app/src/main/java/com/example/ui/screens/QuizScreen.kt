package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.Animal
import com.example.model.GameMode
import com.example.ui.components.AnimalArtwork
import com.example.ui.components.AnimatedStorybookBackground
import com.example.ui.components.ConfettiBurstOverlay
import com.example.ui.components.PulsingSpeakerButton
import com.example.ui.components.StarProgressStrip
import com.example.ui.components.ToyBlockCard
import com.example.ui.theme.CocoaInk
import com.example.ui.theme.CocoaInkMuted
import com.example.ui.theme.CoralBerry
import com.example.ui.theme.CoralBerryDark
import com.example.ui.theme.GrassGreen
import com.example.ui.theme.GrassGreenDark
import com.example.ui.theme.LavenderPop
import com.example.ui.theme.LavenderPopDark
import com.example.ui.theme.SunburstYellow
import com.example.ui.theme.SunburstYellowDark
import com.example.ui.theme.TangerineGlow
import com.example.ui.theme.WarmWhite
import com.example.viewmodel.GameUiState
import kotlin.math.roundToInt

@Composable
fun QuizScreen(
    uiState: GameUiState,
    onBackHome: () -> Unit,
    onToggleSound: () -> Unit,
    onReplayQuestion: () -> Unit,
    onAnswerSelected: (Animal) -> Unit,
    onNextQuestion: () -> Unit
) {
    BackHandler(onBack = onBackHome)

    val quiz = uiState.quiz
    val question = quiz.currentQuestion ?: return

    AnimatedStorybookBackground {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            val isWide = maxWidth >= 600.dp

            Column(
                modifier = Modifier
                    .widthIn(max = 760.dp)
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar: Chunky Home Button, Animated Stars Progress, Sound Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ToyBlockCard(
                        onClick = onBackHome,
                        backgroundColor = SunburstYellow,
                        shadowColor = SunburstYellowDark,
                        cornerRadius = 18.dp,
                        lipHeight = 4.dp,
                        modifier = Modifier
                            .size(50.dp)
                            .testTag("quiz_back_button")
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Home,
                                contentDescription = stringResource(R.string.back_button_desc),
                                tint = CocoaInk,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    StarProgressStrip(
                        total = quiz.totalQuestions.coerceAtLeast(1),
                        earned = quiz.roundStars
                    )

                    SoundToggleButton(
                        isSoundEnabled = uiState.isSoundEnabled,
                        onClick = onToggleSound,
                        modifier = Modifier.testTag("quiz_sound_toggle")
                    )
                }

                // Prompt Stage (Sound Mode or Shadow Mystery Mode)
                if (quiz.mode == GameMode.SOUND_GUESS) {
                    SoundPromptCard(
                        animal = question.target,
                        isRevealed = quiz.isCorrectRevealed,
                        onReplay = onReplayQuestion,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    ShadowPromptCard(
                        animal = question.target,
                        isRevealed = quiz.isCorrectRevealed,
                        onReplay = onReplayQuestion,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Warm Feedback Banner + Instant "Lanjut!" Button on Correct Answer
                AnimatedVisibility(
                    visible = quiz.feedbackText != null,
                    enter = fadeIn() + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                    exit = fadeOut()
                ) {
                    val isPositive = quiz.isFeedbackPositive
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        color = if (isPositive) Color(0xFFE2F9EB) else Color(0xFFFFF1DB),
                        border = BorderStroke(
                            2.5.dp,
                            if (isPositive) GrassGreen else TangerineGlow
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = quiz.feedbackText.orEmpty(),
                                style = MaterialTheme.typography.titleMedium,
                                color = CocoaInk,
                                modifier = Modifier.weight(1f)
                            )
                            if (quiz.isCorrectRevealed) {
                                ToyBlockCard(
                                    onClick = onNextQuestion,
                                    backgroundColor = GrassGreen,
                                    shadowColor = GrassGreenDark,
                                    cornerRadius = 18.dp,
                                    lipHeight = 4.dp,
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("next_question_button")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.next_question_button),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Giant Tactile Answer Cards Grid
                val options = question.options
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (options.size <= 2) {
                        // Extra-large 2-choice layout ideal for 2-3 year olds
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (isWide) 280.dp else 255.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            options.forEach { animal ->
                                AnimalAnswerCard(
                                    animal = animal,
                                    isCorrectTarget = animal.id == question.target.id,
                                    isCorrectRevealed = quiz.isCorrectRevealed,
                                    isWrongTried = animal.id in quiz.wrongAttemptIds,
                                    isShakingNow = quiz.lastWrongAnimalId == animal.id,
                                    shakeTrigger = quiz.wrongShakeTrigger,
                                    onClick = { onAnswerSelected(animal) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize()
                                        .testTag("answer_card_${animal.id.name.lowercase()}")
                                )
                            }
                        }
                    } else {
                        // Clean 2x2 Grid for 4-choice Fun mode
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            val rows = options.chunked(2)
                            rows.forEach { rowAnimals ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    rowAnimals.forEach { animal ->
                                        AnimalAnswerCard(
                                            animal = animal,
                                            isCorrectTarget = animal.id == question.target.id,
                                            isCorrectRevealed = quiz.isCorrectRevealed,
                                            isWrongTried = animal.id in quiz.wrongAttemptIds,
                                            isShakingNow = quiz.lastWrongAnimalId == animal.id,
                                            shakeTrigger = quiz.wrongShakeTrigger,
                                            onClick = { onAnswerSelected(animal) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxSize()
                                                .testTag("answer_card_${animal.id.name.lowercase()}")
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // Full-screen colorful confetti burst on correct answer
        ConfettiBurstOverlay(triggerKey = quiz.celebrationTrigger)
    }
}

@Composable
private fun SoundPromptCard(
    animal: Animal,
    isRevealed: Boolean,
    onReplay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = WarmWhite,
        border = BorderStroke(3.dp, if (isRevealed) GrassGreen else SunburstYellow),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PulsingSpeakerButton(
                onClick = onReplay,
                contentDescription = stringResource(R.string.listen_again_desc),
                buttonSize = 72.dp,
                containerColor = CoralBerry,
                shadowColor = CoralBerryDark,
                modifier = Modifier.testTag("replay_sound_button")
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.prompt_who_makes_sound),
                    style = MaterialTheme.typography.bodyLarge,
                    color = CocoaInkMuted
                )
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF3C4)
                ) {
                    Text(
                        text = "\"${animal.soundDisplay}\"",
                        style = MaterialTheme.typography.headlineMedium,
                        color = CocoaInk,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.hint_tap_speaker),
                    style = MaterialTheme.typography.labelMedium,
                    color = CoralBerry
                )
            }
        }
    }
}

@Composable
private fun ShadowPromptCard(
    animal: Animal,
    isRevealed: Boolean,
    onReplay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = WarmWhite,
        border = BorderStroke(3.dp, if (isRevealed) GrassGreen else LavenderPop),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Animated Shadow Reveal Spotlight
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (isRevealed) animal.cardBackground else Color(0xFFEDE4FF)),
                contentAlignment = Alignment.Center
            ) {
                AnimalArtwork(
                    animalId = animal.id,
                    isSilhouette = !isRevealed,
                    isCelebrating = isRevealed,
                    modifier = Modifier
                        .size(96.dp)
                        .padding(4.dp)
                )
                if (!isRevealed) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = SunburstYellowDark,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (isRevealed) animal.name else stringResource(R.string.prompt_who_is_shadow),
                    style = MaterialTheme.typography.headlineMedium,
                    color = CocoaInk
                )
                Text(
                    text = animal.shadowClue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CocoaInkMuted,
                    maxLines = 3
                )
                ToyBlockCard(
                    onClick = onReplay,
                    backgroundColor = LavenderPop,
                    shadowColor = LavenderPopDark,
                    cornerRadius = 16.dp,
                    lipHeight = 4.dp,
                    modifier = Modifier
                        .height(42.dp)
                        .testTag("replay_shadow_clue_button")
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.listen_button_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimalAnswerCard(
    animal: Animal,
    isCorrectTarget: Boolean,
    isCorrectRevealed: Boolean,
    isWrongTried: Boolean,
    isShakingNow: Boolean,
    shakeTrigger: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Gentle friendly horizontal wobble on wrong tap
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(shakeTrigger) {
        if (isShakingNow && shakeTrigger > 0) {
            for (delta in listOf(-14f, 14f, -10f, 10f, -5f, 0f)) {
                shakeOffset.animateTo(delta, animationSpec = tween(48))
            }
        }
    }

    val isCelebratingThis = isCorrectRevealed && isCorrectTarget
    val cardAlpha = when {
        isCelebratingThis -> 1f
        isCorrectRevealed -> 0.45f
        isWrongTried -> 0.55f
        else -> 1f
    }

    ToyBlockCard(
        onClick = onClick,
        enabled = !isCorrectRevealed,
        backgroundColor = if (isCelebratingThis) Color(0xFFE6FAEE) else animal.cardBackground,
        shadowColor = if (isCelebratingThis) GrassGreenDark else animal.borderColor,
        borderColor = if (isCelebratingThis) GrassGreen else Color.White,
        cornerRadius = 28.dp,
        lipHeight = 7.dp,
        modifier = modifier
            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
            .alpha(cardAlpha)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            if (isCelebratingThis) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GrassGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AnimalArtwork(
                    animalId = animal.id,
                    isCelebrating = isCelebratingThis,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(4.dp)
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.88f)
                ) {
                    Text(
                        text = animal.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = CocoaInk,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
