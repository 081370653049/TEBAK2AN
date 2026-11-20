package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.AnimalId
import com.example.ui.components.AnimalArtwork
import com.example.ui.components.AnimatedStorybookBackground
import com.example.ui.components.ConfettiBurstOverlay
import com.example.ui.components.ToyBlockCard
import com.example.ui.theme.CocoaInk
import com.example.ui.theme.CocoaInkMuted
import com.example.ui.theme.GrassGreen
import com.example.ui.theme.GrassGreenDark
import com.example.ui.theme.SplashBlue
import com.example.ui.theme.SplashBlueDark
import com.example.ui.theme.SunburstYellow
import com.example.ui.theme.SunburstYellowDark
import com.example.ui.theme.WarmWhite
import com.example.viewmodel.GameUiState
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VictoryScreen(
    uiState: GameUiState,
    onPlayAgain: () -> Unit,
    onGoHome: () -> Unit
) {
    BackHandler(onBack = onGoHome)

    val starCount = uiState.quiz.roundStars.coerceAtLeast(1)
    var revealedStars by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in 1..starCount) {
            delay(220L)
            revealedStars = i
        }
    }

    val infinite = rememberInfiniteTransition(label = "victory_sunburst")
    val spin by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sunburst_spin"
    )

    AnimatedStorybookBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Cheering Animals Stage with Rotating Golden Sunburst
                Box(
                    modifier = Modifier.size(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val r = size.minDimension * 0.48f
                        rotate(spin, center) {
                            for (i in 0 until 12) {
                                val a = i * 30.0 * PI / 180.0
                                drawLine(
                                    color = SunburstYellow.copy(alpha = 0.45f),
                                    start = Offset(
                                        center.x + cos(a).toFloat() * r * 0.42f,
                                        center.y + sin(a).toFloat() * r * 0.42f
                                    ),
                                    end = Offset(
                                        center.x + cos(a).toFloat() * r,
                                        center.y + sin(a).toFloat() * r
                                    ),
                                    strokeWidth = 18f
                                )
                            }
                        }
                        drawCircle(SunburstYellow.copy(alpha = 0.35f), r * 0.68f, center)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy((-18).dp)
                    ) {
                        AnimalArtwork(
                            animalId = AnimalId.CAT,
                            isCelebrating = true,
                            modifier = Modifier.size(88.dp)
                        )
                        AnimalArtwork(
                            animalId = AnimalId.LION,
                            isCelebrating = true,
                            modifier = Modifier.size(126.dp)
                        )
                        AnimalArtwork(
                            animalId = AnimalId.DUCK,
                            isCelebrating = true,
                            modifier = Modifier.size(88.dp)
                        )
                    }
                }

                // Celebration Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = WarmWhite,
                    border = BorderStroke(3.dp, SunburstYellow),
                    shadowElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.victory_title),
                            style = MaterialTheme.typography.displayMedium,
                            color = CocoaInk,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = stringResource(R.string.victory_subtitle),
                            style = MaterialTheme.typography.bodyLarge,
                            color = CocoaInkMuted,
                            textAlign = TextAlign.Center
                        )

                        // Popping Golden Stars Row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 1..starCount) {
                                val shown = i <= revealedStars
                                val starScale by animateFloatAsState(
                                    targetValue = if (shown) 1.15f else 0.1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    ),
                                    label = "victory_star_$i"
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = if (shown) SunburstYellow else Color(0xFFE6DCC8),
                                    modifier = Modifier
                                        .size(46.dp)
                                        .scale(starScale)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFFFFF4CC),
                            border = BorderStroke(2.dp, SunburstYellowDark)
                        ) {
                            Text(
                                text = stringResource(R.string.stars_collected_format, uiState.totalStars),
                                style = MaterialTheme.typography.titleMedium,
                                color = CocoaInk,
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Giant Toddler Action Buttons
                ToyBlockCard(
                    onClick = onPlayAgain,
                    backgroundColor = GrassGreen,
                    shadowColor = GrassGreenDark,
                    cornerRadius = 26.dp,
                    lipHeight = 7.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .testTag("play_again_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Replay,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = stringResource(R.string.play_again_button),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                    }
                }

                ToyBlockCard(
                    onClick = onGoHome,
                    backgroundColor = SplashBlue,
                    shadowColor = SplashBlueDark,
                    cornerRadius = 26.dp,
                    lipHeight = 6.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .testTag("victory_home_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = stringResource(R.string.go_home_button),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Joyful continuous confetti shower
        ConfettiBurstOverlay(triggerKey = 1, continuous = true)
    }
}
