package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val StarGold = Color(0xFFF59E0B)
private val StarGoldDark = Color(0xFFD97706)

private fun ratingTitle(stars: Int): String = when (stars) {
    0 -> "Enjoying the app?"
    1, 2 -> "We'd love to improve!"
    3 -> "Pretty good, right?"
    4 -> "Glad you like it!"
    5 -> "You're amazing! 🎉"
    else -> "Enjoying the app?"
}

private fun ratingSubtitle(stars: Int): String = when (stars) {
    0 -> "Tap a star to rate us on the Play Store."
    1, 2 -> "Tell us what we can do better."
    3 -> "Help us get to five stars!"
    4 -> "One more star and we're perfect!"
    5 -> "Thank you so much for your support."
    else -> "Tap a star to rate us on the Play Store."
}

/**
 * A rate-app dialog where the five stars are the visual centrepiece.
 *
 * The title and subtitle animate between different messages as the user taps each star,
 * and each selected star bounces with a spring animation. The gradient submit button
 * only appears meaningful once at least one star is selected.
 *
 * @param appName   The name of the app shown in the hero badge.
 * @param rateText  Label for the submit rating button.
 * @param laterText Label for the "Not Now" button.
 * @param neverText Label for the "Never" button.
 * @param onRate    Called with the selected star count when the user submits.
 * @param onLater   Called when the user taps "Not Now".
 * @param onNever   Called when the user taps "Never".
 */
@Composable
fun RateAppDialog(
    appName: String,
    rateText: String = "Rate Now",
    laterText: String = "Not Now",
    neverText: String = "Never",
    onRate: (stars: Int) -> Unit,
    onLater: () -> Unit,
    onNever: () -> Unit
) {
    var selectedStars by remember { mutableIntStateOf(0) }
    val starScales = remember { List(5) { Animatable(1f) } }
    val scope = rememberCoroutineScope()

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
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── App name badge ─────────────────────────────────────────
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = StarGold.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = appName,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        color = StarGoldDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.4.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ── Dynamic title ──────────────────────────────────────────
                AnimatedContent(
                    targetState = ratingTitle(selectedStars),
                    transitionSpec = {
                        (fadeIn() + scaleIn(initialScale = 0.92f)) togetherWith fadeOut()
                    },
                    label = "rating_title"
                ) { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-0.3).sp
                    )
                }

                Spacer(Modifier.height(6.dp))

                AnimatedContent(
                    targetState = ratingSubtitle(selectedStars),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "rating_subtitle"
                ) { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 21.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                // ── Stars ──────────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { star ->
                        val scale = starScales[star - 1]
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .scale(scale.value)
                                .clip(CircleShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    selectedStars = star
                                    scope.launch {
                                        (0 until star).forEach { i ->
                                            launch {
                                                delay(i * 40L)
                                                starScales[i].animateTo(1.35f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium))
                                                starScales[i].animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium))
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (star <= selectedStars) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = "$star star",
                                tint = if (star <= selectedStars) StarGold else MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ── Gradient submit button ─────────────────────────────────
                val buttonAlpha = if (selectedStars > 0) 1f else 0.45f
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    StarGold.copy(alpha = buttonAlpha),
                                    StarGoldDark.copy(alpha = buttonAlpha)
                                )
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (selectedStars > 0) { onRate(selectedStars); dismiss() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rateText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onLater(); dismiss() }) {
                        Text(laterText, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                    TextButton(onClick = { onNever(); dismiss() }) {
                        Text(neverText, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
