package tv.own.owntv.features.live

/** First OK selects the in-pane channel; a second OK on that channel promotes it to fullscreen. */
internal fun shouldPreviewOnOk(previewEnabled: Boolean, armed: Boolean, playingId: Long?, pickedId: Long): Boolean =
    previewEnabled && (!armed || playingId != pickedId)

/** Play audio on a deliberate selection unless the viewer saved a different preference. */
internal fun previewAudioEnabled(preference: Boolean?, userConfirmed: Boolean): Boolean =
    preference ?: userConfirmed
