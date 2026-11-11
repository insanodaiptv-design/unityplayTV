package tv.own.owntv.core.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.pow

/** Brand contract shared by Android TV and Mobile. */
class UnityplayBrandTest {
    @Test fun lilacIsTheExactDarkAccent() {
        assertEquals(0xFFA446D2L, AccentColor.VIOLET.roles(true).primary)
        assertEquals(0xFFA446D2L, OwnTVPalette.AccentCyan)
        assertEquals(0xFFD69AECL, OwnTVPalette.DarkSecondary)
    }

    @Test fun primaryLabelsStayReadableInBothThemes() {
        for (dark in listOf(true, false)) {
            val roles = AccentColor.VIOLET.roles(dark)
            assertTrue("Primary label contrast must reach 4.5:1", contrast(roles.primary, roles.onPrimary) >= 4.5)
            assertTrue("Container label contrast must reach 4.5:1", contrast(roles.primaryContainer, roles.onPrimaryContainer) >= 4.5)
        }
    }

    @Test fun sharedBackgroundsKeepReadableText() {
        assertTrue(contrast(OwnTVPalette.DarkBackground, OwnTVPalette.DarkOnSurface) >= 7.0)
        assertTrue(contrast(OwnTVPalette.LightBackground, OwnTVPalette.LightOnSurface) >= 7.0)
    }

    private fun luminance(argb: Long): Double {
        fun channel(shift: Int): Double {
            val c = ((argb shr shift) and 255).toDouble() / 255.0
            return if (c <= 0.04045) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * channel(16) + 0.7152 * channel(8) + 0.0722 * channel(0)
    }
    private fun contrast(a: Long, b: Long): Double {
        val x = luminance(a); val y = luminance(b)
        return (maxOf(x, y) + 0.05) / (minOf(x, y) + 0.05)
    }
}
