package com.midnight.vpn.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ── Glass-morphism card modifier ───────────────────────────────────────────────
fun Modifier.glassCard(
    cornerRadius: Dp = 16.dp,
    borderAlpha: Float = 0.10f,
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                GlassWhite,
                Color.Transparent,
            ),
        ),
        shape = RoundedCornerShape(cornerRadius),
    )
    .background(
        color = MidnightCard.copy(alpha = 0.85f),
        shape = RoundedCornerShape(cornerRadius),
    )
    .border(
        width = 1.dp,
        color = Color.White.copy(alpha = borderAlpha),
        shape = RoundedCornerShape(cornerRadius),
    )

// ── Neon glow border (static) ──────────────────────────────────────────────────
fun Modifier.neonBorder(
    color: Color = NeonPurple,
    cornerRadius: Dp = 16.dp,
    glowRadius: Dp = 8.dp,
    borderWidth: Dp = 1.dp,
): Modifier = this
    .drawBehind {
        drawRoundRect(
            color = color.copy(alpha = 0.25f),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(width = glowRadius.toPx()),
        )
    }
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(color, color.copy(alpha = 0.5f)),
        ),
        shape = RoundedCornerShape(cornerRadius),
    )

// ── Animated pulsing neon glow ─────────────────────────────────────────────────
@Composable
fun Modifier.pulsingNeonGlow(
    color: Color = NeonPurple,
    cornerRadius: Dp = 50.dp,
    minAlpha: Float = 0.15f,
    maxAlpha: Float = 0.55f,
    durationMillis: Int = 2000,
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "neon_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow_alpha",
    )

    return this.drawBehind {
        drawRoundRect(
            color = color.copy(alpha = alpha),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(width = 12.dp.toPx()),
        )
        drawRoundRect(
            color = color.copy(alpha = alpha * 0.4f),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(width = 24.dp.toPx()),
        )
    }
}

// ── Gradient border brush ──────────────────────────────────────────────────────
fun Modifier.gradientBorder(
    colors: List<Color> = listOf(GradientPurpleStart, GradientPurpleEnd),
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
): Modifier = this.border(
    width = borderWidth,
    brush = Brush.linearGradient(colors),
    shape = RoundedCornerShape(cornerRadius),
)
