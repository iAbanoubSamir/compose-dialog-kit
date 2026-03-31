package io.github.iabanoubsamir.dialogkit

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Security
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

private val PermPurple = Color(0xFF6A1B9A)
private val PermPurpleLight = Color(0xFF8E24AA)

/**
 * A permission rationale dialog designed to build trust rather than demand compliance.
 *
 * A purple gradient header signals "this is about security/access". A "Why we need this"
 * card lists up to three benefits of granting the permission, each with a checkmark bullet.
 * The "Grant" button is a gradient fill; "Not Now" is a quiet text action below.
 *
 * @param permissionName  Human-readable name of the permission (e.g. "Camera").
 * @param rationale       One-sentence explanation of why the app needs this permission.
 * @param benefits        Up to three short bullet points further justifying the permission.
 * @param icon            Icon representing the permission category.
 * @param grantText       Label for the grant button.
 * @param denyText        Label for the deny button.
 * @param onGrant         Called when the user agrees to grant the permission.
 * @param onDeny          Called when the user denies.
 */
@Composable
fun PermissionRationaleDialog(
    permissionName: String,
    rationale: String,
    benefits: List<String> = emptyList(),
    icon: ImageVector = Icons.Outlined.Security,
    grantText: String = "Grant Permission",
    denyText: String = "Not Now",
    onGrant: () -> Unit,
    onDeny: () -> Unit
) {
    BaseDialog(onDismissRequest = onDeny) { dismiss ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 20.dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ── Purple gradient header ─────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(148.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PermPurpleLight, PermPurple.darken(0.15f)),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(Color.White.copy(alpha = 0.08f), 96.dp.toPx(),
                            center = Offset(-20.dp.toPx(), size.height + 10.dp.toPx()))
                        drawCircle(Color.White.copy(alpha = 0.06f), 74.dp.toPx(),
                            center = Offset(size.width + 14.dp.toPx(), -14.dp.toPx()))
                    }
                    // Shield halo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Permission name chip
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PermPurpleLight.copy(alpha = 0.10f)
                    ) {
                        Text(
                            text = "$permissionName Access",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                            color = PermPurple,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = rationale,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 21.sp
                    )

                    // Benefits card
                    if (benefits.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = PermPurpleLight.copy(alpha = 0.07f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                benefits.forEach { benefit ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.CheckCircle,
                                            contentDescription = null,
                                            tint = PermPurpleLight,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = benefit,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // Gradient grant button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(PermPurpleLight, PermPurple)
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = Color.White)
                            ) { onGrant(); dismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = grantText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    TextButton(
                        onClick = { onDeny(); dismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = denyText,
                            color = PermPurpleLight,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
