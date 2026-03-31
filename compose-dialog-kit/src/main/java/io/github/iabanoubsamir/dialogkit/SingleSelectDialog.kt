package io.github.iabanoubsamir.dialogkit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

private val SingleSelectViolet = Color(0xFF6D4C9F)
private val SingleSelectVioletLight = Color(0xFF9C6FD6)

/**
 * A dialog presenting a scrollable list of items where exactly one can be selected.
 *
 * The selection is shown with a custom radio-style dot indicator. Tapping an item updates
 * the highlighted selection but does not dismiss — the user must tap "Select" to confirm
 * or "Cancel" to discard the change.
 *
 * @param title        Dialog heading.
 * @param message      Optional supporting text below the title.
 * @param items        The full list of selectable item labels.
 * @param selectedItem The item that should appear pre-selected when the dialog opens, or null.
 * @param confirmText  Label for the confirm button.
 * @param dismissText  Label for the cancel button.
 * @param onSelect     Called with the chosen item label when the user confirms.
 * @param onDismiss    Called when the dialog is dismissed or cancelled.
 */
@Composable
fun SingleSelectDialog(
    title: String,
    message: String? = null,
    items: List<String>,
    selectedItem: String? = null,
    confirmText: String = "Select",
    dismissText: String = "Cancel",
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var currentItem by remember { mutableStateOf(selectedItem) }

    BaseDialog(onDismissRequest = onDismiss) { dismiss ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 20.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 24.dp)) {

                // ── Header ─────────────────────────────────────────────────
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-0.3).sp
                    )
                    if (message != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    thickness = 0.5.dp
                )

                // ── Scrollable item list ───────────────────────────────────
                Column(
                    modifier = Modifier
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    items.forEach { item ->
                        val selected = item == currentItem
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple()
                                ) { currentItem = item }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Custom radio indicator
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selected) SingleSelectViolet
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selected) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                            }
                            Spacer(Modifier.width(14.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selected) SingleSelectViolet
                                        else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (selected) FontWeight.SemiBold
                                             else FontWeight.Normal
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    thickness = 0.5.dp
                )
                Spacer(Modifier.height(8.dp))

                // ── Buttons ────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { dismiss() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = dismissText,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                    TextButton(
                        onClick = {
                            val chosen = currentItem
                            if (chosen != null) { onSelect(chosen); dismiss() }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = currentItem != null
                    ) {
                        Text(
                            text = confirmText,
                            color = if (currentItem != null) SingleSelectViolet
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
