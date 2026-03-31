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
import androidx.compose.material.icons.outlined.WorkspacePremium
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import io.github.iabanoubsamir.dialogkit.internal.darken

private val SubPurple = Color(0xFF4527A0)
private val SubPurpleLight = Color(0xFF7C4DFF)
private val SubGold = Color(0xFFFFD54F)
private val SubGoldDark = Color(0xFFFFB300)

/**
 * A premium subscription / paywall dialog.
 *
 * Features a rich gradient header with a crown icon and price badge, a tinted feature-list
 * card, and a high-contrast gradient CTA button — designed to feel aspirational rather
 * than aggressive.
 *
 * @param title      Heading in the gradient header, e.g. "Go Premium".
 * @param subtitle   Supporting line in the header, e.g. "Unlock everything".
 * @param features   List of short feature descriptions shown as checkmark bullets.
 * @param priceLabel Pricing string shown in the header badge, e.g. "$4.99 / month".
 * @param ctaText    Label for the subscribe button.
 * @param dismissText Label for the dismiss text link.
 * @param onSubscribe Called when the user taps the CTA.
 * @param onDismiss  Called when the dialog is dismissed.
 */
@Composable
fun SubscriptionDialog(
    title: String = "Go Premium",
    subtitle: String = "Unlock everything",
    features: List<String>,
    priceLabel: String = "$4.99 / month",
    ctaText: String = "Start Free Trial",
    dismissText: String = "Maybe Later",
    onSubscribe: () -> Unit,
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

                // ── Gradient header ────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SubPurpleLight, SubPurple.darken(0.1f)),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(Color.White.copy(alpha = 0.07f), 110.dp.toPx(),
                            center = Offset(size.width * 1.1f, size.height * 0.05f))
                        drawCircle(Color.White.copy(alpha = 0.05f), 80.dp.toPx(),
                            center = Offset(-20.dp.toPx(), size.height * 0.85f))
                        // Gold shimmer dot
                        drawCircle(SubGold.copy(alpha = 0.15f), 50.dp.toPx(),
                            center = Offset(size.width * 0.8f, size.height * 0.2f))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Crown icon halo
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.WorkspacePremium,
                                contentDescription = null,
                                tint = SubGold,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = title,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            letterSpacing = (-0.4).sp
                        )
                        Spacer(Modifier.height(4.dp))
                        // Price badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = SubGold.copy(alpha = 0.20f)
                        ) {
                            Text(
                                text = priceLabel,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                                color = SubGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        letterSpacing = (-0.2).sp
                    )

                    // Feature list card
                    if (features.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = SubPurpleLight.copy(alpha = 0.07f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                features.forEach { feature ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.CheckCircle,
                                            contentDescription = null,
                                            tint = SubPurpleLight,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(10.dp))
                                        Text(
                                            text = feature,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Gradient CTA button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(SubPurpleLight, SubPurple)
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = Color.White)
                            ) { onSubscribe(); dismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ctaText,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
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
}
