package org.delcom.pam_p4_ifs23031.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/* =========================
   STEAM DARK COLOR SCHEME
   ========================= */

private val DarkColors = darkColorScheme(
    primary = SteamAccent,
    onPrimary = Color.Black,

    primaryContainer = SteamBlueLight,
    onPrimaryContainer = Color.White,

    secondary = SteamAccent,
    onSecondary = Color.Black,

    background = SteamDarkBlue,
    onBackground = Color(0xFFE6EEF5),

    surface = SteamBlue,
    onSurface = Color(0xFFE6EEF5),

    surfaceVariant = SteamBlueLight,
    onSurfaceVariant = Color(0xFFC7D5E0),

    outline = Color(0xFF3A5468),

    error = Color(0xFFCF6679),
    onError = Color.Black
)

/* =========================
   STEAM LIGHT COLOR SCHEME
   ========================= */

private val LightColors = lightColorScheme(
    primary = SteamBlue,
    onPrimary = Color.White,

    primaryContainer = SteamBlueLight,
    onPrimaryContainer = Color.White,

    secondary = SteamAccent,
    onSecondary = Color.White,

    background = Color(0xFFF2F6FA),
    onBackground = SteamDarkBlue,

    surface = Color.White,
    onSurface = SteamDarkBlue,

    surfaceVariant = SteamBlueLight,
    onSurfaceVariant = Color(0xFF1B2838),

    outline = Color(0xFF3A5468),

    error = Color(0xFFCF6679),
    onError = Color.White
)

/* =========================
   APP THEME
   ========================= */

@Composable
fun DelcomTheme(
    darkTheme: Boolean = true, // Default Steam feel
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}