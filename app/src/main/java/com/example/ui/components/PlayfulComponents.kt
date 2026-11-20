package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BubblegumPink
import com.example.ui.theme.CoralBerry
import com.example.ui.theme.CreamSkyBottom
import com.example.ui.theme.CreamSkyTop
import com.example.ui.theme.GrassGreen
import com.example.ui.theme.LavenderPop
import com.example.ui.theme.MeadowMint
import com.example.ui.theme.SoftMeadow
import com.example.ui.theme.SplashBlue
import com.example.ui.theme.SunburstYellow
import com.example.ui.theme.SunburstYellowDark
import com.example.ui.theme.TangerineGlow
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Cheerful animated storybook sky with drifting clouds, twinkling stars,
 * rotating soft sunshine, and rolling meadow hills.
 */
@Composable
fun AnimatedStorybookBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "bg_transition")
    val cloudDrift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloud_drift"
    )
    val sunSpin by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 36000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sun_spin"
    )
    val twinkle by infinite.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CreamSkyTop, CreamSkyBottom, Color(0xFFECFAEE))
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Soft rotating sun in top-right corner
            val sunCenter = Offset(w * 0.88f, h * 0.08f)
            rotate(degrees = sunSpin, pivot = sunCenter) {
                for (i in 0 until 10) {
                    val angle = i * 36.0 * PI / 180.0
                    val start = Offset(
                        sunCenter.x + cos(angle).toFloat() * (w * 0.08f),
                        sunCenter.y + sin(angle).toFloat() * (w * 0.08f)
                    )
                    val end = Offset(
                        sunCenter.x + cos(angle).toFloat() * (w * 0.14f),
                        sunCenter.y + sin(angle).toFloat() * (w * 0.14f)
                    )
                    drawLine(
                        color = SunburstYellow.copy(alpha = 0.28f),
                        start = start,
                        end = end,
                        strokeWidth = 10f
                    )
                }
            }
            drawCircle(
                color = SunburstYellow.copy(alpha = 0.28f),
                radius = w * 0.10f,
                center = sunCenter
            )
            drawCircle(
                color = SunburstYellow.copy(alpha = 0.42f),
                radius = w * 0.065f,
                center = sunCenter
            )

            // Fluffy drifting clouds
            fun drawCloud(cx: Float, cy: Float, scale: Float, alpha: Float) {
                val r = w * 0.055f * scale
                val col = Color.White.copy(alpha = alpha)
                drawCircle(col, r, Offset(cx, cy))
                drawCircle(col, r * 0.82f, Offset(cx - r * 0.85f, cy + r * 0.2f))
                drawCircle(col, r * 0.78f, Offset(cx + r * 0.85f, cy + r * 0.22f))
                drawRoundRect(
                    color = col,
                    topLeft = Offset(cx - r * 1.4f, cy + r * 0.12f),
                    size = Size(r * 2.8f, r * 0.9f),
                    cornerRadius = CornerRadius(r * 0.5f, r * 0.5f)
                )
            }

            val c1x = ((cloudDrift * (w + w * 0.5f)) - w * 0.25f)
            val c2x = (((cloudDrift + 0.52f) % 1f) * (w + w * 0.5f)) - w * 0.25f
            drawCloud(c1x, h * 0.15f, 1.15f, 0.65f)
            drawCloud(c2x, h * 0.27f, 0.85f, 0.52f)

            // Subtle pastel sparkles
            val starOffsets = listOf(
                Offset(w * 0.12f, h * 0.11f),
                Offset(w * 0.52f, h * 0.07f),
                Offset(w * 0.85f, h * 0.34f),
                Offset(w * 0.14f, h * 0.48f)
            )
            starOffsets.forEachIndexed { idx, pt ->
                val pulse = if (idx % 2 == 0) twinkle else 1.45f - twinkle
                drawCircle(
                    color = SunburstYellow.copy(alpha = 0.35f * pulse),
                    radius = 7f * pulse,
                    center = pt
                )
            }

            // Gentle rolling meadow hills at bottom
            val hillPath1 = Path().apply {
                moveTo(0f, h * 0.88f)
                quadraticTo(w * 0.28f, h * 0.80f, w * 0.58f, h * 0.88f)
                quadraticTo(w * 0.82f, h * 0.94f, w, h * 0.86f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hillPath1, SoftMeadow.copy(alpha = 0.72f))

            val hillPath2 = Path().apply {
                moveTo(0f, h * 0.93f)
                quadraticTo(w * 0.35f, h * 0.87f, w * 0.72f, h * 0.94f)
                quadraticTo(w * 0.90f, h * 0.97f, w, h * 0.91f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hillPath2, GrassGreen.copy(alpha = 0.18f))
        }

        content()
    }
}

/**
 * Chunky tactile 3D toy-block button/card with spring squash-and-stretch press physics.
 */
@Composable
fun ToyBlockCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    shadowColor: Color,
    cornerRadius: Dp = 26.dp,
    lipHeight: Dp = 6.dp,
    borderColor: Color? = null,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "toy_card_scale"
    )
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(shadowColor)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                role = Role.Button,
                onClick = onClick
            )
            .padding(bottom = lipHeight)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = shape,
            color = backgroundColor,
            border = borderColor?.let { BorderStroke(2.5.dp, it) }
        ) {
            Box(content = content)
        }
    }
}

/**
 * Giant pulsing speaker button with animated sound waves for toddler listening prompts.
 */
