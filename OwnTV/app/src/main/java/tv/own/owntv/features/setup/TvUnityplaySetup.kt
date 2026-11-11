package tv.own.owntv.features.setup

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.tv.material3.Text
import tv.own.owntv.R
import tv.own.owntv.features.settings.StageFieldRow
import tv.own.owntv.features.settings.SettingHelp
import tv.own.owntv.ui.components.BrandLockup
import tv.own.owntv.ui.components.OwnTVIcon
import tv.own.owntv.ui.theme.mpx
import tv.own.owntv.ui.theme.stageAccent
import tv.own.owntv.ui.theme.stageText

internal object TvSetupPolicy {
    fun canImport(server: String, username: String, password: String) =
        server.isNotBlank() && username.isNotBlank() && password.isNotBlank()
}

@Composable
internal fun TvWelcomeBrandHeader() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.mpx)) {
        Text(stringResource(R.string.unityplay_tv_welcome), style = stageText(19, 700), color = stageAccent.accent)
        BrandLockup(markSize = 64, stacked = true)
    }
}

@Composable
internal fun TvXtreamSetupForm(
    name: String, server: String, username: String, password: String,
    onName: (String) -> Unit, onServer: (String) -> Unit,
    onUsername: (String) -> Unit, onPassword: (String) -> Unit,
    onSubmit: () -> Unit, onBack: () -> Unit, firstFocus: FocusRequester,
) {
    val importLabel = stringResource(R.string.setup_start_import)
    val backLabel = stringResource(R.string.common_back)
    val canImport = TvSetupPolicy.canImport(server, username, password)
    WizardFrame(
        step = WizardStep.PLAYLIST, title = stringResource(R.string.unityplay_tv_playlist), sub = null,
        back = WizardAction(backLabel, onBack),
        next = WizardAction(importLabel, onSubmit, enabled = canImport),
        centered = true,
        showStepDots = false,
    ) {
        val help = SettingHelp(stringResource(R.string.unityplay_tv_playlist), stringResource(R.string.unityplay_tv_empty))
        StageFieldRow(OwnTVIcon.PENCIL, stringResource(R.string.unityplay_tv_name), name, onName, help,
            modifier = Modifier.focusRequester(firstFocus), backLabel = backLabel)
        StageFieldRow(OwnTVIcon.PENCIL, stringResource(R.string.unityplay_tv_url), server, onServer, help,
            keyboardType = KeyboardType.Uri, backLabel = backLabel)
        StageFieldRow(OwnTVIcon.PENCIL, stringResource(R.string.setup_username), username, onUsername, help,
            backLabel = backLabel)
        StageFieldRow(OwnTVIcon.PENCIL, stringResource(R.string.setup_password), password, onPassword, help,
            password = true, backLabel = backLabel)
    }
}
