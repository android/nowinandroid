package com.google.samples.apps.nowinandroid.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BookmarkNoteDialog(
    initialNote: String = "",
    onDismiss: () -> Unit,
    onSave: (note: String) -> Unit,
) {
    var note by remember { mutableStateOf(initialNote) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a note") },
        text = {
            Column {
                OutlinedTextField(
                    value = note,
                    onValueChange = { if (it.length <= 280) note = it },
                    label = { Text("Note (optional)") },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "${note.length}/280",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(note) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
