package pl.tronshark.airmouse.screen.settings.tab.scroll

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.tronshark.airmouse.core.common.utils.PrefKeys
import pl.tronshark.airmouse.core.common.utils.PreferencesManager
import pl.tronshark.airmouse.core.common.utils.ScrollAction
import pl.tronshark.airmouse.screen.settings.mvi.SettingsTabViewModel
import pl.tronshark.airmouse.screen.settings.mvi.SettingsViewAction
import javax.inject.Inject

@HiltViewModel
internal class SettingsScrollViewModel @Inject constructor(
    preferencesManager: PreferencesManager
) : SettingsTabViewModel<SettingsScrollState>(
    preferencesManager,
    SettingsScrollState()
) {
    override fun refreshSettingsData() = updateState {
        SettingsScrollState(
            singlePressAction = preferencesManager.get(PrefKeys.SCROLL_SINGLE_PRESS_ACTION),
            doublePressAction = preferencesManager.get(PrefKeys.SCROLL_DOUBLE_PRESS_ACTION),
            longPressAction = preferencesManager.get(PrefKeys.SCROLL_LONG_PRESS_ACTION),
            scrollDistance = preferencesManager.get(PrefKeys.SCROLL_DISTANCE)
        )
    }

    override fun onViewAction(action: SettingsViewAction) = when(action) {
        is SettingsViewAction.RefreshData -> refreshSettingsData()
        is SettingsViewAction.SavePreference<*> -> savePreference(action)
        is SettingsViewAction.UpdatePreference<*> -> updatePreference(action)
        is SettingsScrollViewAction.SinglePressActionUpdate -> singlePressActionUpdate(action.action)
        is SettingsScrollViewAction.DoublePressActionUpdate -> doublePressActionUpdate(action.action)
        is SettingsScrollViewAction.LongPressActionUpdate -> longPressActionUpdate(action.action)
        else -> Unit
    }

    override fun updatePreference(action: SettingsViewAction.UpdatePreference<*>) = when(action.key) {
        PrefKeys.SCROLL_DISTANCE -> {
            updateState { it.copy(scrollDistance = action.value as Float) }
        }
        else -> {}
    }

    private fun singlePressActionUpdate(action: ScrollAction) {
        preferencesManager.put(PrefKeys.SCROLL_SINGLE_PRESS_ACTION, action)
        updateState { it.copy(singlePressAction = action) }
        tryToPingService()
    }

    private fun doublePressActionUpdate(action: ScrollAction) {
        preferencesManager.put(PrefKeys.SCROLL_DOUBLE_PRESS_ACTION, action)
        updateState { it.copy(doublePressAction = action) }
        tryToPingService()
    }

    private fun longPressActionUpdate(action: ScrollAction) {
        preferencesManager.put(PrefKeys.SCROLL_LONG_PRESS_ACTION, action)
        updateState { it.copy(longPressAction = action) }
        tryToPingService()
    }
}
