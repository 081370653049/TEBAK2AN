package com.example.ui.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

internal fun DrawScope.drawMonkey(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val fur = Color(0xFF9C5A2C).orShadow(isSilhouette)
    val faceSkin = Color(0xFFFFDFBA).orShadow(isSilhouette)
    val r = s * 0.32f

    // Big round ears
    drawCircle(fur, r * 0.42f, Offset(cx - r * 0.96f, cy - r * 0.08f + wiggle * 3f))
    drawCircle(fur, r * 0.42f, Offset(cx + r * 0.96f, cy - r * 0.08f - wiggle * 3f))
    if (!isSilhouette) {
        drawCircle(faceSkin, r * 0.24f, Offset(cx - r * 0.96f, cy - r * 0.08f + wiggle * 3f))
        drawCircle(faceSkin, r * 0.24f, Offset(cx + r * 0.96f, cy - r * 0.08f - wiggle * 3f))
    }

    // Little hair tuft on top
    drawOval(fur, Offset(cx - r * 0.18f, cy - r * 1.18f), Size(r * 0.36f, r * 0.42f))

    // Head
    drawCircle(fur, r * 0.95f, Offset(cx, cy))

    if (!isSilhouette) {
        // Heart-shaped peach face mask
        drawCircle(faceSkin, r * 0.46f, Offset(cx - r * 0.30f, cy - r * 0.16f))
        drawCircle(faceSkin, r * 0.46f, Offset(cx + r * 0.30f, cy - r * 0.16f))
        drawOval(faceSkin, Offset(cx - r * 0.68f, cy - r * 0.02f), Size(r * 1.36f, r * 0.84f))

        // Tiny nostrils
        drawCircle(Color(0xFF6B3E26), r * 0.06f, Offset(cx - r * 0.10f, cy + r * 0.16f))
        drawCircle(Color(0xFF6B3E26), r * 0.06f, Offset(cx + r * 0.10f, cy + r * 0.16f))
    }

    drawHappySmile(Offset(cx, cy + r * 0.38f), r * 0.62f, s * 0.03f, isSilhouette)
    drawCuteEyes(
        Offset(cx - r * 0.34f, cy - r * 0.16f),
        Offset(cx + r * 0.34f, cy - r * 0.16f),
        r * 0.16f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.56f, cy + r * 0.22f),
        Offset(cx + r * 0.56f, cy + r * 0.22f),
        r * 0.14f,
        isSilhouette
    )
}

internal fun DrawScope.drawRabbit(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val fur = Color(0xFFFFFAFC).orShadow(isSilhouette)
    val earInner = Color(0xFFFFB5C9).orShadow(isSilhouette)
    val r = s * 0.29f
    val headCy = cy + r * 0.32f

    // Long adorable bunny ears
    rotate(degrees = -12f + wiggle * 6f, pivot = Offset(cx - r * 0.42f, headCy - r * 0.6f)) {
        drawOval(fur, Offset(cx - r * 0.72f, headCy - r * 2.10f), Size(r * 0.58f, r * 1.62f))
        if (!isSilhouette) {
            drawOval(
                Color(0xFFE7D7DE),
                Offset(cx - r * 0.72f, headCy - r * 2.10f),
                Size(r * 0.58f, r * 1.62f),
                style = Stroke(width = s * 0.016f)
            )
            drawOval(earInner, Offset(cx - r * 0.58f, headCy - r * 1.88f), Size(r * 0.30f, r * 1.18f))
        }
    }
    rotate(degrees = 12f - wiggle * 6f, pivot = Offset(cx + r * 0.42f, headCy - r * 0.6f)) {
        drawOval(fur, Offset(cx + r * 0.14f, headCy - r * 2.10f), Size(r * 0.58f, r * 1.62f))
        if (!isSilhouette) {
            drawOval(
                Color(0xFFE7D7DE),
                Offset(cx + r * 0.14f, headCy - r * 2.10f),
                Size(r * 0.58f, r * 1.62f),
                style = Stroke(width = s * 0.016f)
            )
            drawOval(earInner, Offset(cx + r * 0.28f, headCy - r * 1.88f), Size(r * 0.30f, r * 1.18f))
        }
    }

    // Chubby bunny head
    drawOval(fur, Offset(cx - r * 1.02f, headCy - r * 0.85f), Size(r * 2.04f, r * 1.72f))
    if (!isSilhouette) {
        drawOval(
            Color(0xFFE7D7DE),
            Offset(cx - r * 1.02f, headCy - r * 0.85f),
            Size(r * 2.04f, r * 1.72f),
            style = Stroke(width = s * 0.018f)
        )
        // Little white bunny teeth
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(cx - r * 0.14f, headCy + r * 0.36f),
            size = Size(r * 0.28f, r * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        // Pink nose
        drawOval(Color(0xFFFF6B95), Offset(cx - r * 0.14f, headCy + r * 0.10f), Size(r * 0.28f, r * 0.18f))
    }

    drawHappySmile(Offset(cx, headCy + r * 0.32f), r * 0.44f, s * 0.026f, isSilhouette)
    drawCuteEyes(
        Offset(cx - r * 0.38f, headCy - r * 0.12f),
        Offset(cx + r * 0.38f, headCy - r * 0.12f),
        r * 0.16f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.62f, headCy + r * 0.18f),
        Offset(cx + r * 0.62f, headCy + r * 0.18f),
        r * 0.15f,
        isSilhouette
    )
}

