package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import com.example.model.AnimalId
import com.example.ui.theme.ShadowIndigo
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Cute animated storybook illustration for all 12 toddler animals.
 * Supports continuous breathing/wiggling, eye blinking, happy celebration hop,
 * and clean shadow silhouette mode for Tebak Bayangan!
 */
@Composable
fun AnimalArtwork(
    animalId: AnimalId,
    modifier: Modifier = Modifier,
    isSilhouette: Boolean = false,
    isCelebrating: Boolean = false,
    isActiveBounce: Boolean = false
) {
    val infinite = rememberInfiniteTransition(label = "animal_idle_$animalId")

    val wiggle by infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isCelebrating) 360 else 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wiggle"
    )

    val blinkCycle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blink"
    )
    val isBlinking = blinkCycle > 0.93f

    val hopY by infinite.animateFloat(
        initialValue = 0f,
        targetValue = if (isCelebrating || isActiveBounce) -14f else -3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isCelebrating || isActiveBounce) 280 else 1350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hop"
    )

    Box(
        modifier = modifier.graphicsLayer {
            translationY = hopY
            rotationZ = if (isCelebrating) wiggle * 6.5f else wiggle * 2.2f
            val scaleFactor = if (isCelebrating) 1.07f else 1f
            scaleX = scaleFactor
            scaleY = scaleFactor
        }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val s = size.minDimension
            val cx = size.width / 2f
            val cy = size.height / 2f

            when (animalId) {
                AnimalId.CAT -> drawCat(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.DOG -> drawDog(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.COW -> drawCow(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.CHICKEN -> drawChicken(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.LION -> drawLion(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.ELEPHANT -> drawElephant(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.MONKEY -> drawMonkey(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.RABBIT -> drawRabbit(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.DUCK -> drawDuck(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.FROG -> drawFrog(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.FISH -> drawFish(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
                AnimalId.WHALE -> drawWhale(cx, cy, s, wiggle, isBlinking, isCelebrating, isSilhouette)
            }
        }
    }
}

internal fun Color.orShadow(isSilhouette: Boolean): Color =
    if (isSilhouette) ShadowIndigo else this

internal fun DrawScope.drawCuteEyes(
    leftCenter: Offset,
    rightCenter: Offset,
    radius: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    if (isSilhouette) return
    val ink = Color(0xFF2D221E)
    if (isCelebrating) {
        // Happy curved ^ ^ eyes!
        listOf(leftCenter, rightCenter).forEach { c ->
            val path = Path().apply {
                moveTo(c.x - radius * 1.1f, c.y + radius * 0.2f)
                quadraticTo(c.x, c.y - radius * 1.15f, c.x + radius * 1.1f, c.y + radius * 0.2f)
            }
            drawPath(path, ink, style = Stroke(width = radius * 0.48f, cap = StrokeCap.Round))
        }
    } else if (isBlinking) {
        listOf(leftCenter, rightCenter).forEach { c ->
            drawLine(
                color = ink,
                start = Offset(c.x - radius, c.y),
                end = Offset(c.x + radius, c.y),
                strokeWidth = radius * 0.45f,
                cap = StrokeCap.Round
            )
        }
    } else {
        listOf(leftCenter, rightCenter).forEach { c ->
            drawCircle(ink, radius, c)
            drawCircle(Color.White, radius * 0.36f, Offset(c.x - radius * 0.28f, c.y - radius * 0.28f))
            drawCircle(Color.White.copy(alpha = 0.7f), radius * 0.16f, Offset(c.x + radius * 0.25f, c.y + radius * 0.25f))
        }
    }
}

internal fun DrawScope.drawRosyCheeks(
    leftCenter: Offset,
    rightCenter: Offset,
    radius: Float,
    isSilhouette: Boolean
) {
    if (isSilhouette) return
    val blush = Color(0xFFFF8BA7).copy(alpha = 0.55f)
    drawOval(
        color = blush,
        topLeft = Offset(leftCenter.x - radius * 1.2f, leftCenter.y - radius * 0.75f),
        size = Size(radius * 2.4f, radius * 1.5f)
    )
    drawOval(
        color = blush,
        topLeft = Offset(rightCenter.x - radius * 1.2f, rightCenter.y - radius * 0.75f),
        size = Size(radius * 2.4f, radius * 1.5f)
    )
}

internal fun DrawScope.drawHappySmile(
    center: Offset,
    width: Float,
    strokeWidth: Float,
    isSilhouette: Boolean
) {
    if (isSilhouette) return
    val path = Path().apply {
        moveTo(center.x - width * 0.5f, center.y)
        quadraticTo(center.x, center.y + width * 0.55f, center.x + width * 0.5f, center.y)
    }
    drawPath(path, Color(0xFF3A2820), style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
}

private fun DrawScope.drawCat(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val fur = Color(0xFFFFA94D).orShadow(isSilhouette)
    val furStripe = Color(0xFFE87E24).orShadow(isSilhouette)
    val earInner = Color(0xFFFFB5C2).orShadow(isSilhouette)
    val r = s * 0.34f

    // Pointy ears
    val earTilt = wiggle * 4f
    val leftEar = Path().apply {
        moveTo(cx - r * 0.88f, cy - r * 0.25f)
        lineTo(cx - r * 0.92f + earTilt, cy - r * 1.25f)
        lineTo(cx - r * 0.12f, cy - r * 0.85f)
        close()
    }
    val rightEar = Path().apply {
        moveTo(cx + r * 0.88f, cy - r * 0.25f)
        lineTo(cx + r * 0.92f + earTilt, cy - r * 1.25f)
        lineTo(cx + r * 0.12f, cy - r * 0.85f)
        close()
    }
    drawPath(leftEar, fur)
    drawPath(rightEar, fur)

    if (!isSilhouette) {
        val leftInner = Path().apply {
            moveTo(cx - r * 0.74f, cy - r * 0.42f)
            lineTo(cx - r * 0.76f + earTilt, cy - r * 1.02f)
            lineTo(cx - r * 0.28f, cy - r * 0.78f)
            close()
        }
        val rightInner = Path().apply {
            moveTo(cx + r * 0.74f, cy - r * 0.42f)
            lineTo(cx + r * 0.76f + earTilt, cy - r * 1.02f)
            lineTo(cx + r * 0.28f, cy - r * 0.78f)
            close()
        }
        drawPath(leftInner, earInner)
        drawPath(rightInner, earInner)
    }

    // Round chubby face
    drawOval(
        color = fur,
        topLeft = Offset(cx - r * 1.06f, cy - r * 0.92f),
        size = Size(r * 2.12f, r * 1.92f)
    )

    if (!isSilhouette) {
        // Cute forehead stripes
        for (i in -1..1) {
            drawRoundRect(
                color = furStripe,
                topLeft = Offset(cx + i * r * 0.22f - r * 0.06f, cy - r * 0.88f),
                size = Size(r * 0.12f, r * 0.34f),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
        // Soft cream muzzle
        drawOval(
            color = Color(0xFFFFF3E0),
            topLeft = Offset(cx - r * 0.45f, cy + r * 0.06f),
            size = Size(r * 0.90f, r * 0.60f)
        )
        // Whiskers
        val wColor = Color(0xFF5A3E2B)
        for (side in listOf(-1f, 1f)) {
            drawLine(
                wColor,
                Offset(cx + side * r * 0.55f, cy + r * 0.18f),
                Offset(cx + side * r * 1.22f, cy + r * 0.08f + wiggle * 2f),
                strokeWidth = s * 0.022f,
                cap = StrokeCap.Round
            )
            drawLine(
                wColor,
                Offset(cx + side * r * 0.55f, cy + r * 0.36f),
                Offset(cx + side * r * 1.22f, cy + r * 0.42f - wiggle * 2f),
                strokeWidth = s * 0.022f,
                cap = StrokeCap.Round
            )
        }
        // Pink triangle nose
        val nose = Path().apply {
            moveTo(cx - r * 0.15f, cy + r * 0.14f)
            lineTo(cx + r * 0.15f, cy + r * 0.14f)
            lineTo(cx, cy + r * 0.30f)
            close()
        }
        drawPath(nose, Color(0xFFFF6B8B))
        // W-shaped cat mouth
        val mouth = Path().apply {
            moveTo(cx - r * 0.24f, cy + r * 0.40f)
            quadraticTo(cx - r * 0.12f, cy + r * 0.54f, cx, cy + r * 0.34f)
            quadraticTo(cx + r * 0.12f, cy + r * 0.54f, cx + r * 0.24f, cy + r * 0.40f)
        }
        drawPath(mouth, Color(0xFF3A2820), style = Stroke(width = s * 0.028f, cap = StrokeCap.Round))
    }

    drawCuteEyes(
        Offset(cx - r * 0.42f, cy - r * 0.10f),
        Offset(cx + r * 0.42f, cy - r * 0.10f),
        r * 0.17f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.62f, cy + r * 0.20f),
        Offset(cx + r * 0.62f, cy + r * 0.20f),
        r * 0.14f,
        isSilhouette
    )
}

private fun DrawScope.drawDog(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val fur = Color(0xFFD69A5E).orShadow(isSilhouette)
    val earColor = Color(0xFF9E6234).orShadow(isSilhouette)
    val muzzle = Color(0xFFFFF2DD).orShadow(isSilhouette)
    val r = s * 0.33f

    // Floppy wiggling ears
    rotate(degrees = -16f + wiggle * 7f, pivot = Offset(cx - r * 0.85f, cy - r * 0.55f)) {
        drawRoundRect(
            color = earColor,
            topLeft = Offset(cx - r * 1.38f, cy - r * 0.55f),
            size = Size(r * 0.68f, r * 1.35f),
            cornerRadius = CornerRadius(r * 0.4f, r * 0.4f)
        )
    }
    rotate(degrees = 16f - wiggle * 7f, pivot = Offset(cx + r * 0.85f, cy - r * 0.55f)) {
        drawRoundRect(
            color = earColor,
            topLeft = Offset(cx + r * 0.70f, cy - r * 0.55f),
            size = Size(r * 0.68f, r * 1.35f),
            cornerRadius = CornerRadius(r * 0.4f, r * 0.4f)
        )
    }

    // Head
    drawCircle(fur, r * 0.98f, Offset(cx, cy))

    if (!isSilhouette) {
        // Cute eye patch
        drawCircle(earColor.copy(alpha = 0.65f), r * 0.34f, Offset(cx - r * 0.42f, cy - r * 0.12f))
        // Muzzle
        drawOval(
            color = muzzle,
            topLeft = Offset(cx - r * 0.56f, cy + r * 0.05f),
            size = Size(r * 1.12f, r * 0.72f)
        )
        // Happy pink tongue sticking out
        drawRoundRect(
            color = Color(0xFFFF758F),
            topLeft = Offset(cx - r * 0.17f, cy + r * 0.46f),
            size = Size(r * 0.34f, r * 0.36f + (if (isCelebrating) r * 0.08f else 0f)),
            cornerRadius = CornerRadius(r * 0.2f, r * 0.2f)
        )
        // Big shiny nose
        drawOval(
            color = Color(0xFF3A241A),
            topLeft = Offset(cx - r * 0.22f, cy + r * 0.04f),
            size = Size(r * 0.44f, r * 0.30f)
        )
        drawCircle(Color.White.copy(alpha = 0.5f), r * 0.06f, Offset(cx - r * 0.07f, cy + r * 0.13f))
    }

    drawHappySmile(Offset(cx, cy + r * 0.38f), r * 0.52f, s * 0.03f, isSilhouette)
    drawCuteEyes(
        Offset(cx - r * 0.40f, cy - r * 0.14f),
        Offset(cx + r * 0.40f, cy - r * 0.14f),
        r * 0.16f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.60f, cy + r * 0.22f),
        Offset(cx + r * 0.60f, cy + r * 0.22f),
        r * 0.14f,
        isSilhouette
    )
}

private fun DrawScope.drawCow(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val body = Color(0xFFFFFDF8).orShadow(isSilhouette)
    val spot = Color(0xFF3E3A39).orShadow(isSilhouette)
    val horn = Color(0xFFF4D35E).orShadow(isSilhouette)
    val snout = Color(0xFFFFB8C6).orShadow(isSilhouette)
    val r = s * 0.33f

    // Cute horns
    drawOval(horn, Offset(cx - r * 0.85f, cy - r * 1.18f), Size(r * 0.34f, r * 0.55f))
    drawOval(horn, Offset(cx + r * 0.51f, cy - r * 1.18f), Size(r * 0.34f, r * 0.55f))

    // Side ears
    rotate(degrees = -22f + wiggle * 6f, pivot = Offset(cx - r * 0.9f, cy - r * 0.45f)) {
        drawOval(body, Offset(cx - r * 1.48f, cy - r * 0.65f), Size(r * 0.68f, r * 0.38f))
    }
    rotate(degrees = 22f - wiggle * 6f, pivot = Offset(cx + r * 0.9f, cy - r * 0.45f)) {
        drawOval(body, Offset(cx + r * 0.80f, cy - r * 0.65f), Size(r * 0.68f, r * 0.38f))
    }

    // Face
    drawRoundRect(
        color = body,
        topLeft = Offset(cx - r * 0.92f, cy - r * 0.92f),
        size = Size(r * 1.84f, r * 1.65f),
        cornerRadius = CornerRadius(r * 0.72f, r * 0.72f)
    )

    if (!isSilhouette) {
        // Cow outline for light cream contrast
        drawRoundRect(
            color = Color(0xFFD9CFC1),
            topLeft = Offset(cx - r * 0.92f, cy - r * 0.92f),
            size = Size(r * 1.84f, r * 1.65f),
            cornerRadius = CornerRadius(r * 0.72f, r * 0.72f),
            style = Stroke(width = s * 0.018f)
        )
        // Signature cow patches
        drawCircle(spot, r * 0.34f, Offset(cx - r * 0.48f, cy - r * 0.42f))
        drawCircle(spot, r * 0.24f, Offset(cx + r * 0.56f, cy - r * 0.52f))
    }

    // Big pink snout
    drawOval(
        color = snout,
        topLeft = Offset(cx - r * 0.82f, cy + r * 0.08f),
        size = Size(r * 1.64f, r * 0.88f)
    )

    if (!isSilhouette) {
        drawOval(Color(0xFFD96B82), Offset(cx - r * 0.38f, cy + r * 0.32f), Size(r * 0.20f, r * 0.14f))
        drawOval(Color(0xFFD96B82), Offset(cx + r * 0.18f, cy + r * 0.32f), Size(r * 0.20f, r * 0.14f))
    }

    drawHappySmile(Offset(cx, cy + r * 0.58f), r * 0.48f, s * 0.028f, isSilhouette)
    drawCuteEyes(
        Offset(cx - r * 0.38f, cy - r * 0.16f),
        Offset(cx + r * 0.38f, cy - r * 0.16f),
        r * 0.15f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
}

private fun DrawScope.drawChicken(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val feather = Color(0xFFFFF8E1).orShadow(isSilhouette)
    val comb = Color(0xFFFF4D6D).orShadow(isSilhouette)
    val beak = Color(0xFFFFA62B).orShadow(isSilhouette)
    val wingColor = Color(0xFFFFE5A3).orShadow(isSilhouette)
    val r = s * 0.34f

    // Flapping cute wings
    rotate(degrees = -18f + wiggle * 10f, pivot = Offset(cx - r * 0.85f, cy + r * 0.15f)) {
        drawOval(wingColor, Offset(cx - r * 1.32f, cy - r * 0.10f), Size(r * 0.55f, r * 0.78f))
    }
    rotate(degrees = 18f - wiggle * 10f, pivot = Offset(cx + r * 0.85f, cy + r * 0.15f)) {
        drawOval(wingColor, Offset(cx + r * 0.77f, cy - r * 0.10f), Size(r * 0.55f, r * 0.78f))
    }

    // Red comb (jengger) on top
    drawCircle(comb, r * 0.24f, Offset(cx - r * 0.22f, cy - r * 0.96f))
    drawCircle(comb, r * 0.28f, Offset(cx, cy - r * 1.06f))
    drawCircle(comb, r * 0.22f, Offset(cx + r * 0.22f, cy - r * 0.94f))

    // Plump round body/head
    drawOval(
        color = feather,
        topLeft = Offset(cx - r * 0.95f, cy - r * 0.88f),
        size = Size(r * 1.90f, r * 1.85f)
    )
    if (!isSilhouette) {
        drawOval(
            color = Color(0xFFEBD5A7),
            topLeft = Offset(cx - r * 0.95f, cy - r * 0.88f),
            size = Size(r * 1.90f, r * 1.85f),
            style = Stroke(width = s * 0.018f)
        )
        // Red wattle under beak
        drawCircle(comb, r * 0.15f, Offset(cx - r * 0.10f, cy + r * 0.44f))
        drawCircle(comb, r * 0.15f, Offset(cx + r * 0.10f, cy + r * 0.44f))

        // Orange beak diamond
        val beakPath = Path().apply {
            moveTo(cx, cy + r * 0.04f)
            lineTo(cx + r * 0.26f, cy + r * 0.24f)
            lineTo(cx, cy + r * 0.44f)
            lineTo(cx - r * 0.26f, cy + r * 0.24f)
            close()
        }
        drawPath(beakPath, beak)
    }

    drawCuteEyes(
        Offset(cx - r * 0.38f, cy - r * 0.12f),
        Offset(cx + r * 0.38f, cy - r * 0.12f),
        r * 0.15f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.58f, cy + r * 0.18f),
        Offset(cx + r * 0.58f, cy + r * 0.18f),
        r * 0.14f,
        isSilhouette
    )
}

private fun DrawScope.drawLion(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val mane = Color(0xFFE76F2B).orShadow(isSilhouette)
    val face = Color(0xFFFFCB57).orShadow(isSilhouette)
    val muzzle = Color(0xFFFFF2CC).orShadow(isSilhouette)
    val r = s * 0.30f

    // Fluffy scalloped sunflower mane
    rotate(degrees = wiggle * 4f, pivot = Offset(cx, cy)) {
        val petals = 12
        for (i in 0 until petals) {
            val angle = i * (360.0 / petals) * PI / 180.0
            val px = cx + cos(angle).toFloat() * (r * 0.98f)
            val py = cy + sin(angle).toFloat() * (r * 0.98f)
            drawCircle(mane, r * 0.42f, Offset(px, py))
        }
    }

    // Round ears peeking out
    drawCircle(face, r * 0.32f, Offset(cx - r * 0.72f, cy - r * 0.72f))
    drawCircle(face, r * 0.32f, Offset(cx + r * 0.72f, cy - r * 0.72f))
    if (!isSilhouette) {
        drawCircle(Color(0xFFFF9E7D), r * 0.16f, Offset(cx - r * 0.72f, cy - r * 0.72f))
        drawCircle(Color(0xFFFF9E7D), r * 0.16f, Offset(cx + r * 0.72f, cy - r * 0.72f))
    }

    // Golden face
    drawCircle(face, r * 0.92f, Offset(cx, cy + r * 0.04f))

    if (!isSilhouette) {
        drawOval(muzzle, Offset(cx - r * 0.44f, cy + r * 0.14f), Size(r * 0.88f, r * 0.56f))
        val nose = Path().apply {
            moveTo(cx - r * 0.18f, cy + r * 0.18f)
            lineTo(cx + r * 0.18f, cy + r * 0.18f)
            lineTo(cx, cy + r * 0.36f)
            close()
        }
        drawPath(nose, Color(0xFF7B3E19))
    }

    drawHappySmile(Offset(cx, cy + r * 0.44f), r * 0.44f, s * 0.028f, isSilhouette)
    drawCuteEyes(
        Offset(cx - r * 0.38f, cy - r * 0.10f),
        Offset(cx + r * 0.38f, cy - r * 0.10f),
        r * 0.16f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.58f, cy + r * 0.22f),
        Offset(cx + r * 0.58f, cy + r * 0.22f),
        r * 0.14f,
        isSilhouette
    )
}

private fun DrawScope.drawElephant(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val skin = Color(0xFF8ECAE6).orShadow(isSilhouette)
    val earInner = Color(0xFFFFB5C2).orShadow(isSilhouette)
    val r = s * 0.30f

    // Giant flapping elephant ears
    rotate(degrees = wiggle * 6f, pivot = Offset(cx - r * 0.75f, cy)) {
        drawOval(skin, Offset(cx - r * 1.56f, cy - r * 0.75f), Size(r * 1.05f, r * 1.38f))
        if (!isSilhouette) {
            drawOval(earInner, Offset(cx - r * 1.38f, cy - r * 0.48f), Size(r * 0.68f, r * 0.88f))
        }
    }
    rotate(degrees = -wiggle * 6f, pivot = Offset(cx + r * 0.75f, cy)) {
        drawOval(skin, Offset(cx + r * 0.51f, cy - r * 0.75f), Size(r * 1.05f, r * 1.38f))
        if (!isSilhouette) {
            drawOval(earInner, Offset(cx + r * 0.70f, cy - r * 0.48f), Size(r * 0.68f, r * 0.88f))
        }
    }

    // Round head
    drawCircle(skin, r * 0.92f, Offset(cx, cy - r * 0.08f))

    // Cute curved trunk
    val trunkPath = Path().apply {
        moveTo(cx - r * 0.18f, cy + r * 0.12f)
        quadraticTo(cx - r * 0.12f, cy + r * 1.05f, cx + r * 0.42f + wiggle * 6f, cy + r * 0.92f)
    }
    drawPath(
        trunkPath,
        color = skin,
        style = Stroke(width = r * 0.38f, cap = StrokeCap.Round)
    )
    if (!isSilhouette) {
        drawPath(
            trunkPath,
            color = Color(0xFF69B3D6),
            style = Stroke(width = r * 0.07f, cap = StrokeCap.Round)
        )
    }

    drawCuteEyes(
        Offset(cx - r * 0.36f, cy - r * 0.18f),
        Offset(cx + r * 0.36f, cy - r * 0.18f),
        r * 0.16f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.58f, cy + r * 0.14f),
        Offset(cx + r * 0.58f, cy + r * 0.14f),
        r * 0.14f,
        isSilhouette
    )
}
