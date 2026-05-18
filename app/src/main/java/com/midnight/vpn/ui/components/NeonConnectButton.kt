package com.midnight.vpn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.midnight.vpn.domain.model.ConnectionState
import com.midnight.vpn.ui.theme.CyberBlue
import com.midnight.vpn.ui.theme.InterFontFamily
import com.midnight.vpn.ui.theme.NeonPurple
import com.midnight.vpn.ui.theme.NeonPurpleGlow
import com.midnight.vpn.ui.theme.StatusConnected
import com.midnight.vpn.ui.theme.StatusConnecting
import com.midnight.vpn.ui.theme.StatusDisconnected
import com.midnight.vpn.ui.theme.TextPrimary

@Composable
fun NeonConnectButton(
    connectionState: ConnectionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button_pulse")

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = when (connectionState) {
            ConnectionState.CONNECTED -> 0.8f
            ConnectionState.CONNECTING, ConnectionState.DISCONNECTING -> 0.6f
            else -> 0.4f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (connectionState) {
                    ConnectionState.CONNECTING, ConnectionState.DISCONNECTING -> 800
                    else -> 2000
                },
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse_alpha",
    )

    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ring_rotation",
    )

    val primaryColor by animateColorAsState(
        targetValue = when (connectionState) {
            ConnectionState.CONNECTED -> StatusConnected
            ConnectionState.CONNECTING, ConnectionState.DISCONNECTING -> StatusConnecting
            ConnectionState.ERROR -> StatusDisconnected
            ConnectionState.DISCONNECTED -> NeonPurple
        },
        animationSpec = tween(500),
        label = "button_color",
    )

    val buttonText = when (connectionState) {
        ConnectionState.DISCONNECTED -> "CONNECT"
        ConnectionState.CONNECTING -> "CONNECTING"
        ConnectionState.CONNECTED -> "CONNECTED"
        ConnectionState.DISCONNECTING -> "STOPPING"
        ConnectionState.ERROR -> "ERROR"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(200.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
    ) {
        // Outer glow ring
        Canvas(modifier = Modifier.size(200.dp)) {
            val radius = size.minDimension / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Glow layers
            drawCircle(
                color = primaryColor.copy(alpha = pulseAlpha * 0.15f),
                radius = radius,
                center = center,
            )
            drawCircle(
                color = primaryColor.copy(alpha = pulseAlpha * 0.25f),
                radius = radius * 0.85f,
                center = center,
            )

            // Main ring
            drawCircle(
                color = primaryColor.copy(alpha = pulseAlpha),
                radius = radius * 0.75f,
                center = center,
                style = Stroke(width = 3.dp.toPx()),
            )

            // Inner ring
            drawCircle(
                color = primaryColor.copy(alpha = 0.3f),
                radius = radius * 0.65f,
                center = center,
                style = Stroke(width = 1.dp.toPx()),
            )

            // Spinning arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Transparent,
                        primaryColor.copy(alpha = pulseAlpha),
                        Color.Transparent,
                    ),
                ),
                startAngle = ringRotation,
                sweepAngle = 90f,
                useCenter = false,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
            )
        }

        // Power icon / text
        Text(
            text = buttonText,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = primaryColor,
            letterSpacing = 2.sp,
        )
    }
}