internal fun DrawScope.drawDuck(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val yellow = Color(0xFFFFD93D).orShadow(isSilhouette)
    val wing = Color(0xFFF6BD22).orShadow(isSilhouette)
    val bill = Color(0xFFFF8826).orShadow(isSilhouette)
    val r = s * 0.33f

    // Cute hair feather tuft
    drawOval(yellow, Offset(cx - r * 0.14f, cy - r * 1.15f), Size(r * 0.28f, r * 0.40f))
    drawOval(yellow, Offset(cx + r * 0.04f, cy - r * 1.06f), Size(r * 0.24f, r * 0.34f))

    // Flapping duck wings
    rotate(degrees = -20f + wiggle * 12f, pivot = Offset(cx - r * 0.85f, cy + r * 0.15f)) {
        drawOval(wing, Offset(cx - r * 1.32f, cy - r * 0.08f), Size(r * 0.56f, r * 0.72f))
    }
    rotate(degrees = 20f - wiggle * 12f, pivot = Offset(cx + r * 0.85f, cy + r * 0.15f)) {
        drawOval(wing, Offset(cx + r * 0.76f, cy - r * 0.08f), Size(r * 0.56f, r * 0.72f))
    }

    // Round sunny duck head/body
    drawCircle(yellow, r * 0.94f, Offset(cx, cy))

    // Wide cheerful orange duck bill
    drawOval(bill, Offset(cx - r * 0.42f, cy + r * 0.14f), Size(r * 0.84f, r * 0.42f))
    if (!isSilhouette) {
        drawLine(
            color = Color(0xFFD9640A),
            start = Offset(cx - r * 0.32f, cy + r * 0.35f),
            end = Offset(cx + r * 0.32f, cy + r * 0.35f),
            strokeWidth = s * 0.022f,
            cap = StrokeCap.Round
        )
    }

    drawCuteEyes(
        Offset(cx - r * 0.38f, cy - r * 0.14f),
        Offset(cx + r * 0.38f, cy - r * 0.14f),
        r * 0.16f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.62f, cy + r * 0.16f),
        Offset(cx + r * 0.62f, cy + r * 0.16f),
        r * 0.14f,
        isSilhouette
    )
}

internal fun DrawScope.drawFrog(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val green = Color(0xFF6BCB5B).orShadow(isSilhouette)
    val belly = Color(0xFFD8F5A2).orShadow(isSilhouette)
    val r = s * 0.33f
    val headCy = cy + r * 0.16f

    // Big popping frog eye bumps on top
    drawCircle(green, r * 0.40f, Offset(cx - r * 0.56f, headCy - r * 0.78f))
    drawCircle(green, r * 0.40f, Offset(cx + r * 0.56f, headCy - r * 0.78f))
    if (!isSilhouette) {
        drawCircle(Color.White, r * 0.27f, Offset(cx - r * 0.56f, headCy - r * 0.78f))
        drawCircle(Color.White, r * 0.27f, Offset(cx + r * 0.56f, headCy - r * 0.78f))
    }

    // Wide smiling frog face
    drawOval(green, Offset(cx - r * 1.06f, headCy - r * 0.68f), Size(r * 2.12f, r * 1.48f))

    if (!isSilhouette) {
        // Light green chin
        drawOval(belly, Offset(cx - r * 0.70f, headCy + r * 0.16f), Size(r * 1.40f, r * 0.54f))
        // Tiny nostrils
        drawCircle(Color(0xFF2D6A30), r * 0.055f, Offset(cx - r * 0.14f, headCy - r * 0.08f))
        drawCircle(Color(0xFF2D6A30), r * 0.055f, Offset(cx + r * 0.14f, headCy - r * 0.08f))
    }

    drawHappySmile(Offset(cx, headCy + r * 0.16f), r * 0.92f, s * 0.032f, isSilhouette)
    drawCuteEyes(
        Offset(cx - r * 0.56f, headCy - r * 0.78f),
        Offset(cx + r * 0.56f, headCy - r * 0.78f),
        r * 0.16f,
        isBlinking,
        isCelebrating,
        isSilhouette
    )
    drawRosyCheeks(
        Offset(cx - r * 0.72f, headCy + r * 0.14f + wiggle * 1.5f),
        Offset(cx + r * 0.72f, headCy + r * 0.14f - wiggle * 1.5f),
        r * 0.15f,
        isSilhouette
    )
}

