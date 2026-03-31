package io.github.iabanoubsamir.dialogkit

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
import androidx.compose.material.icons.outlined.Lightbulb
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import io.github.iabanoubsamir.dialogkit.internal.darken

/**
 * A coach-mark style dialog for guiding users through a feature during onboarding.
 *
 * Designed to feel encouraging rather than interruptive: a tinted icon box, friendly
 * headline, concise description, and a clear primary action. An optional step indicator
 * shows progress through a multi-step tour. A skip link lets users bail out gracefully.
 *
 * @param title       Feature name or short action heading.
 * @param message     One or two sentences explaining the feature.
 * @param icon        Icon representing the feature being introduced.
 * @param accentColor Tint color applied to the icon box and buttons.
 * @param stepText    Optional step indicator shown above the title, e.g. "Step 2 of 5".
 * @param primaryText Label for the main action button (e.g. "Got it", "Next").
 * @param skipText    Label for the skip link, or null to hide it.
 * @param onPrimary   Called when the user taps the primary button.
 * @param onSkip      Called when the user taps skip.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun OnboardingTipDialog(
    title: String,
    message: String,
    icon: ImageVector = Icons.Outlined.Lightbulb,
    accentColor: Color = Color(0xFF1E88E5),
    stepText: String? = null,
    primaryText: String = "Got it",
    skipText: String? = "Skip",
    onPrimary: () -> Unit,
    onSkip: (() -> Unit)? = null,
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

                // ── Icon box ───────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.20f),
                                    accentColor.copy(alpha = 0.08f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(Modifier.height(18.dp))

                // Step chip
                if (stepText != null) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = accentColor.copy(alpha = 0.10f)
                    ) {
                        Text(
                            text = stepText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            letterSpacing = 0.3.sp
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }

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

                // Gradient primary button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(accentColor, accentColor.darken(0.18f))
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.White)
                        ) { onPrimary(); dismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = primaryText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                if (skipText != null && onSkip != null) {
                    TextButton(
                        onClick = { onSkip(); dismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = skipText,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}
