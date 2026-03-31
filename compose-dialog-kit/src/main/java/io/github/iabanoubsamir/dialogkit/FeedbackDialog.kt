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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

private val FeedbackTeal = Color(0xFF00897B)
private val FeedbackTealLight = Color(0xFF26A69A)
private val FeedbackStarGold = Color(0xFFF59E0B)

/**
 * An in-app feedback dialog combining a text area with an optional star rating.
 *
 * Unlike [RateAppDialog] (which redirects to the Play Store), this dialog collects
 * free-form text feedback that can be sent directly to the developer. The submit button
 * stays disabled until the user has typed something.
 *
 * @param title         Dialog heading.
 * @param hint          Placeholder text inside the text field.
 * @param includeRating Whether to show the optional star rating row above the text field.
 * @param submitText    Label for the submit button.
 * @param dismissText   Label for the cancel button.
 * @param onSubmit      Called with the entered text and star count (0 if rating hidden/skipped).
 * @param onDismiss     Called when the dialog is dismissed.
 */
@Composable
fun FeedbackDialog(
    title: String = "Send Feedback",
    hint: String = "Tell us what you think…",
    includeRating: Boolean = true,
    submitText: String = "Send",
    dismissText: String = "Cancel",
    onSubmit: (text: String, stars: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var feedbackText by remember { mutableStateOf("") }
    var selectedStars by remember { mutableIntStateOf(0) }

    BaseDialog(onDismissRequest = onDismiss) { dismiss ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 20.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Icon + title ───────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(FeedbackTeal.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RateReview,
                        contentDescription = null,
                        tint = FeedbackTeal,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.3).sp
                )

                // ── Optional star rating ───────────────────────────────────
                if (includeRating) {
                    Spacer(Modifier.height(16.dp))
                    Row {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = if (star <= selectedStars) Icons.Filled.Star
                                              else Icons.Outlined.StarOutline,
                                contentDescription = "$star star",
                                tint = if (star <= selectedStars) FeedbackStarGold
                                       else MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { selectedStars = star }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Text field ─────────────────────────────────────────────
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    },
                    minLines = 4,
                    maxLines = 6,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FeedbackTeal,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        cursorColor = FeedbackTeal
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

                Spacer(Modifier.height(20.dp))

                // Gradient send button (disabled until text is entered)
                val hasText = feedbackText.isNotBlank()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    FeedbackTealLight.copy(alpha = if (hasText) 1f else 0.4f),
                                    FeedbackTeal.copy(alpha = if (hasText) 1f else 0.4f)
                                )
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = hasText
                        ) {
                            if (hasText) { onSubmit(feedbackText.trim(), selectedStars); dismiss() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = submitText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                TextButton(
                    onClick = { dismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = dismissText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
