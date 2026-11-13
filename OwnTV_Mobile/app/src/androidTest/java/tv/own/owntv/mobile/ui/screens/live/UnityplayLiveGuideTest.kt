package tv.own.owntv.mobile.ui.screens.live

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UnityplayLiveGuideTest {
    @get:Rule val compose = createComposeRule()

    @Test fun categoriesAndGuideAreAvailableInsideLiveTv() {
        val selected = mutableStateOf(LiveTvSection.CHANNELS)
        compose.setContent {
            MaterialTheme {
                LiveTvSectionTabs(selected.value) { selected.value = it }
            }
        }
        compose.onNodeWithText("Categorias").assertIsDisplayed()
        compose.onNodeWithText("Guia de programação").assertIsDisplayed().performClick()
        compose.runOnIdle { assertEquals(LiveTvSection.GUIDE, selected.value) }
        compose.onNodeWithText("Categorias").performClick()
        compose.runOnIdle { assertEquals(LiveTvSection.CHANNELS, selected.value) }
    }
}
