package io.github.iabanoubsamir.dialogkit

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog

private val HUE_GRADIENT_COLORS = listOf(
    Color.Red,
    Color(0xFFFF7F00),
    Color.Yellow,
    Color.Green,
    Color.Cyan,
    Color.Blue,
    Color(0xFF8B00FF),
    Color.Red
)

private fun hsvToColor(hue: Float, saturation: Float, value: Float): Color =
    Color(AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value)))

/**
 * A custom HSV color picker dialog with interactive gradient sliders.
 *
 * Three sliders let the user adjust Hue (0–360°), Saturation (0–100%),
 * and Brightness (0–100%). A live preview circle and hex code update in real time.
 * The confirm button adopts the selected color as its background.
 *
 * @param initialColor Starting color, converted to HSV on first composition.
 * @param title        Dialog heading.
 * @param confirmText  Label for the confirm button.
 * @param dismissText  Label for the cancel button.
 * @param onConfirm    Called with the selected [Color] when confirmed.
 * @param onDismiss    Called when the dialog is dismissed.
 */
@Composable
fun ColorPickerDialog(
    initialColor: Color = Color(0xFF1E88E5),
    title: String = "Pick a Color",
    confirmText: String = "Select",
    dismissText: String = "Cancel",
    onConfirm: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val initialHsv = remember {
        FloatArray(3).also { hsv ->
            AndroidColor.RGBToHSV(
                (initialColor.red * 255).toInt(),
                (initialColor.green * 255).toInt(),
                (initialColor.blue * 255).toInt(),
                hsv
            )
        }
    }

    var hue by remember { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember { mutableFloatStateOf(initialHsv[1]) }
    var brightness by remember { mutableFloatStateOf(initialHsv[2]) }

    val selectedColor = remember(hue, saturation, brightness) {
        hsvToColor(hue, saturation, brightness)
    }
    val pureHueColor = remember(hue) { hsvToColor(hue, 1f, 1f) }

    val hex = remember(selectedColor) {
        "#%02X%02X%02X".format(
            (selectedColor.red * 255).toInt(),
            (selectedColor.green * 255).toInt(),
            (selectedColor.blue * 255).toInt()
        )
    }

    val buttonTextColor = remember(selectedColor) {
        val lum = 0.299f * selectedColor.red + 0.587f * selectedColor.green + 0.114f * selectedColor.blue
        if (lum > 0.6f) Color.Black else Color.White
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
            Column(modifier = Modifier.padding(24.dp)) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.3).sp
                )

                Spacer(Modifier.height(20.dp))

                // ── Color preview + hex display ─────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(selectedColor)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = CircleShape
                            )
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = hex,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 1.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "H ${hue.toInt()}°  S ${(saturation * 100).toInt()}%  B ${(brightness * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ── Hue slider ──────────────────────────────────────────────
                SliderLabel("Hue")
                ColorSlider(
                    value = hue / 360f,
                    gradient = HUE_GRADIENT_COLORS,
                    thumbColor = pureHueColor,
                    onValueChange = { hue = it * 360f }
                )

                Spacer(Modifier.height(16.dp))

                // ── Saturation slider ───────────────────────────────────────
                SliderLabel("Saturation")
                ColorSlider(
                    value = saturation,
                    gradient = listOf(
                        hsvToColor(hue, 0f, brightness.coerceAtLeast(0.15f)),
                        pureHueColor
                    ),
                    thumbColor = selectedColor,
                    onValueChange = { saturation = it }
                )

                Spacer(Modifier.height(16.dp))

                // ── Brightness slider ───────────────────────────────────────
                SliderLabel("Brightness")
                ColorSlider(
                    value = brightness,
                    gradient = listOf(
                        Color.Black,
                        hsvToColor(hue, saturation.coerceAtLeast(0.15f), 1f)
                    ),
                    thumbColor = selectedColor,
                    onValueChange = { brightness = it }
                )

                Spacer(Modifier.height(28.dp))

                // ── Buttons ─────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    OutlinedButton(
                        onClick = { dismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(dismissText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Spacer(Modifier.size(12.dp))
                    Button(
                        onClick = { onConfirm(selectedColor); dismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = selectedColor,
                            contentColor = buttonTextColor
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(confirmText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SliderLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ColorSlider(
    value: Float,
    gradient: List<Color>,
    thumbColor: Color,
    onValueChange: (Float) -> Unit
) {
    val thumbRadius = 12.dp
    val trackHeight = 16.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thumbRadius * 2)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    down.consume()
                    onValueChange((down.position.x / size.width.toFloat()).coerceIn(0f, 1f))
                    drag(down.id) { change ->
                        change.consume()
                        onValueChange((change.position.x / size.width.toFloat()).coerceIn(0f, 1f))
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val centerY = size.height / 2f
            val tr = thumbRadius.toPx()
            val th = trackHeight.toPx()
            val trackLeft = tr
            val trackWidth = size.width - 2f * tr

            // Gradient track
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = gradient,
                    start = Offset(trackLeft, centerY),
                    end = Offset(trackLeft + trackWidth, centerY)
                ),
                topLeft = Offset(trackLeft, centerY - th / 2f),
                size = Size(trackWidth, th),
                cornerRadius = CornerRadius(th / 2f)
            )

            // Thumb: white outer ring + colored fill + subtle border
            val thumbX = trackLeft + value * trackWidth
            drawCircle(color = Color.White, radius = tr, center = Offset(thumbX, centerY))
            drawCircle(
                color = thumbColor,
                radius = tr - 3.dp.toPx(),
                center = Offset(thumbX, centerY)
            )
            drawCircle(
                color = Color.Black.copy(alpha = 0.12f),
                radius = tr,
                center = Offset(thumbX, centerY),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}
