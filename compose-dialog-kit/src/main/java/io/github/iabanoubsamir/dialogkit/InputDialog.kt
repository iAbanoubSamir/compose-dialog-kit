package io.github.iabanoubsamir.dialogkit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import io.github.iabanoubsamir.dialogkit.internal.darken

private val InputBlue = Color(0xFF1E88E5)

/**
 * A dialog with a text input field as the visual centrepiece.
 *
 * The card is minimal and focused — there is no decorative header, keeping all attention
 * on the text field. A coloured top accent strip (matching [iconColor]) replaces the
 * large header band and indicates the purpose at a glance. The confirm button uses a
 * gradient fill; the cancel is a quiet text action.
 *
 * @param title         Dialog heading.
 * @param message       Optional supporting text shown above the input field.
 * @param hint          Placeholder text shown inside the input field.
 * @param initialValue  Pre-filled value in the input field.
 * @param confirmText   Label for the confirm button.
 * @param dismissText   Label for the cancel button.
 * @param keyboardType  Keyboard type for the input field.
 * @param icon          Icon shown in the accent strip.
 * @param iconColor     Color of the accent strip and interactive elements.
 * @param onConfirm     Called with the entered text when the user confirms.
 * @param onDismiss     Called when the dialog is dismissed.
 */
@Composable
fun InputDialog(
    title: String,
    message: String? = null,
    hint: String = "",
    initialValue: String = "",
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector = Icons.Outlined.Edit,
    iconColor: Color = InputBlue,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }

    BaseDialog(onDismissRequest = onDismiss) { dismiss ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 20.dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ── Thin accent strip at top ───────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(iconColor, iconColor.darken(0.25f))
                            )
                        )
                )

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-0.3).sp
                    )

                    if (message != null) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 21.sp
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    // ── Input field ────────────────────────────────────────
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            Text(
                                hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = iconColor,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            cursorColor = iconColor,
                            focusedLabelColor = iconColor
                        )
                    )

                    Spacer(Modifier.height(22.dp))

                    // ── Gradient confirm button ────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(iconColor, iconColor.darken(0.22f))
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = Color.White)
                            ) { onConfirm(text); dismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 0.2.sp
                        )
                    }

                    TextButton(
                        onClick = dismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = dismissText,
                            color = iconColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
