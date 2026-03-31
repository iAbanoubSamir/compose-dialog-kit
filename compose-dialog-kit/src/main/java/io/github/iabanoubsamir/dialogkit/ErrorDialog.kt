package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private val ErrorRed = Color(0xFFD32F2F)
private val ErrorRedLight = Color(0xFFEF5350)

/**
 * An error dialog with a shake animation on the X icon, a dark red gradient header,
 * and an optional retry action laid out beside the dismiss button.
 *
 * @param title       Dialog heading.
 * @param message     Error description shown to the user.
 * @param dismissText Label for the dismiss button.
 * @param retryText   Label for the retry button, or null to hide it.
 * @param onDismiss   Called when the dialog is dismissed.
 * @param onRetry     Called when the user taps retry. Null hides the button.
 */
@Composable
fun ErrorDialog(
    title: String = "Something went wrong",
    message: String,
    dismissText: String = "Dismiss",
    retryText: String? = "Try Again",
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(280)
        repeat(4) { i ->
            shakeOffset.animateTo(if (i % 2 == 0) 8f else -8f, animationSpec = tween(65))
        }
        shakeOffset.animateTo(0f, animationSpec = tween(65))
    }

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

                // ── Dark red gradient header ───────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(ErrorRed, Color(0xFF7F0000)),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(Color.White.copy(alpha = 0.07f), radius = 100.dp.toPx(),
                            center = Offset(-18.dp.toPx(), size.height + 8.dp.toPx()))
                        drawCircle(Color.White.copy(alpha = 0.05f), radius = 80.dp.toPx(),
                            center = Offset(size.width + 16.dp.toPx(), -16.dp.toPx()))
                    }

                    // Shaking X icon
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Hand-drawn X via Canvas for crisp look
                        Canvas(modifier = Modifier.size(36.dp)) {
                            val sw = 3.5.dp.toPx()
                            val pad = 6.dp.toPx()
                            drawLine(Color.White, Offset(pad, pad), Offset(size.width - pad, size.height - pad), sw, StrokeCap.Round)
                            drawLine(Color.White, Offset(size.width - pad, pad), Offset(pad, size.height - pad), sw, StrokeCap.Round)
                        }
                    }
                }

                // ── Content ────────────────────────────────────────────────
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
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 21.sp
                    )
                    Spacer(Modifier.height(24.dp))

                    if (onRetry != null && retryText != null) {
                        // Side-by-side: Dismiss (outlined) | Retry (filled)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                        ) {
                            OutlinedButton(
                                onClick = dismiss,
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.5.dp, ErrorRedLight.copy(alpha = 0.55f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                            ) {
                                Text(dismissText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, textAlign = TextAlign.Center)
                            }
                            Spacer(Modifier.width(12.dp))
                            Button(
                                onClick = { onRetry(); dismiss() },
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                            ) {
                                Icon(Icons.Outlined.Refresh, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(retryText, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
                            }
                        }
                    } else {
                        // Single dismiss button, full width
                        OutlinedButton(
                            onClick = dismiss,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.5.dp, ErrorRed),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)
                        ) {
                            Text(dismissText, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
