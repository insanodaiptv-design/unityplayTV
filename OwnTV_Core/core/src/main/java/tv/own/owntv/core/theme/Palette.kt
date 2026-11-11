package tv.own.owntv.core.theme

/**
 * The one place OwnTV's colour values are written down, shared by the TV app and the mobile app.
 *
 * Values are ARGB longs rather than `androidx.compose.ui.graphics.Color` because core carries the
 * Compose *runtime* only — no `compose-ui` — and because the two apps build different colour
 * schemes from them (`androidx.tv.material3` on the TV, `androidx.compose.material3` here). Each
 * app wraps a value once: `Color(OwnTVPalette.DarkBackground)`.
 *
 * Dark uses a near-black background (#040e0b) so the panel colours pop against the deep dark
 * surface while keeping a subtle green undertone. NEUTRAL and the secondary/tertiary roles are
 * theme-only; the `primary` roles are seeded per [AccentColor] (default teal).
 *
 * The three `*RailPanel` / `*ContentPanel` / `*PreviewPanel` values are not part of the M3 ladder:
 * they are the shell's per-region colour identity, keeping the navigation, the content and the
 * detail pane distinct in both themes instead of collapsing into the greyer generic elevation
 * steps. Both apps draw those same three regions, so they belong here rather than in either one.
 */
object OwnTVPalette {

    /** Brand mark colour (the OwnTV play logo) — constant, and the default teal accent. */
    const val AccentCyan = 0xFFA446D2L

    // ---------------- DARK (M3 dark over near-black #040e0b) ----------------
    const val DarkBackground = 0xFF100B18L
    const val DarkSurface = 0xFF171020L
    const val DarkSurfaceContainerLowest = 0xFF0D0913L
    const val DarkSurfaceContainerLow = 0xFF20172CL
    const val DarkSurfaceContainer = 0xFF271D35L
    const val DarkSurfaceContainerHigh = 0xFF332640L
    const val DarkSurfaceContainerHighest = 0xFF41314FL
    const val DarkRailPanel = 0xFF1D1429L
    const val DarkContentPanel = 0xFF150E1FL
    const val DarkPreviewPanel = 0xFF251A32L
    const val DarkOnSurface = 0xFFF3EDF7L
    const val DarkOnSurfaceVariant = 0xFFD1C3DCL
    const val DarkOutline = 0xFFA295AEL
    const val DarkOutlineVariant = 0xFF4D3E59L
    const val DarkSecondary = 0xFFD69AECL
    const val DarkOnSecondary = 0xFF341044L
    const val DarkSecondaryContainer = 0xFF62217EL
    const val DarkOnSecondaryContainer = 0xFFF1D8FCL
    const val DarkTertiary = 0xFFD9A8EFL
    const val DarkOnTertiary = 0xFF351046L
    const val DarkTertiaryContainer = 0xFF5E2878L
    const val DarkOnTertiaryContainer = 0xFFF0DBFFL
    const val DarkError = 0xFFFFB4ABL

    // ---------------- LIGHT (M3 light) ----------------
    const val LightBackground = 0xFFFCF7FFL
    const val LightSurface = 0xFFFCF7FFL
    const val LightSurfaceContainerLowest = 0xFFFFFFFFL
    const val LightSurfaceContainerLow = 0xFFF7EFFBL
    const val LightSurfaceContainer = 0xFFF0E6F6L
    const val LightSurfaceContainerHigh = 0xFFEADDEEL
    const val LightSurfaceContainerHighest = 0xFFE2D4E9L
    const val LightRailPanel = 0xFFEEE2F5L
    const val LightContentPanel = 0xFFFAF4FEL
    const val LightPreviewPanel = 0xFFE8D9F0L
    const val LightOnSurface = 0xFF281A32L
    const val LightOnSurfaceVariant = 0xFF594663L
    const val LightOutline = 0xFF806B8AL
    const val LightOutlineVariant = 0xFFD1BEDBL
    const val LightSecondary = 0xFF81349FL
    const val LightOnSecondary = 0xFFFFFFFFL
    const val LightSecondaryContainer = 0xFFF1D8FCL
    const val LightOnSecondaryContainer = 0xFF2E0D3BL
    const val LightTertiary = 0xFF7E389CL
    const val LightOnTertiary = 0xFFFFFFFFL
    const val LightTertiaryContainer = 0xFFF0DBFFL
    const val LightOnTertiaryContainer = 0xFF2E0F3EL
    const val LightError = 0xFFBA1A1AL
}

