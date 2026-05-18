package com.midnight.vpn.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Midnight dark colour scheme ────────────────────────────────────────────────
private val MidnightColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = TextPrimary,
    primaryContainer = NeonPurpleDark,
    onPrimaryContainer = TextPrimary,
    secondary = CyberBlue,
    onSecondary = MidnightBlack,
    secondaryContainer = CyberBlueDark,
    onSecondaryContainer = TextPrimary,
    tertiary = NeonPurpleLight,
    onTertiary = MidnightBlack,
    background = MidnightBlack,
    onBackground = TextPrimary,
    surface = MidnightSurface,
    onSurface = TextPrimary,
    surfaceVariant = MidnightCard,
    onSurfaceVariant = TextSecondary,
    outline = MidnightCardBorder,
    outlineVariant = GlassBorder,
    error = StatusDisconnected,
    onError = TextPrimary,
)

@Composable
fun MidnightVPNTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = MidnightBlack.toArgb()
            window.navigationBarColor = MidnightBlack.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = MidnightColorScheme,
        typography = MidnightTypography,
        content = content,
    )
}
