package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import java.util.Calendar

/**
 * A custom time picker dialog with scrollable hour and minute wheels.
 *
 * Each column displays the current value with up/down arrow buttons and an animated
 * slide transition. Supports 12-hour (with AM/PM toggle) and 24-hour modes.
 *
 * @param title       Dialog heading.
 * @param accentColor Color used for the arrows, AM/PM highlight, and confirm button.
 * @param is24Hour    If true shows 00–23. If false shows 1–12 with AM/PM toggle.
 * @param confirmText Label for the confirm button.
 * @param dismissText Label for the cancel button.
 * @param onConfirm   Called with (hour 0–23, minute 0–59) when confirmed.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    accentColor: Color = Color(0xFF7B1FA2),
    is24Hour: Boolean = false,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val now = remember { Calendar.getInstance() }
    val initialHour24 = now.get(Calendar.HOUR_OF_DAY)
    val initialMinute = now.get(Calendar.MINUTE)

    val initialDisplayHour = if (is24Hour) initialHour24 else when {
        initialHour24 == 0 -> 12
        initialHour24 > 12 -> initialHour24 - 12
        else -> initialHour24
    }

    var hour by remember { mutableIntStateOf(initialDisplayHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }
    var isAm by remember { mutableStateOf(initialHour24 < 12) }
    var slideHourUp by remember { mutableStateOf(true) }
    var slideMinuteUp by remember { mutableStateOf(true) }

    BaseDialog(onDismissRequest = onDismiss) { dismiss ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 20.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.3).sp
                )

                Spacer(Modifier.height(20.dp))

                // ── Hour : Minute wheels ────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimeWheelColumn(
                        value = hour,
                        slideUp = slideHourUp,
                        accentColor = accentColor,
                        onUp = {
                            slideHourUp = true
                            hour = if (is24Hour) (hour + 1) % 24 else hour % 12 + 1
                        },
                        onDown = {
                            slideHourUp = false
                            hour = if (is24Hour) (hour + 23) % 24 else if (hour == 1) 12 else hour - 1
                        }
                    )

                    Text(
                        text = ":",
                        fontSize = 46.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .padding(bottom = 12.dp)
                    )

                    TimeWheelColumn(
                        value = minute,
                        slideUp = slideMinuteUp,
                        accentColor = accentColor,
                        onUp = { slideMinuteUp = true; minute = (minute + 1) % 60 },
                        onDown = { slideMinuteUp = false; minute = (minute + 59) % 60 }
                    )
                }

                // ── AM/PM toggle (12h only) ──────────────────────────────────
                if (!is24Hour) {
                    Spacer(Modifier.height(12.dp))
                    AmPmToggle(
                        isAm = isAm,
                        accentColor = accentColor,
                        onAmSelected = { isAm = true },
                        onPmSelected = { isAm = false }
                    )
                }

                Spacer(Modifier.height(24.dp))

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
                        onClick = {
                            val finalHour = when {
                                is24Hour -> hour
                                isAm && hour == 12 -> 0
                                !isAm && hour != 12 -> hour + 12
                                else -> hour
                            }
                            onConfirm(finalHour, minute)
                            dismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = Color.White
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
private fun TimeWheelColumn(
    value: Int,
    slideUp: Boolean,
    accentColor: Color,
    onUp: () -> Unit,
    onDown: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onUp, modifier = Modifier.size(44.dp)) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Increase",
                tint = accentColor,
                modifier = Modifier.size(32.dp)
            )
        }

        AnimatedContent(
            targetState = value,
            transitionSpec = {
                if (slideUp) {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                } else {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }
            },
            label = "time_wheel"
        ) { v ->
            Text(
                text = "%02d".format(v),
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                letterSpacing = (-1).sp,
                modifier = Modifier.width(76.dp)
            )
        }

        IconButton(onClick = onDown, modifier = Modifier.size(44.dp)) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Decrease",
                tint = accentColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun AmPmToggle(
    isAm: Boolean,
    accentColor: Color,
    onAmSelected: () -> Unit,
    onPmSelected: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            AmPmChip(
                label = "AM",
                selected = isAm,
                accentColor = accentColor,
                isStart = true,
                modifier = Modifier.weight(1f),
                onClick = onAmSelected
            )
            Spacer(Modifier.width(4.dp))
            AmPmChip(
                label = "PM",
                selected = !isAm,
                accentColor = accentColor,
                isStart = false,
                modifier = Modifier.weight(1f),
                onClick = onPmSelected
            )
        }
    }
}

@Composable
private fun AmPmChip(
    label: String,
    selected: Boolean,
    accentColor: Color,
    isStart: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cornerRadius = 10.dp
    val shape = if (isStart) {
        RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius, topEnd = 4.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius, topStart = 4.dp, bottomStart = 4.dp)
    }
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(shape)
            .background(if (selected) accentColor else Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp
        )
    }
}
