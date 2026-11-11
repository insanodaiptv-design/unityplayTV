package tv.own.owntv.mobile.ui.setup

import androidx.test.platform.app.InstrumentationRegistry
import tv.own.owntv.mobile.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import tv.own.owntv.core.sync.SyncScopeChoice
import tv.own.owntv.core.settings.PlaylistRefresh

class UnityplayOnboardingTest {
    @get:Rule val compose = createComposeRule()

    @Test fun welcomeShowsTheStackedBrandFromTheReference() {
        compose.setContent { MaterialTheme { WelcomeBrandHeader() } }
        compose.onNodeWithText("BEM-VINDO AO").assertIsDisplayed()
        compose.onNodeWithText("unityplayTV").assertIsDisplayed()
    }

    @Test fun firstRunHasExactlyFourFieldsAndImportsAllCataloguesWithExistingDefaults() {
        var submitted: List<String>? = null
        compose.setContent {
            MaterialTheme {
                AddSourceForm(
                    simpleSetup = true,
                    onStartXtream = { name, url, user, pass, ua, referer, refresh, live, movies, series, hls ->
                        submitted = listOf(name, url, user, pass)
                        assertEquals("", ua)
                        assertEquals("", referer)
                        assertEquals(PlaylistRefresh(), refresh)
                        assertEquals(SyncScopeChoice.Now, live)
                        assertEquals(SyncScopeChoice.Now, movies)
                        assertEquals(SyncScopeChoice.Now, series)
                        assertFalse(hls)
                    },
                    onStartM3u = { _, _, _, _, _ -> fail("Unexpected M3U import") },
                    onStartStalker = { _, _, _, _, _, _, _, _, _, _, _, _, _ -> fail("Unexpected portal import") },
                )
            }
        }
        val importLabel = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.setup_start_import)
        val fields = compose.onAllNodes(hasSetTextAction())
        fields.assertCountEquals(4)
        compose.onNodeWithText("UnityplayTV").assertIsDisplayed()
        val heading = compose.onNodeWithText("UnityplayTV").fetchSemanticsNode().boundsInRoot
        val playlist = compose.onNodeWithText("Lista de reprodução").fetchSemanticsNode().boundsInRoot
        assertTrue("Brand must remain above the playlist form", heading.bottom < playlist.top)
        // Empty credentials must never start an import.
        compose.onNodeWithText(importLabel).assertIsNotEnabled()
        listOf("Minha lista", "http://example.test:8080", "usuario", "segredo").forEachIndexed { index, value ->
            fields[index].performScrollTo().performTextInput(value)
        }
        compose.onNodeWithText(importLabel).performScrollTo().assertIsEnabled().performClick()
        compose.runOnIdle {
            assertEquals(listOf("Minha lista", "http://example.test:8080", "usuario", "segredo"), submitted)
        }
    }
}
