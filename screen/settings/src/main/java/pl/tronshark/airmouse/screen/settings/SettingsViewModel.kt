package pl.tronshark.airmouse.screen.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.tronshark.airmouse.core.common.spen.AppToServiceEvent
import pl.tronshark.airmouse.core.common.spen.AppToServiceEvent.ModeStatus
import pl.tronshark.airmouse.core.common.utils.CursorType
import pl.tronshark.airmouse.core.common.utils.PreferencesManager
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    private val _state: MutableStateFlow<SettingsScreenState> =
        MutableStateFlow(SettingsScreenState())
    val state: StateFlow<SettingsScreenState> = _state.asStateFlow()

    fun onViewAction(action: SettingsViewAction) = when(action) {
        is SettingsViewAction.ToggleResetDialog -> toggleResetDialog(action.show)
        is SettingsViewAction.ResetSettings -> resetSettings()
    }

    private fun toggleResetDialog(show: Boolean) {
        _state.update { it.copy(showSettingsResetDialog = show) }
    }

    private fun resetSettings() {
        preferencesManager.reset()
        File(context.filesDir, CursorType.Custom.getFileName(ModeStatus.Mouse)).delete()

        if (AppToServiceEvent.serviceStatus.value != ModeStatus.Off) {
            AppToServiceEvent.event.tryEmit(AppToServiceEvent.Event.PreferencesUpdated)
        }
    }
}
