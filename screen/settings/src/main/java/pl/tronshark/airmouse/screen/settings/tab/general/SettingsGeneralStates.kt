package pl.tronshark.airmouse.screen.settings.tab.general

import pl.tronshark.airmouse.screen.settings.mvi.SettingsTabState

internal data class SettingsGeneralState(
    val analyticsEnabled: Boolean = false,
) : SettingsTabState()
