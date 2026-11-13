package tv.own.owntv.features.live
import org.junit.Assert.*
import org.junit.Test
class LiveBrowseSelectionTest {
    @Test fun firstOkStartsSmallPreview() { assertTrue(shouldPreviewOnOk(true, false, null, 42)) }
    @Test fun restoredChannelStillNeedsOk() { assertTrue(shouldPreviewOnOk(true, false, 42, 42)) }
    @Test fun secondOkOpensConfirmedChannelFullscreen() { assertFalse(shouldPreviewOnOk(true, true, 42, 42)) }
    @Test fun anotherChannelStartsInPreview() { assertTrue(shouldPreviewOnOk(true, true, 42, 43)) }
    @Test fun previewDisabledKeepsFullscreenEntry() { assertFalse(shouldPreviewOnOk(false, false, null, 42)) }

    @Test fun confirmedChannelHasAudioByDefault() {
        assertTrue(previewAudioEnabled(preference = null, userConfirmed = true))
    }

    @Test fun focusPreviewStaysSilentByDefault() {
        assertFalse(previewAudioEnabled(preference = null, userConfirmed = false))
    }

    @Test fun explicitAudioPreferenceOverridesBothDefaults() {
        assertFalse(previewAudioEnabled(preference = false, userConfirmed = true))
        assertTrue(previewAudioEnabled(preference = true, userConfirmed = false))
    }
}
