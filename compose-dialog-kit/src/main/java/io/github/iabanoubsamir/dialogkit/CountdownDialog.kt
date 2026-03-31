package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import kotlin.math.ceil

/**
 * A dialog that auto-dismisses after [seconds] seconds, with a depleting ring animation.
 *
 * The ring drains smoothly from full to empty over the full duration, giving users a
 * clear visual cue of how much time remains. Use it for timed offers, auto-retry prompts,
 * or any UX flow where you want a countdown before acting automatically.
 *
 * @param title       Heading shown below the ring.
 * @param message     Optional supporting text.
 * @param seconds     Total countdown duration in seconds.
 * @param accentColor Color used for the ring and countdown number.
 * @param cancelText  Label for the cancel button, or null to hide it.
 * @param onTimeout   Called when the countdown reaches zero (before dismiss animation).
 * @param onCancel    Called when the user taps cancel. Falls back to [onDismiss] if null.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun CountdownDialog(
    title: String,
    message: String? = null,
    seconds: Int,
    accentColor: Color = Color(0xFFE53935),
    cancelText: String? = "Cancel",
    onTimeout: () -> Unit,
    onCancel: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    require(seconds > 0) { "seconds must be positive, got $seconds" }
    val animatedSeconds = remember { Animatable(seconds.toFloat()) }

    BaseDialog(onDismissRequest = onDismiss) { dismiss ->

        LaunchedEffect(Unit) {
            animatedSeconds.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = seconds * 1000,
                    easing = LinearEasing
                )
            )
            onTimeout()
            dismiss()
        }

        val fraction = (animatedSeconds.value / seconds.toFloat()).coerceIn(0f, 1f)
        val displaySeconds = ceil(animatedSeconds.value).toInt().coerceAtLeast(0)

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

                // ── Countdown ring ─────────────────────────────────────────
                Box(
                    modifier = Modifier.size(112.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val strokeWidth = 8.dp.toPx()
                        val inset = strokeWidth / 2f
                        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                        val topLeft = Offset(inset, inset)

                        // Background track
                        drawArc(
                            color = accentColor.copy(alpha = 0.12f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Depleting foreground arc
                        drawArc(
                            color = accentColor,
                            startAngle = -90f,
                            sweepAngle = 360f * fraction,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "$displaySeconds",
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        letterSpacing = (-1).sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.3).sp
                )

                if (message != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 21.sp
                    )
                }

                if (cancelText != null) {
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = { onCancel?.invoke(); dismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = cancelText,
                            color = accentColor,
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
