package pl.tronshark.airmouse.screen.settings.tab.scroll

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.tronshark.airmouse.core.common.utils.PrefKey
import pl.tronshark.airmouse.core.common.utils.PrefKeys
import pl.tronshark.airmouse.core.common.utils.ScrollAction
import pl.tronshark.airmouse.core.ui.theme.AirMouseDevicePreview
import pl.tronshark.airmouse.core.ui.theme.pad_l
import pl.tronshark.airmouse.core.ui.theme.pad_m
import pl.tronshark.airmouse.core.ui.theme.pad_xl
import pl.tronshark.airmouse.screen.settings.R
import pl.tronshark.airmouse.screen.settings.components.horizontalDivider
import pl.tronshark.airmouse.screen.settings.components.settingsSlider
import pl.tronshark.airmouse.screen.settings.mvi.SettingsViewAction

@Composable
internal fun SettingsScrollTab(
    contentPadding: PaddingValues,
    refreshDataTrigger: Boolean,
    viewModel: SettingsScrollViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(refreshDataTrigger) {
        if (!refreshDataTrigger) viewModel.onViewAction(SettingsViewAction.RefreshData)
    }

    // @formatter:off
    SettingsScrollTabContent(
        viewState = viewState,
        contentPadding = contentPadding,
        onSinglePressActionSelected = { viewModel.onViewAction(SettingsScrollViewAction.SinglePressActionUpdate(it)) },
        onDoublePressActionSelected = { viewModel.onViewAction(SettingsScrollViewAction.DoublePressActionUpdate(it)) },
        onLongPressActionSelected = { viewModel.onViewAction(SettingsScrollViewAction.LongPressActionUpdate(it)) },
        onPrefChanging = { key, value -> viewModel.onViewAction(SettingsViewAction.UpdatePreference(key, value)) },
        onPrefChanged = { key, value -> viewModel.onViewAction(SettingsViewAction.SavePreference(key, value)) }
    )
    // @formatter:on
}

@Composable
private fun SettingsScrollTabContent(
    viewState: SettingsScrollState,
    contentPadding: PaddingValues,
    onSinglePressActionSelected: (ScrollAction) -> Unit = {},
    onDoublePressActionSelected: (ScrollAction) -> Unit = {},
    onLongPressActionSelected: (ScrollAction) -> Unit = {},
    onPrefChanging: (PrefKey<Float>, Float) -> Unit = { _, _ -> },
    onPrefChanged: (PrefKey<Float>, Float) -> Unit = { _, _ -> }
) {
    var showSinglePressDialog by remember { mutableStateOf(false) }
    var showDoublePressDialog by remember { mutableStateOf(false) }
    var showLongPressDialog by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        scrollActionItem(
            labelId = R.string.settings_scroll_single_press_action,
            currentAction = viewState.singlePressAction,
            onClick = { showSinglePressDialog = true }
        )

        horizontalDivider()

        scrollActionItem(
            labelId = R.string.settings_scroll_double_press_action,
            currentAction = viewState.doublePressAction,
            onClick = { showDoublePressDialog = true }
        )

        horizontalDivider()

        scrollActionItem(
            labelId = R.string.settings_scroll_long_press_action,
            currentAction = viewState.longPressAction,
            onClick = { showLongPressDialog = true }
        )

        horizontalDivider()

        settingsSlider(
            text = R.string.settings_scroll_distance_slider_label,
            value = viewState.scrollDistance,
            prefKey = PrefKeys.SCROLL_DISTANCE,
            onPrefChanging = onPrefChanging,
            onPrefChanged = onPrefChanged
        )

        horizontalDivider()
    }

    if (showSinglePressDialog) {
        ScrollActionPickerDialog(
            title = stringResource(R.string.settings_scroll_single_press_action),
            currentAction = viewState.singlePressAction,
            onDismiss = { showSinglePressDialog = false },
            onSelected = onSinglePressActionSelected
        )
    }

    if (showDoublePressDialog) {
        ScrollActionPickerDialog(
            title = stringResource(R.string.settings_scroll_double_press_action),
            currentAction = viewState.doublePressAction,
            onDismiss = { showDoublePressDialog = false },
            onSelected = onDoublePressActionSelected
        )
    }

    if (showLongPressDialog) {
        ScrollActionPickerDialog(
            title = stringResource(R.string.settings_scroll_long_press_action),
            currentAction = viewState.longPressAction,
            onDismiss = { showLongPressDialog = false },
            onSelected = onLongPressActionSelected
        )
    }
}

private fun LazyListScope.scrollActionItem(
    @StringRes labelId: Int,
    currentAction: ScrollAction,
    onClick: () -> Unit
) = item {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = pad_l, horizontal = pad_xl)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(labelId),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(currentAction.label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ScrollActionPickerDialog(
    title: String,
    currentAction: ScrollAction,
    onDismiss: () -> Unit,
    onSelected: (ScrollAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                ScrollAction.entries.forEach { action ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelected(action)
                                onDismiss()
                            }
                            .padding(vertical = pad_m),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (action == currentAction),
                            onClick = {
                                onSelected(action)
                                onDismiss()
                            }
                        )
                        Text(
                            text = stringResource(action.label),
                            modifier = Modifier.padding(start = pad_m)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.settings_reset_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun SettingsScrollPreview() {
    AirMouseDevicePreview {
        SettingsScrollTabContent(
            viewState = SettingsScrollState(),
            contentPadding = it,
        )
    }
}