internal fun DrawScope.drawFish(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val body = Color(0xFFFF7B54).orShadow(isSilhouette)
    val stripe = Color(0xFFFFF5E4).orShadow(isSilhouette)
    val fin = Color(0xFFFFB26B).orShadow(isSilhouette)
    val r = s * 0.34f

    // Wiggling tail fin on the right
    rotate(degrees = wiggle * 10f, pivot = Offset(cx + r * 0.72f, cy)) {
        val tail = Path().apply {
            moveTo(cx + r * 0.65f, cy)
            lineTo(cx + r * 1.38f, cy - r * 0.62f)
            quadraticTo(cx + r * 1.15f, cy, cx + r * 1.38f, cy + r * 0.62f)
            close()
        }
        drawPath(tail, fin)
    }

    // Top dorsal fin & bottom fin
    val topFin = Path().apply {
        moveTo(cx - r * 0.35f, cy - r * 0.65f)
        quadraticTo(cx + r * 0.10f, cy - r * 1.18f, cx + r * 0.45f, cy - r * 0.60f)
        close()
    }
    drawPath(topFin, fin)
    drawOval(fin, Offset(cx - r * 0.20f, cy + r * 0.52f), Size(r * 0.48f, r * 0.34f))

    // Plump clownfish body
    drawOval(body, Offset(cx - r * 1.05f, cy - r * 0.74f), Size(r * 1.85f, r * 1.48f))

    if (!isSilhouette) {
        // White clownfish stripes
        drawRoundRect(
            color = stripe,
            topLeft = Offset(cx - r * 0.12f, cy - r * 0.68f),
            size = Size(r * 0.24f, r * 1.36f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = stripe,
            topLeft = Offset(cx + r * 0.34f, cy - r * 0.52f),
            size = Size(r * 0.18f, r * 1.04f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        // Little bubbles in front of fish
        val bubbleColor = Color(0xFF7BDFF2).copy(alpha = 0.78f)
        drawCircle(bubbleColor, r * 0.12f, Offset(cx - r * 1.24f, cy - r * 0.35f + wiggle * 4f))
        drawCircle(bubbleColor, r * 0.08f, Offset(cx - r * 1.16f, cy - r * 0.68f - wiggle * 3f))
    }

    // Single cute side-facing big eye + smile
    if (!isSilhouette) {
        val eyeC = Offset(cx - r * 0.52f, cy - r * 0.12f)
        if (isBlinking) {
            drawLine(
                Color(0xFF2D221E),
                Offset(eyeC.x - r * 0.15f, eyeC.y),
                Offset(eyeC.x + r * 0.15f, eyeC.y),
                strokeWidth = s * 0.03f,
                cap = StrokeCap.Round
            )
        } else {
            drawCircle(Color.White, r * 0.24f, eyeC)
            drawCircle(Color(0xFF2D221E), r * 0.15f, eyeC)
            drawCircle(Color.White, r * 0.055f, Offset(eyeC.x - r * 0.05f, eyeC.y - r * 0.05f))
        }
        drawCircle(Color(0xFFFF8BA7).copy(alpha = 0.55f), r * 0.13f, Offset(cx - r * 0.56f, cy + r * 0.20f))
        val smile = Path().apply {
            moveTo(cx - r * 0.96f, cy + r * 0.16f)
            quadraticTo(cx - r * 0.78f, cy + r * 0.34f, cx - r * 0.66f, cy + r * 0.20f)
        }
        drawPath(smile, Color(0xFF3A2820), style = Stroke(width = s * 0.028f, cap = StrokeCap.Round))
    }
}

internal fun DrawScope.drawWhale(
    cx: Float,
    cy: Float,
    s: Float,
    wiggle: Float,
    isBlinking: Boolean,
    isCelebrating: Boolean,
    isSilhouette: Boolean
) {
    val blue = Color(0xFF3FA7D6).orShadow(isSilhouette)
    val belly = Color(0xFFD8F3FF).orShadow(isSilhouette)
    val spout = Color(0xFF8BE8FF).orShadow(isSilhouette)
    val r = s * 0.32f
    val bodyCy = cy + r * 0.18f

    // Cute water spout on top
    val spoutH = r * (0.58f + (if (isCelebrating) 0.15f else 0f) + wiggle * 0.06f)
    val leftSpout = Path().apply {
        moveTo(cx - r * 0.18f, bodyCy - r * 0.72f)
        quadraticTo(cx - r * 0.25f, bodyCy - r * 0.75f - spoutH, cx - r * 0.62f, bodyCy - r * 0.68f - spoutH * 0.7f)
    }
    val rightSpout = Path().apply {
        moveTo(cx - r * 0.18f, bodyCy - r * 0.72f)
        quadraticTo(cx - r * 0.05f, bodyCy - r * 0.75f - spoutH, cx + r * 0.28f, bodyCy - r * 0.68f - spoutH * 0.7f)
    }
    drawPath(leftSpout, spout, style = Stroke(width = r * 0.14f, cap = StrokeCap.Round))
    drawPath(rightSpout, spout, style = Stroke(width = r * 0.14f, cap = StrokeCap.Round))

    // Whale tail on right
    rotate(degrees = wiggle * 8f, pivot = Offset(cx + r * 0.78f, bodyCy + r * 0.10f)) {
        val tail = Path().apply {
            moveTo(cx + r * 0.65f, bodyCy + r * 0.20f)
            quadraticTo(cx + r * 1.10f, bodyCy + r * 0.05f, cx + r * 1.15f, bodyCy - r * 0.45f)
            lineTo(cx + r * 0.90f, bodyCy - r * 0.35f)
            lineTo(cx + r * 1.38f, bodyCy - r * 0.55f)
            quadraticTo(cx + r * 1.32f, bodyCy + r * 0.32f, cx + r * 0.70f, bodyCy + r * 0.42f)
            close()
        }
        drawPath(tail, blue)
    }

    // Round whale body
    drawRoundRect(
        color = blue,
        topLeft = Offset(cx - r * 1.10f, bodyCy - r * 0.78f),
        size = Size(r * 1.95f, r * 1.44f),
        cornerRadius = CornerRadius(r * 0.74f, r * 0.74f)
    )

    if (!isSilhouette) {
        // Light blue belly patch
        drawRoundRect(
            color = belly,
            topLeft = Offset(cx - r * 1.02f, bodyCy + r * 0.18f),
            size = Size(r * 1.35f, r * 0.44f),
            cornerRadius = CornerRadius(r * 0.30f, r * 0.30f)
        )
        // Friendly eye
        val eyeC = Offset(cx - r * 0.54f, bodyCy - r * 0.06f)
        if (isBlinking) {
            drawLine(
                Color(0xFF2D221E),
                Offset(eyeC.x - r * 0.14f, eyeC.y),
                Offset(eyeC.x + r * 0.14f, eyeC.y),
                strokeWidth = s * 0.028f,
                cap = StrokeCap.Round
            )
        } else {
            drawCircle(Color(0xFF1F2D3D), r * 0.16f, eyeC)
            drawCircle(Color.White, r * 0.055f, Offset(eyeC.x - r * 0.05f, eyeC.y - r * 0.05f))
        }
        drawCircle(Color(0xFFFF8BA7).copy(alpha = 0.55f), r * 0.14f, Offset(cx - r * 0.68f, bodyCy + r * 0.16f))
        val smile = Path().apply {
            moveTo(cx - r * 1.02f, bodyCy + r * 0.24f)
            quadraticTo(cx - r * 0.66f, bodyCy + r * 0.44f, cx - r * 0.34f, bodyCy + r * 0.24f)
        }
        drawPath(smile, Color(0xFF1F2D3D), style = Stroke(width = s * 0.028f, cap = StrokeCap.Round))
    }
}
