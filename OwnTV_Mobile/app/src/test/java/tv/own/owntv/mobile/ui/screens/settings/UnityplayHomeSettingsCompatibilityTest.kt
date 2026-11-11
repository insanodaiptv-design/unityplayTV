package tv.own.owntv.mobile.ui.screens.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import tv.own.owntv.core.model.HomeRow
import tv.own.owntv.mobile.R

class UnityplayHomeSettingsCompatibilityTest {
    @Test fun everyCoreHomeRowHasASettingsTitleAndDescription() {
        HomeRow.entries.forEach { row ->
            assertTrue(row.name, row.titleRes() != 0)
            assertTrue(row.name, row.descriptionRes() != 0)
        }
    }

    @Test fun favoriteMoviesAndSeriesKeepTheirOwnSettingsLabels() {
        assertEquals(R.string.home_row_favorite_movies, HomeRow.FAVORITE_MOVIES.titleRes())
        assertEquals(R.string.home_row_favorite_series, HomeRow.FAVORITE_SERIES.titleRes())
        assertEquals(R.string.home_row_favorite_movies_description, HomeRow.FAVORITE_MOVIES.descriptionRes())
        assertEquals(R.string.home_row_favorite_series_description, HomeRow.FAVORITE_SERIES.descriptionRes())
    }
}
