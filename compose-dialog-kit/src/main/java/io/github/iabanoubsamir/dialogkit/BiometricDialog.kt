package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

/**
 * A visual biometric authentication prompt with a pulsing fingerprint animation.
 *
 * Provides the in-app overlay for a biometric flow. The caller is responsible for
 * triggering the actual BiometricPrompt API; this composable shows the surrounding UI.
 *
 * @param title        Heading shown below the fingerprint icon.
 * @param subtitle     Supporting instruction text.
 * @param accentColor  Color for the icon, pulsing rings, and fallback button.
 * @param fallbackText Label for the alternative auth button, or null to hide it.
 * @param onFallback   Called when the user taps the fallback button.
 * @param onDismiss    Called when the dialog is dismissed.
 */
@Composable
fun BiometricDialog(
    title: String = "Biometric Login",
    subtitle: String = "Place your finger on the sensor to authenticate",
    accentColor: Color = Color(0xFF00897B),
    fallbackText: String? = "Use PIN instead",
    onFallback: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "bio_pulse")
    val ring1 by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring1"
    )
    val ring2 by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, delayMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring2"
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
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Pulsing rings + fingerprint icon ───────────────────────
                Box(
                    modifier = Modifier.size(144.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val maxRadius = size.minDimension / 2f

                        // Expanding ring 1
                        drawCircle(
                            color = accentColor.copy(alpha = (1f - ring1) * 0.28f),
                            radius = maxRadius * ring1,
                            style = Stroke(width = 2.dp.toPx())
                        )
                        // Expanding ring 2
                        drawCircle(
                            color = accentColor.copy(alpha = (1f - ring2) * 0.28f),
                            radius = maxRadius * ring2,
                            style = Stroke(width = 2.dp.toPx())
                        )
                        // Static center halo
                        drawCircle(
                            color = accentColor.copy(alpha = 0.12f),
                            radius = 54.dp.toPx()
                        )
                    }

                    Icon(
                        imageVector = Icons.Outlined.Fingerprint,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(72.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

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
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(8.dp))

                if (fallbackText != null && onFallback != null) {
                    TextButton(
                        onClick = { onFallback(); dismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = fallbackText,
                            color = accentColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    TextButton(
                        onClick = { dismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cancel",
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
