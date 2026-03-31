package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

private val OfflineSlate = Color(0xFF455A64)
private val OfflineSlateLight = Color(0xFF607D8B)

/**
 * A no-internet dialog with a custom animated wifi-arc icon drawn entirely with Canvas.
 *
 * The three wifi arcs fade in and out sequentially (like a scanning animation), making
 * it immediately clear that the device is trying and failing to connect. The "Retry"
 * button is the sole call-to-action; dismiss is a quiet text link.
 *
 * @param title       Dialog heading.
 * @param message     Supporting text explaining the situation.
 * @param retryText   Label for the retry button.
 * @param dismissText Label for the cancel text link, or null to hide it.
 * @param onRetry     Called when the user taps retry.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun NoInternetDialog(
    title: String = "No Internet Connection",
    message: String = "Please check your connection and try again.",
    retryText: String = "Retry",
    dismissText: String? = "Cancel",
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    // Three arcs animate with staggered phase
    val transition = rememberInfiniteTransition(label = "wifi_scan")
    val arc1 by transition.animateFloat(
        initialValue = 0.25f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, 0, EaseInOut), RepeatMode.Reverse),
        label = "arc1"
    )
    val arc2 by transition.animateFloat(
        initialValue = 0.25f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, 300, EaseInOut), RepeatMode.Reverse),
        label = "arc2"
    )
    val arc3 by transition.animateFloat(
        initialValue = 0.25f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, 600, EaseInOut), RepeatMode.Reverse),
        label = "arc3"
    )

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

                // ── Animated wifi-arc icon ─────────────────────────────────
                Box(
                    modifier = Modifier.size(112.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(OfflineSlate.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(52.dp)) {
                            val cx = size.width / 2f
                            val cy = size.height * 0.72f
                            val sw = 3.dp.toPx()

                            // Dot at bottom center
                            drawCircle(OfflineSlate, 3.dp.toPx(), Offset(cx, cy))

                            // Three upward arcs, each brighter when active
                            listOf(
                                Triple(11.dp.toPx(), 200f, arc1),
                                Triple(22.dp.toPx(), 200f, arc2),
                                Triple(33.dp.toPx(), 200f, arc3)
                            ).forEach { (radius, sweepAngle, alpha) ->
                                val startAngle = 270f - sweepAngle / 2f
                                drawArc(
                                    color = OfflineSlate.copy(alpha = 0.25f + alpha * 0.55f),
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = Offset(cx - radius, cy - radius),
                                    size = Size(radius * 2, radius * 2),
                                    style = Stroke(width = sw, cap = StrokeCap.Round)
                                )
                            }
                        }
                    }
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
                Spacer(Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(26.dp))

                // Gradient retry button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(OfflineSlateLight, OfflineSlate)
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.White)
                        ) { onRetry(); dismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = retryText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                if (dismissText != null) {
                    TextButton(
                        onClick = dismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = dismissText,
                            color = OfflineSlateLight,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
