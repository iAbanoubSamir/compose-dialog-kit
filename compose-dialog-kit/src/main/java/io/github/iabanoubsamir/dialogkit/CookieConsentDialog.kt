package io.github.iabanoubsamir.dialogkit

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

private val CookieAmber = Color(0xFFF57C00)
private val CookieAmberLight = Color(0xFFFF9800)
private val CookieAmberPale = Color(0xFFFFF3E0)

/**
 * A GDPR-style cookie/privacy consent dialog.
 *
 * Presents a warm, approachable framing of data usage rather than a legal wall of text.
 * "Accept All" is the primary action; "Manage Preferences" lets the user opt out of
 * non-essential cookies. An optional privacy policy link can be surfaced via [onPolicy].
 *
 * @param title       Dialog heading.
 * @param description Short explanation of what cookies/data are used for.
 * @param acceptText  Label for the accept-all button.
 * @param manageText  Label for the manage-preferences button.
 * @param policyText  Label for the privacy policy link, or null to hide it.
 * @param onAccept    Called when the user accepts all.
 * @param onManage    Called when the user wants to manage preferences.
 * @param onPolicy    Called when the user taps the privacy policy link.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun CookieConsentDialog(
    title: String = "We Value Your Privacy",
    description: String = "We use cookies to personalize your experience, analyze traffic, and show relevant content. You can manage your preferences at any time.",
    acceptText: String = "Accept All",
    manageText: String = "Manage Preferences",
    policyText: String? = "Privacy Policy",
    onAccept: () -> Unit,
    onManage: () -> Unit,
    onPolicy: (() -> Unit)? = null,
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

                // ── Cookie icon ────────────────────────────────────────────
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        // Warm glow rings
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(CookieAmberLight.copy(alpha = 0.18f), Color.Transparent),
                                radius = 48.dp.toPx()
                            ),
                            radius = 48.dp.toPx(),
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                        drawCircle(
                            color = CookieAmber.copy(alpha = 0.08f),
                            radius = 46.dp.toPx(),
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(CookieAmberLight, CookieAmber),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Cookie,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
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
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(22.dp))

                // ── Buttons ────────────────────────────────────────────────
                // Accept All — full-width gradient primary
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(CookieAmberLight, CookieAmber)
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.White)
                        ) { onAccept(); dismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = acceptText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Manage Preferences — full-width outlined secondary
                OutlinedButton(
                    onClick = { onManage(); dismiss() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, CookieAmber.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CookieAmber)
                ) {
                    Text(
                        text = manageText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                if (policyText != null && onPolicy != null) {
                    TextButton(onClick = { onPolicy(); dismiss() }) {
                        Text(
                            text = policyText,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}
