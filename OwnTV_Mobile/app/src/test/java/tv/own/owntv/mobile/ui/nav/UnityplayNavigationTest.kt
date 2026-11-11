package tv.own.owntv.mobile.ui.nav

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Test
import tv.own.owntv.core.nav.MainSection
import tv.own.owntv.mobile.R

class UnityplayNavigationTest {
    @Test fun phoneNavigationOpensTheTwoCataloguesDirectlyInRequestedOrder() {
        assertEquals(listOf("home", "live", "movies", "series", "more"), MobileDestination.bottomBar.map { it.route })
        assertFalse(MobileDestination.bottomBar.contains(MobileDestination.LIBRARY))
        assertFalse(MobileDestination.bottomBar.contains(MobileDestination.GUIDE))
    }

    @Test fun landscapeUsesTheSameFiveDestinations() {
        assertEquals(MobileDestination.bottomBar, MobileDestination.rail)
    }

    @Test fun settingsLabelStillOpensTheExistingToolsHub() {
        assertSame(MobileDestination.MORE, MobileDestination.bottomBar.last())
        assertEquals("more", MobileDestination.bottomBar.last().route)
        assertEquals(R.string.unityplay_nav_settings, MobileDestination.MORE.labelRes)
    }

    @Test fun movieAndSeriesDestinationsKeepDistinctCatalogueScopes() {
        assertEquals(setOf(MainSection.MOVIES), MobileDestination.MOVIES.sections)
        assertEquals(setOf(MainSection.SERIES), MobileDestination.SERIES.sections)
    }
}
