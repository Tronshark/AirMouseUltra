package pl.tronshark.airmouse.screen.settings.mvi

import android.net.Uri
import pl.tronshark.airmouse.core.common.utils.CursorType

internal abstract class SettingsTabCursorState() : SettingsTabState() {
    abstract val cursorType: CursorType
}

internal open class SettingsCursorViewAction : SettingsViewAction() {
    data class CursorTypeChanged(val cursorType: CursorType) : SettingsCursorViewAction()
    data class CustomCursorFileSelected(val uri: Uri) : SettingsCursorViewAction()
}