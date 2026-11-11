package tv.own.owntv.mobile.ui.screens.home

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import tv.own.owntv.core.home.HomeFeed
import tv.own.owntv.core.model.HomeRow

class UnityplayHomeCompatibilityTest {
    @Test fun emptyHomeDoesNotDrawHeadingsForAnyCoreRow() {
        val feed = HomeFeed()
        HomeRow.entries.forEach { row -> assertFalse(row.name, feed.hasContent(row)) }
    }

    @Test fun emptyFavoriteCataloguesProduceNoPosters() {
        val feed = HomeFeed()
        assertTrue(feed.favoritePosters(HomeRow.FAVORITE_MOVIES).isEmpty())
        assertTrue(feed.favoritePosters(HomeRow.FAVORITE_SERIES).isEmpty())
    }

    @Test fun otherHomeRowsAreNotAccidentallyShownAsFavoriteCatalogues() {
        val feed = HomeFeed()
        HomeRow.entries.filter { it != HomeRow.FAVORITE_MOVIES && it != HomeRow.FAVORITE_SERIES }
            .forEach { row -> assertTrue(row.name, feed.favoritePosters(row).isEmpty()) }
    }
}
