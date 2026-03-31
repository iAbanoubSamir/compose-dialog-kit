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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LockClock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private val DeleteCrimson = Color(0xFFD32F2F)
private val DeleteCrimsonLight = Color(0xFFEF5350)

/**
 * A destructive-action confirmation dialog designed to give the user pause.
 *
 * The trash icon shakes once on entry (subconscious danger signal), a radial glow
 * pulses behind it, and the title is rendered in crimson. The button hierarchy is
 * intentionally inverted — Cancel is the visually dominant action, Delete is outlined
 * and de-emphasised, reducing accidental destructive taps.
 *
 * @param title       Dialog heading (e.g. "Delete photo?").
 * @param message     Irreversibility notice shown below the title.
 * @param confirmText Label for the destructive confirm button.
 * @param dismissText Label for the safe cancel button.
 * @param onConfirm   Called when the user confirms the destructive action.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun DeleteDialog(
    title: String = "Delete?",
    message: String = "This action cannot be undone.",
    confirmText: String = "Delete",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // Shake animation: icon waggles left-right once after dialog enters
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(320) // let the enter spring settle first
        for (i in 0 until 4) {
            shakeOffset.animateTo(if (i % 2 == 0) 7f else -7f, animationSpec = tween(70))
        }
        shakeOffset.animateTo(0f, animationSpec = tween(70))
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
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Radial glow + shaking icon ─────────────────────────────
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
                    contentAlignment = Alignment.Center
                ) {
                    // Radial gradient glow (different from Warning's discrete rings)
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    DeleteCrimson.copy(alpha = 0.22f),
                                    DeleteCrimson.copy(alpha = 0.10f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = size.minDimension / 2f
                            ),
                            radius = size.minDimension / 2f,
                            center = center
                        )
                    }
                    // Icon container
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(DeleteCrimson),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // ── "Permanent action" chip ────────────────────────────────
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DeleteCrimson.copy(alpha = 0.09f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LockClock,
                            contentDescription = null,
                            tint = DeleteCrimson,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "Permanent action",
                            color = DeleteCrimson,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Title in crimson
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = DeleteCrimson,
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

                Spacer(Modifier.height(28.dp))

                // ── Inverted button hierarchy ──────────────────────────────
                // Cancel is the dominant filled button (safe-action-primary pattern).
                // Delete is outlined + red — present but visually secondary.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    Button(
                        onClick = dismiss,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = dismissText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = { onConfirm(); dismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, DeleteCrimsonLight),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DeleteCrimson),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = confirmText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