@Composable
fun PulsingSpeakerButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 76.dp,
    containerColor: Color = CoralBerry,
    shadowColor: Color = Color(0xFFD94343)
) {
    val infinite = rememberInfiniteTransition(label = "speaker_pulse")
    val ring1 by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.42f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring1"
    )
    val ring1Alpha by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring1_alpha"
    )
    val iconBounce by infinite.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_bounce"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "speaker_press"
    )

    Box(
        modifier = modifier.size(buttonSize + 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Expanding sound ripple ring
        Box(
            modifier = Modifier
                .size(buttonSize)
                .scale(ring1)
                .clip(CircleShape)
                .background(containerColor.copy(alpha = ring1Alpha))
        )

        // Tactile circular button
        Box(
            modifier = Modifier
                .size(buttonSize)
                .scale(pressScale * iconBounce)
                .clip(CircleShape)
                .background(shadowColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = true),
                    role = Role.Button,
                    onClick = onClick
                )
                .padding(bottom = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.VolumeUp,
                    contentDescription = contentDescription,
                    tint = Color.White,
                    modifier = Modifier.size(buttonSize * 0.52f)
                )
            }
        }
    }
}

/**
 * Animated golden star progress strip for quiz rounds.
 */
@Composable
fun StarProgressStrip(
    total: Int,
    earned: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.85f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until total) {
            val isEarned = i < earned
            val starScale by animateFloatAsState(
                targetValue = if (isEarned) 1.15f else 0.88f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                label = "star_scale_$i"
            )
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = if (isEarned) SunburstYellow else Color(0xFFE3D8C6),
                modifier = Modifier
                    .size(26.dp)
                    .scale(starScale)
            )
        }
    }
}

private data class ConfettiPiece(
    val xFraction: Float,
    val speedY: Float,
    val swayAmp: Float,
    val swayFreq: Float,
    val sizeDp: Float,
    val color: Color,
    val rotationSpeed: Float,
    val isCircle: Boolean
)

/**
 * Colorful confetti burst animation triggered whenever [triggerKey] increments,
 * or continuously when [continuous] is true (e.g., on the Victory screen).
 */
@Composable
fun ConfettiBurstOverlay(
    triggerKey: Int,
    modifier: Modifier = Modifier,
    continuous: Boolean = false
) {
    val palette = remember {
        listOf(
            CoralBerry,
            SunburstYellow,
            MeadowMint,
            SplashBlue,
            LavenderPop,
            BubblegumPink,
            TangerineGlow,
            GrassGreen
        )
    }
    val pieces = remember(triggerKey, continuous) {
        val rng = Random(triggerKey * 37 + 13)
        List(44) {
            ConfettiPiece(
                xFraction = rng.nextFloat(),
                speedY = 0.65f + rng.nextFloat() * 0.75f,
                swayAmp = 18f + rng.nextFloat() * 36f,
                swayFreq = 2f + rng.nextFloat() * 4f,
                sizeDp = 10f + rng.nextFloat() * 12f,
                color = palette[rng.nextInt(palette.size)],
                rotationSpeed = (rng.nextFloat() - 0.5f) * 720f,
                isCircle = rng.nextBoolean()
            )
        }
    }

    if (continuous) {
        val infinite = rememberInfiniteTransition(label = "victory_confetti")
        val progress by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "confetti_loop"
        )
        ConfettiCanvas(pieces = pieces, progress = progress, alpha = 0.92f, modifier = modifier)
    } else if (triggerKey > 0) {
        var target by remember(triggerKey) { mutableFloatStateOf(0f) }
        LaunchedEffect(triggerKey) {
            target = 0f
            delay(16)
            target = 1f
        }
        val progress by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            label = "confetti_burst"
        )
        if (progress > 0f && progress < 0.99f) {
            val fadeAlpha = (1f - ((progress - 0.65f).coerceAtLeast(0f) / 0.35f)).coerceIn(0f, 1f)
            ConfettiCanvas(pieces = pieces, progress = progress, alpha = fadeAlpha, modifier = modifier)
        }
    }
}

@Composable
private fun ConfettiCanvas(
    pieces: List<ConfettiPiece>,
    progress: Float,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        pieces.forEach { p ->
            val y = ((progress * p.speedY * 1.25f) % 1.15f) * h - h * 0.08f
            val x = p.xFraction * w + sin(progress * p.swayFreq * PI).toFloat() * p.swayAmp
            val drawColor = p.color.copy(alpha = alpha)
            if (p.isCircle) {
                drawCircle(
                    color = drawColor,
                    radius = p.sizeDp * 0.55f,
                    center = Offset(x, y)
                )
            } else {
                rotate(degrees = progress * p.rotationSpeed, pivot = Offset(x, y)) {
                    drawRoundRect(
                        color = drawColor,
                        topLeft = Offset(x - p.sizeDp * 0.6f, y - p.sizeDp * 0.35f),
                        size = Size(p.sizeDp * 1.2f, p.sizeDp * 0.7f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
            }
        }
    }
}

/**
 * Small golden trophy badge star with warm outline.
 */
@Composable
fun GoldenStarPill(
    stars: Int,
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = SunburstYellow,
        border = BorderStroke(2.5.dp, SunburstYellowDark)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            androidx.compose.material3.Text(
                text = label,
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                color = Color(0xFF4A2E00)
            )
        }
    }
}
