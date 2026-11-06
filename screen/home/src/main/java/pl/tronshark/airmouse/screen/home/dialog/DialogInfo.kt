package pl.tronshark.airmouse.screen.home.dialog

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import pl.tronshark.airmouse.core.ui.utils.openUrl
import pl.tronshark.airmouse.core.ui.R as coreR

@Composable
internal fun DialogInfo(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var suggestionText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "AirMouse Ultra")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Version: 1.0")
                Text(text = "Creator: TronShark")

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Send a Suggestion:")
                OutlinedTextField(
                    value = suggestionText,
                    onValueChange = { suggestionText = it },
                    placeholder = { Text("Type your suggestion here...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (suggestionText.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:krrishkrishna612@gmail.com".toUri()
                                putExtra(Intent.EXTRA_SUBJECT, "AirMouse Ultra - User Suggestion")
                                putExtra(Intent.EXTRA_TEXT, suggestionText)
                            }
                            try {
                                context.startActivity(intent)
                                suggestionText = ""
                                onDismissRequest()
                            } catch (_: Exception) {
                                Toast.makeText(context, "No email app found to send suggestion.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please enter a suggestion first.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Suggestion")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    openUrl(context, coreR.string.donation_url)
                }
            ) {
                Text(text = "Donate ❤️")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = "Close")
            }
        }
    )
}
