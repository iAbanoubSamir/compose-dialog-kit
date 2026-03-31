package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import io.github.iabanoubsamir.dialogkit.internal.darken
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlinx.coroutines.launch

private val SuccessGreen = Color(0xFF2E7D32)
private val SuccessGreenLight = Color(0xFF43A047)

/**
 * A success dialog with a full-width green gradient header, an animated draw-on
 * checkmark, and particle dots that burst outward on entry.
 *
 * @param title       Dialog heading.
 * @param message     Supporting description of what succeeded.
 * @param dismissText Label for the dismiss button.
 * @param onDismiss   Called when the user taps the button or outside the dialog.
 */
@Composable
fun SuccessDialog(
    title: String = "Success!",
    message: String,
    dismissText: String = "Done",
    onDismiss: () -> Unit
) {
    val checkProgress = remember { Animatable(0f) }
    val headerScale = remember { Animatable(0.7f) }
    // 6 particle dots: (angle-degrees, distance-fraction)
    val particleAlphas = remember { List(6) { Animatable(0f) } }
    val particleOffsets = remember { List(6) { Animatable(0f) } }

    LaunchedEffect(Unit) {
        // Header icon scales in
        launch {
            headerScale.animateTo(
                1f,
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
            )
        }
        // Checkmark draws on
        launch {
            checkProgress.animateTo(1f, animationSpec = tween(550, easing = EaseOut))
        }
        // Particles burst outward
        particleAlphas.forEachIndexed { i, anim ->
            launch {
                anim.animateTo(1f, animationSpec = tween(200))
                anim.animateTo(0f, animationSpec = tween(400))
            }
        }
        particleOffsets.forEach { anim ->
            launch { anim.animateTo(1f, animationSpec = tween(550, easing = EaseOut)) }
        }
    }

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

                // ── Green gradient header ──────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SuccessGreenLight, SuccessGreen),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Particle burst + checkmark circle
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val angles = listOf(0f, 60f, 120f, 180f, 240f, 300f)
                        Canvas(modifier = Modifier.matchParentSize()) {
                            val center = Offset(size.width / 2f, size.height / 2f)
                            // Decorative bg circle
                            drawCircle(Color.White.copy(alpha = 0.15f), radius = 52.dp.toPx(), center = center)
                            // Particles
                            angles.forEachIndexed { i, angleDeg ->
                                val rad = angleDeg * PI / 180.0
                                val dist = 44.dp.toPx() * particleOffsets[i].value
                                val px = center.x + (dist * cos(rad)).toFloat()
                                val py = center.y + (dist * sin(rad)).toFloat()
                                drawCircle(
                                    color = Color.White.copy(alpha = particleAlphas[i].value * 0.85f),
                                    radius = 4.dp.toPx(),
                                    center = Offset(px, py)
                                )
                            }
                        }

                        // Checkmark circle
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .scale(headerScale.value)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val progress = checkProgress.value
                            Canvas(modifier = Modifier.size(44.dp)) {
                                val sw = 3.2.dp.toPx()
                                val w = size.width; val h = size.height
                                val p1 = Offset(w * 0.14f, h * 0.50f)
                                val p2 = Offset(w * 0.42f, h * 0.74f)
                                val p3 = Offset(w * 0.86f, h * 0.27f)
                                val seg1 = hypot((p2.x - p1.x).toDouble(), (p2.y - p1.y).toDouble()).toFloat()
                                val seg2 = hypot((p3.x - p2.x).toDouble(), (p3.y - p2.y).toDouble()).toFloat()
                                val total = seg1 + seg2
                                val drawn = progress * total
                                if (drawn > 0f) {
                                    if (drawn <= seg1) {
                                        val t = drawn / seg1
                                        drawLine(Color.White, p1, Offset(p1.x + (p2.x - p1.x) * t, p1.y + (p2.y - p1.y) * t), sw, StrokeCap.Round)
                                    } else {
                                        drawLine(Color.White, p1, p2, sw, StrokeCap.Round)
                                        val t = (drawn - seg1) / seg2
                                        drawLine(Color.White, p2, Offset(p2.x + (p3.x - p2.x) * t, p2.y + (p3.y - p2.y) * t), sw, StrokeCap.Round)
                                    }
                                }
                            }
                        }
                    }
                }

                // ── Content ────────────────────────────────────────────────
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = SuccessGreen,
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(SuccessGreenLight, SuccessGreen)
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = Color.White)
                            ) { dismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
