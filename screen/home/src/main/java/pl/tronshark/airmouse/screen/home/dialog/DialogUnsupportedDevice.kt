package pl.tronshark.airmouse.screen.home.dialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pl.tronshark.airmouse.screen.home.R
import pl.tronshark.airmouse.core.ui.components.MarkdownDialog
import pl.tronshark.airmouse.core.ui.R as coreR

@Composable
internal fun DialogUnsupportedDevice(
    onDismissRequest: () -> Unit
) = MarkdownDialog(
    iconId = coreR.drawable.ic_off_mode,
    titleId = R.string.dialog_unsupported_device_title,
    markdownTextId = R.raw.dialog_unsupported_device,
    confirmButton = {
        TextButton(
            onClick = onDismissRequest
        ) {
            Text(text = stringResource(R.string.dialog_unsupported_device_button))
        }
    },
    onDismissRequest = onDismissRequest
)