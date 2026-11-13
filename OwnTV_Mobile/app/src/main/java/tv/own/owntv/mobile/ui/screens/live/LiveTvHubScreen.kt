package tv.own.owntv.mobile.ui.screens.live

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.SharedFlow
import tv.own.owntv.mobile.R
import tv.own.owntv.mobile.ui.components.FilterChipRow
import tv.own.owntv.mobile.ui.nav.MobileDestination
import tv.own.owntv.mobile.ui.screens.guide.GuideScreen

/** The guide belongs to live television rather than taking a catalogue's bottom-bar slot. */
internal enum class LiveTvSection(@param:StringRes val labelRes: Int) {
    CHANNELS(R.string.settings_customize_categories),
    GUIDE(R.string.unityplay_live_guide),
}

@Composable
internal fun LiveTvSectionTabs(selected: LiveTvSection, onSelect: (LiveTvSection) -> Unit) {
    FilterChipRow(
        labels = LiveTvSection.entries.map { stringResource(it.labelRes) },
        selectedIndex = selected.ordinal,
        onSelect = { index -> LiveTvSection.entries.getOrNull(index)?.let(onSelect) },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun LiveTvHubScreen(
    scrollToTop: SharedFlow<String>,
    onOpenChannel: (Long, Boolean) -> Unit,
    onOpenPlayer: () -> Unit,
    onAddEpg: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Preserve the active section across rotation and trips to a channel's details.
    var section by rememberSaveable { mutableStateOf(LiveTvSection.CHANNELS) }
    var categoryPicker by rememberSaveable { mutableStateOf(false) }
    Column(modifier.fillMaxSize()) {
        LiveTvSectionTabs(selected = section, onSelect = {
            section = it
            categoryPicker = it == LiveTvSection.CHANNELS
        })
        Box(Modifier.weight(1f).fillMaxWidth()) {
            when (section) {
                LiveTvSection.CHANNELS -> LiveScreen(
                    scrollToTop = scrollToTop,
                    onOpenChannel = onOpenChannel,
                    onOpenPlayer = onOpenPlayer,
                    categoryPicker = categoryPicker,
                    onCategoryPickerChange = { categoryPicker = it },
                    modifier = Modifier.fillMaxSize(),
                )
                LiveTvSection.GUIDE -> GuideScreen(
                    scrollToTop = scrollToTop,
                    onOpenChannel = { channelId -> onOpenChannel(channelId, false) },
                    onAddEpg = onAddEpg,
                    scrollRoute = MobileDestination.LIVE.route,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
