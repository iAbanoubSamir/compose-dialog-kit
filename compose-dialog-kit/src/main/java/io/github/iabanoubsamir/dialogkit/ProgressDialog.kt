package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

private val ProgressCyan = Color(0xFF0097A7)
private val ProgressCyanLight = Color(0xFF00BCD4)

/**
 * A determinate progress dialog showing how far along a task is.
 *
 * Displays an animated horizontal progress bar with a live percentage label. Useful for
 * file uploads, sync operations, or multi-step flows where the total work is known.
 *
 * @param title      Heading shown above the progress bar.
 * @param message    Optional supporting text below the title.
 * @param progress   Current progress as a value between 0f and 1f.
 * @param stepText   Optional label shown below the bar, e.g. "Step 2 of 4" or "Uploading…".
 * @param cancelText Label for the cancel button, or null to hide it.
 * @param onCancel   Called when the user taps cancel. Required if [cancelText] is non-null.
 * @param onDismiss  Called when the dialog is dismissed.
 */
@Composable
fun ProgressDialog(
    title: String = "Processing…",
    message: String? = null,
    progress: Float,
    stepText: String? = null,
    cancelText: String? = "Cancel",
    onCancel: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 400),
        label = "progress"
    )
    val percentage = (animatedProgress * 100).toInt()

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
                // Percentage badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ProgressCyan.copy(alpha = 0.10f)
                ) {
                    Text(
                        text = "$percentage%",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        color = ProgressCyan,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(Modifier.height(16.dp))

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

                Spacer(Modifier.height(22.dp))

                // Progress bar track + fill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ProgressCyan.copy(alpha = 0.12f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(ProgressCyanLight, ProgressCyan)
                                )
                            )
                    )
                }

                if (stepText != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = stepText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                if (cancelText != null && onCancel != null) {
                    Spacer(Modifier.height(6.dp))
                    TextButton(
                        onClick = { onCancel(); dismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = cancelText,
                            color = ProgressCyan,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}
