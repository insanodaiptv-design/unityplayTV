package tv.own.owntv.mobile.ui.theme

import androidx.compose.ui.text.font.FontFamily
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import tv.own.owntv.core.theme.AppFontFamily

class UnityplayFontCompatibilityTest {
    @Test fun everyStoredCoreFontHasAMobileFamilyAndSubtitlePolicy() {
        AppFontFamily.entries.forEach { font ->
            assertNotNull(font.name, font.asComposeFamily())
            assertTrue(font.name, font.subtitleFontResource >= 0)
        }
    }

    @Test fun unbundledJakartaPreferenceUsesSystemFallbackWithoutMissingResource() {
        assertEquals(FontFamily.SansSerif, AppFontFamily.PLUS_JAKARTA_SANS.asComposeFamily())
        assertEquals(0, AppFontFamily.PLUS_JAKARTA_SANS.subtitleFontResource)
    }
}
