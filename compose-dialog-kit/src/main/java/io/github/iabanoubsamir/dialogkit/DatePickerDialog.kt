package io.github.iabanoubsamir.dialogkit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
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
import java.text.DateFormatSymbols
import java.util.Calendar

private val DAY_LABELS = arrayOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")

private fun daysInMonth(month: Int, year: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    else -> 0
}

private fun firstDayOfWeek(month: Int, year: Int): Int {
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, 1)
    return cal.get(Calendar.DAY_OF_WEEK) - 1 // 0=Sun … 6=Sat
}

/**
 * A fully custom date picker dialog with a calendar month grid.
 *
 * Navigates between months with chevron arrows. Today's date is marked with a dot.
 * The selected date is highlighted with a filled circle in [accentColor].
 *
 * @param title        Dialog heading.
 * @param accentColor  Color used for the selected day circle and confirm button.
 * @param confirmText  Label for the confirm button.
 * @param dismissText  Label for the cancel button.
 * @param onConfirm    Called with (day, month 1-12, year) when confirmed.
 * @param onDismiss    Called when the dialog is dismissed.
 */
@Composable
fun DatePickerDialog(
    title: String = "Select Date",
    accentColor: Color = Color(0xFF1E88E5),
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: (day: Int, month: Int, year: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val monthNames = remember { DateFormatSymbols.getInstance().months.take(12) }
    val today = remember { Calendar.getInstance() }
    val todayDay = today.get(Calendar.DAY_OF_MONTH)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayYear = today.get(Calendar.YEAR)

    var displayMonth by remember { mutableIntStateOf(todayMonth) }
    var displayYear by remember { mutableIntStateOf(todayYear) }
    var selectedDay by remember { mutableIntStateOf(todayDay) }
    var selectedMonth by remember { mutableIntStateOf(todayMonth) }
    var selectedYear by remember { mutableIntStateOf(todayYear) }

    // Track direction for slide animation
    var slideDirection by remember { mutableIntStateOf(1) }

    fun prevMonth() {
        slideDirection = -1
        if (displayMonth == 1) { displayMonth = 12; displayYear-- } else displayMonth--
    }

    fun nextMonth() {
        slideDirection = 1
        if (displayMonth == 12) { displayMonth = 1; displayYear++ } else displayMonth++
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
            Column(modifier = Modifier.padding(20.dp)) {

                // ── Title ──────────────────────────────────────────────────
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.3).sp
                )

                Spacer(Modifier.height(16.dp))

                // ── Month / year navigation ────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = ::prevMonth, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Outlined.ChevronLeft,
                            contentDescription = "Previous month",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    AnimatedContent(
                        targetState = displayMonth to displayYear,
                        transitionSpec = {
                            slideInHorizontally { it * slideDirection } togetherWith
                                slideOutHorizontally { -it * slideDirection }
                        },
                        modifier = Modifier.weight(1f),
                        label = "month_nav"
                    ) { (month, year) ->
                        Text(
                            text = "${monthNames[month - 1]} $year",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    IconButton(onClick = ::nextMonth, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Outlined.ChevronRight,
                            contentDescription = "Next month",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ── Day-of-week headers ────────────────────────────────────
                Row(modifier = Modifier.fillMaxWidth()) {
                    DAY_LABELS.forEach { label ->
                        Text(
                            text = label,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                // ── Calendar grid ──────────────────────────────────────────
                AnimatedContent(
                    targetState = displayMonth to displayYear,
                    transitionSpec = {
                        slideInHorizontally { it * slideDirection } togetherWith
                            slideOutHorizontally { -it * slideDirection }
                    },
                    label = "calendar_grid"
                ) { (month, year) ->
                    val firstDay = firstDayOfWeek(month, year)
                    val days = daysInMonth(month, year)
                    val totalCells = firstDay + days
                    val rows = (totalCells + 6) / 7

                    Column {
                        repeat(rows) { row ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                repeat(7) { col ->
                                    val cellIndex = row * 7 + col
                                    val day = cellIndex - firstDay + 1
                                    val isValidDay = day in 1..days
                                    val isSelected = isValidDay &&
                                        day == selectedDay &&
                                        month == selectedMonth &&
                                        year == selectedYear
                                    val isToday = isValidDay &&
                                        day == todayDay &&
                                        month == todayMonth &&
                                        year == todayYear

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isSelected -> accentColor
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .then(
                                                if (isValidDay) Modifier.clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null
                                                ) {
                                                    selectedDay = day
                                                    selectedMonth = month
                                                    selectedYear = year
                                                } else Modifier
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isValidDay) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = "$day",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                    color = when {
                                                        isSelected -> Color.White
                                                        isToday -> accentColor
                                                        else -> MaterialTheme.colorScheme.onSurface
                                                    }
                                                )
                                                // Today dot
                                                if (isToday && !isSelected) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(4.dp)
                                                            .clip(CircleShape)
                                                            .background(accentColor)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── Buttons ────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    OutlinedButton(
                        onClick = { dismiss() },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 14.dp)
                    ) {
                        Text(dismissText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Spacer(Modifier.size(12.dp))
                    Button(
                        onClick = { onConfirm(selectedDay, selectedMonth, selectedYear); dismiss() },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
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
