package pl.tronshark.airmouse.screen.settings.tab.mouse

import pl.tronshark.airmouse.core.common.utils.CursorType
import pl.tronshark.airmouse.screen.settings.mvi.SettingsCursorViewAction
import pl.tronshark.airmouse.screen.settings.mvi.SettingsTabCursorState

internal data class SettingsMouseState(
    override val cursorType: CursorType = CursorType.Light,
    val sPenSensitivity: Float = 0.0f,
    val cursorHideDelay: Float = 0f,
    val sPenSleepEnabled: Boolean = false,
    val cursorSize: Float = 0f,
    val cursorAlpha: Float = 0f,
) : SettingsTabCursorState()

internal sealed class SettingsMouseViewAction : SettingsCursorViewAction() {
    data class SPenSleepEnabled(val enabled: Boolean) : SettingsMouseViewAction()
}
