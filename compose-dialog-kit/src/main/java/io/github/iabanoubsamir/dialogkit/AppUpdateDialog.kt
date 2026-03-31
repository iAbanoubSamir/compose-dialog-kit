package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import io.github.iabanoubsamir.dialogkit.internal.darken

private val UpdateIndigo = Color(0xFF3949AB)

/**
 * A dialog prompting the user to install a new version of the app.
 *
 * The version number is the visual hero: it springs into view as a large pill badge.
 * Decorative scatter dots at the top give a subtle celebratory feel. The "What's New"
 * notes live in a tinted card. The gradient "Update Now" button sits full-width at the
 * bottom with "Later" as a quiet text action beneath it.
 *
 * @param newVersion  The version string of the available update (e.g. "3.2.0").
 * @param updateNotes Bullet-point list of what's new in this release.
 * @param updateText  Label for the update button.
 * @param laterText   Label for the dismiss button.
 * @param onUpdate    Called when the user taps the update button.
 * @param onLater     Called when the user taps the later button.
 */
@Composable
fun AppUpdateDialog(
    newVersion: String,
    updateNotes: List<String> = emptyList(),
    updateText: String = "Update Now",
    laterText: String = "Later",
    onUpdate: () -> Unit,
    onLater: () -> Unit
) {
    val badgeScale = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        badgeScale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    BaseDialog(onDismissRequest = onLater) { dismiss ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 20.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Scatter dots ───────────────────────────────────────────
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                ) {
                    val dots = listOf(
                        Triple(28.dp.toPx(),  16.dp.toPx(), 5f),
                        Triple(size.width - 36.dp.toPx(), 8.dp.toPx(),  4f),
                        Triple(size.width * 0.38f, 12.dp.toPx(), 3f),
                        Triple(size.width * 0.65f, 24.dp.toPx(), 5f),
                        Triple(68.dp.toPx(),  26.dp.toPx(), 3f),
                        Triple(size.width - 70.dp.toPx(), 22.dp.toPx(), 4f)
                    )
                    dots.forEachIndexed { i, (x, y, r) ->
                        drawCircle(
                            color = UpdateIndigo.copy(alpha = 0.13f + i * 0.03f),
                            radius = r.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }

                // ── Spring-animated version badge ──────────────────────────
                Box(
                    modifier = Modifier.scale(badgeScale.value)
                ) {
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = UpdateIndigo
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SystemUpdate,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "v$newVersion",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                letterSpacing = (-0.3).sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Update Available",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.3).sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "A new version is ready to install.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // ── What's New card ────────────────────────────────────────
                if (updateNotes.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = UpdateIndigo.copy(alpha = 0.07f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "What's New",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = UpdateIndigo,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            updateNotes.forEach { note ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(UpdateIndigo)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = note,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ── Gradient update button ─────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(UpdateIndigo, UpdateIndigo.darken(0.22f))
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.White)
                        ) { onUpdate(); dismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = updateText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.2.sp
                    )
                }

                TextButton(
                    onClick = { onLater(); dismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = laterText,
                        color = UpdateIndigo,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
