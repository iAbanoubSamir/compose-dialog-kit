package io.github.iabanoubsamir.dialogkit

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import io.github.iabanoubsamir.dialogkit.internal.darken

private val AlertBlue = Color(0xFF1E88E5)

/**
 * A general-purpose alert dialog with a full-width gradient header, decorative depth circles,
 * and a gradient action button. One-liner simple case, fully customisable.
 *
 * @param title       Dialog heading.
 * @param message     Supporting text shown below the title.
 * @param confirmText Label for the primary (confirm) button.
 * @param dismissText Label for the secondary (cancel) button, or null to hide it.
 * @param icon        Icon displayed in the gradient header.
 * @param iconColor   Gradient base color — drives the entire header and button palette.
 * @param onConfirm   Called when the confirm button is tapped.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun AlertDialog(
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String? = "Cancel",
    icon: ImageVector = Icons.Outlined.Info,
    iconColor: Color = AlertBlue,
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ── Gradient header ─────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(iconColor, iconColor.darken(0.28f)),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Decorative depth circles
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.09f),
                            radius = 108.dp.toPx(),
                            center = Offset(-22.dp.toPx(), size.height + 14.dp.toPx())
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.07f),
                            radius = 86.dp.toPx(),
                            center = Offset(size.width + 18.dp.toPx(), -18.dp.toPx())
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.05f),
                            radius = 52.dp.toPx(),
                            center = Offset(size.width * 0.78f, size.height * 0.85f)
                        )
                    }

                    // Halo ring behind the icon
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }

                // ── Content ─────────────────────────────────────────────────
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

                    // Gradient confirm button
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
                            ) { onConfirm(); dismiss() },
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

                    if (dismissText != null) {
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
}
