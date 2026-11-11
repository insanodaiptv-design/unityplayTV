package tv.own.owntv.mobile.ui.nav

import tv.own.owntv.mobile.ui.components.MobileIcons
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import tv.own.owntv.core.nav.MainSection
import tv.own.owntv.mobile.R

/**
 * Where the phone can navigate to.
 *
 * [sections] describes each destination's catalogue scope. The shell keeps the five navigation
 * buttons stable while content loads. Legacy Library and Guide routes remain valid for saved state.
 *
 * MORE has no section: it is always reachable, because Settings, Profiles and Downloads live behind
 * it and a user must never be able to hide their way out of the app's own settings.
 */
enum class MobileDestination(
    val route: String,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
    val sections: Set<MainSection>,
) {
    HOME("home", R.string.unityplay_nav_home, MobileIcons.Home, setOf(MainSection.HOME)),
    LIVE("live", R.string.unityplay_nav_live, MobileIcons.LiveTv, setOf(MainSection.LIVE_TV)),
    LIBRARY(
        "library",
        R.string.common_nav_library,
        MobileIcons.VideoLibrary,
        setOf(MainSection.MOVIES, MainSection.SERIES),
    ),
    GUIDE("guide", R.string.common_nav_guide, MobileIcons.CalendarMonth, setOf(MainSection.EPG)),
    MORE("more", R.string.unityplay_nav_settings, MobileIcons.Settings, emptySet()),

    // Dedicated catalogue destinations shared by the bottom bar and rail.
    MOVIES("movies", R.string.unityplay_nav_movies, MobileIcons.Movie, setOf(MainSection.MOVIES)),
    SERIES("series", R.string.unityplay_nav_series, MobileIcons.Theaters, setOf(MainSection.SERIES)),
    DOWNLOADS(
        "downloads",
        R.string.common_nav_downloads,
        MobileIcons.Download,
        setOf(MainSection.DOWNLOADS),
    ),
    SETTINGS("settings", R.string.common_nav_settings, MobileIcons.Settings, emptySet());

    companion object {
        /** Direct catalogue access; More keeps its existing tools behind the Settings label. */
        val bottomBar: List<MobileDestination> = listOf(HOME, LIVE, MOVIES, SERIES, MORE)

        /** The same navigation when a phone rotates or a wider screen uses the rail. */
        val rail: List<MobileDestination> = bottomBar

        /**
         * Filters a bar or rail to what the active playlist actually offers. A destination with no
         * sections is always kept; the rest survive if core says any of their sections is visible.
         */
        fun List<MobileDestination>.visible(sections: Set<MainSection>): List<MobileDestination> =
            filter { it.sections.isEmpty() || it.sections.any { section -> section in sections } }
    }
}
