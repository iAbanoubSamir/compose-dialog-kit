package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

private val WarningAmber = Color(0xFFF59E0B)
private val WarningAmberDark = Color(0xFFD97706)

/**
 * A warning dialog that signals urgency without the finality of a destructive action.
 *
 * Features an animated pulsing-ring icon (radar metaphor for "pay attention") and renders
 * the title in amber so the caution intent is instantly clear. Buttons are side-by-side,
 * giving equal visual weight to cancel and confirm.
 *
 * @param title       Dialog heading.
 * @param message     Warning description shown below the title.
 * @param confirmText Label for the confirm action.
 * @param dismissText Label for the cancel action.
 * @param onConfirm   Called when the user confirms.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun WarningDialog(
    title: String,
    message: String,
    confirmText: String = "Proceed",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // Pulsing outer ring animation
    val infiniteTransition = rememberInfiniteTransition(label = "warning_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_radius"
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

                // ── Pulsing ring icon ──────────────────────────────────────
                Box(
                    modifier = Modifier.size(128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        // Animated outermost ring
                        drawCircle(
                            color = WarningAmber.copy(alpha = pulseAlpha),
                            radius = 60.dp.toPx() * pulseRadius,
                            center = center
                        )
                        // Static middle ring
                        drawCircle(
                            color = WarningAmber.copy(alpha = 0.14f),
                            radius = 46.dp.toPx(),
                            center = center
                        )
                        // Inner warm ring
                        drawCircle(
                            color = WarningAmber.copy(alpha = 0.22f),
                            radius = 33.dp.toPx(),
                            center = center
                        )
                    }
                    // Solid icon center
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(WarningAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                // Title in amber — visually separates from AlertDialog at a glance
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = WarningAmberDark,
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

                // ── Side-by-side buttons ───────────────────────────────────
                // IntrinsicSize.Max lets the Row grow to the taller button's height,
                // then fillMaxHeight() on each button keeps them equal.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    OutlinedButton(
                        onClick = dismiss,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, WarningAmber.copy(alpha = 0.55f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningAmberDark),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = dismissText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = { onConfirm(); dismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WarningAmber),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = confirmText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