/**
 * The four M3 primary roles for one accent on one theme, as ARGB longs.
 *
 * They come either from a preset's table ([roles]) or are generated from the user's custom hex
 * seed ([accentRolesFromSeed]).
 */
data class AccentRoleValues(
    val primary: Long,
    val onPrimary: Long,
    val primaryContainer: Long,
    val onPrimaryContainer: Long,
)

/**
 * The tonal palette each [AccentColor] seeds the M3 colour scheme with, for both themes (M3 uses
 * lighter tones on dark surfaces, darker tones on light).
 *
 * The display label belongs to whichever app is drawing the settings screen, so it is not here.
 */
private class AccentPalette(val dark: AccentRoleValues, val light: AccentRoleValues)

private val TealPalette = AccentPalette(
    dark = AccentRoleValues(0xFF52DBC8L, 0xFF003730L, 0xFF004F46L, 0xFF6FF8E4L),
    light = AccentRoleValues(0xFF006B5EL, 0xFFFFFFFFL, 0xFF6FF8E4L, 0xFF00201BL),
)

private val BluePalette = AccentPalette(
    dark = AccentRoleValues(0xFF6FB0FFL, 0xFF00315CL, 0xFF134A7CL, 0xFFD3E4FFL),
    light = AccentRoleValues(0xFF1565C0L, 0xFFFFFFFFL, 0xFFD6E3FFL, 0xFF001C3AL),
)

private val VioletPalette = AccentPalette(
    dark = AccentRoleValues(0xFFA446D2L, 0xFFFFFFFFL, 0xFF503060L, 0xFFF0DBFFL),
    light = AccentRoleValues(0xFFA446D2L, 0xFFFFFFFFL, 0xFFF0DBFFL, 0xFF2D153BL),
)

private val GreenPalette = AccentPalette(
    dark = AccentRoleValues(0xFF6FDB94L, 0xFF00391CL, 0xFF1F5135L, 0xFF8BF8AFL),
    light = AccentRoleValues(0xFF1B6B3FL, 0xFFFFFFFFL, 0xFFA6F2C0L, 0xFF00210FL),
)

private val AmberPalette = AccentPalette(
    dark = AccentRoleValues(0xFFFFB95CL, 0xFF452B00L, 0xFF624000L, 0xFFFFDDB3L),
    light = AccentRoleValues(0xFF8A5100L, 0xFFFFFFFFL, 0xFFFFDDB3L, 0xFF2C1600L),
)

/** The preset's four primary-role values for the given theme. */
fun AccentColor.roles(isDark: Boolean): AccentRoleValues {
    val palette = when (this) {
        AccentColor.TEAL -> TealPalette
        AccentColor.BLUE -> BluePalette
        AccentColor.VIOLET -> VioletPalette
        AccentColor.GREEN -> GreenPalette
        AccentColor.AMBER -> AmberPalette
    }
    return if (isDark) palette.dark else palette.light
}

/** Parses "#RRGGBB" / "RRGGBB" (also 8-digit AARRGGBB) into an ARGB long; null when invalid. */
fun parseAccentHex(hex: String): Long? {
    val s = hex.trim().removePrefix("#")
    return runCatching {
        when (s.length) {
            6 -> 0xFF000000L or s.toLong(16)
            8 -> s.toLong(16)
            else -> null
        }
    }.getOrNull()
}

/**
 * Generate tonal primary roles from an arbitrary seed colour (the custom hex accent).
 * The seed is used EXACTLY as `primary` so the user's hex renders true; only the supporting
 * contrast roles (onPrimary / containers) are derived by nudging the seed's lightness.
 */
fun accentRolesFromSeed(seed: Long, isDark: Boolean): AccentRoleValues {
    val argb = seed.toInt()
    // Choose a readable foreground for text/icons drawn on top of the exact seed colour.
    val onPrimary = if (androidx.core.graphics.ColorUtils.calculateLuminance(argb) > 0.5) {
        0xFF000000L
    } else {
        0xFFFFFFFFL
    }
    return if (isDark) {
        AccentRoleValues(seed, onPrimary, argb.withLightness(0.26f), argb.withLightness(0.90f))
    } else {
        AccentRoleValues(seed, onPrimary, argb.withLightness(0.88f), argb.withLightness(0.10f))
    }
}

/** Keep the seed's hue/saturation but pin the HSL lightness — a cheap stand-in for M3 tones. */
private fun Int.withLightness(l: Float): Long {
    val hsl = FloatArray(3)
    androidx.core.graphics.ColorUtils.colorToHSL(this, hsl)
    hsl[2] = l
    return androidx.core.graphics.ColorUtils.HSLToColor(hsl).toLong() and 0xFFFFFFFFL
}
