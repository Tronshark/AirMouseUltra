package pl.tronshark.airmouse.screen.settings.tab.scroll

import pl.tronshark.airmouse.core.common.utils.ScrollAction
import pl.tronshark.airmouse.screen.settings.mvi.SettingsTabState
import pl.tronshark.airmouse.screen.settings.mvi.SettingsViewAction

internal data class SettingsScrollState(
    val singlePressAction: ScrollAction = ScrollAction.Default,
    val doublePressAction: ScrollAction = ScrollAction.None,
    val longPressAction: ScrollAction = ScrollAction.None,
    val scrollDistance: Float = 10f
): SettingsTabState()

internal sealed class SettingsScrollViewAction: SettingsViewAction() {
    data class SinglePressActionUpdate(val action: ScrollAction): SettingsScrollViewAction()
    data class DoublePressActionUpdate(val action: ScrollAction): SettingsScrollViewAction()
    data class LongPressActionUpdate(val action: ScrollAction): SettingsScrollViewAction()
}
