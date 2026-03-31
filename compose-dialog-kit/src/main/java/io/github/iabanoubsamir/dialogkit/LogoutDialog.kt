package io.github.iabanoubsamir.dialogkit

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
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

private val LogoutOrange = Color(0xFFE65100)
private val LogoutOrangeLight = Color(0xFFF4511E)

/**
 * A logout confirmation dialog with a warm deep-orange palette.
 *
 * The icon floats above a two-tone background arc drawn with Canvas — the arc splits
 * the icon area into a warm-toned half and a neutral half, visually suggesting the
 * "leaving" metaphor. The button layout mirrors [DeleteDialog]'s safe-action-primary
 * pattern: "Stay" is the dominant filled button, "Log Out" is an outlined danger action.
 *
 * @param title       Dialog heading.
 * @param message     Confirmation question shown below the title.
 * @param confirmText Label for the logout button.
 * @param dismissText Label for the stay/cancel button.
 * @param onConfirm   Called when the user confirms logout.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun LogoutDialog(
    title: String = "Log Out?",
    message: String = "Are you sure you want to log out?",
    confirmText: String = "Log Out",
    dismissText: String = "Stay",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
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

                // ── Two-tone arc icon area ─────────────────────────────────
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        // Warm half-disc (left side)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LogoutOrangeLight.copy(alpha = 0.20f),
                                    Color.Transparent
                                ),
                                center = Offset(cx - 10.dp.toPx(), cy),
                                radius = 52.dp.toPx()
                            ),
                            radius = 52.dp.toPx(),
                            center = Offset(cx, cy)
                        )
                        // Outer thin ring
                        drawCircle(
                            color = LogoutOrange.copy(alpha = 0.12f),
                            radius = 54.dp.toPx(),
                            center = Offset(cx, cy),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(LogoutOrangeLight, LogoutOrange),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
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

                Spacer(Modifier.height(28.dp))

                // Safe-action-primary: Stay is dominant, Log Out is outlined
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    Button(
                        onClick = dismiss,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(dismissText, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                    Spacer(Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = { onConfirm(); dismiss() },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, LogoutOrangeLight),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LogoutOrange),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(confirmText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
