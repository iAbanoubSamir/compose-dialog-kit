package io.github.iabanoubsamir.dialogkit

import android.graphics.Color as AndroidColor
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import io.github.iabanoubsamir.dialogkit.internal.bottomSheetEnterTransition
import io.github.iabanoubsamir.dialogkit.internal.bottomSheetExitTransition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val EXIT_MS = 200L
private val DestructiveRed = Color(0xFFE53935)

/**
 * An item displayed inside an [ActionSheetDialog].
 *
 * @param label         The text label for this action.
 * @param icon          Optional leading icon shown inside a rounded-square container.
 * @param color         Tint color for the icon container. Null uses the theme's
 *                      [MaterialTheme.colorScheme.secondaryContainer] palette.
 * @param isDestructive If true, the icon container and label are rendered in red.
 * @param onClick       Called when the user taps this item.
 */
data class ActionSheetItem(
    val label: String,
    val icon: ImageVector? = null,
    val color: Color? = null,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)

/**
 * A bottom-anchored action sheet with a premium, modern feel.
 *
 * Design highlights:
 * - Handle bar pill at the top signals "slideable" affordance.
 * - Each action has a rounded-square icon container (like iOS app shortcuts) with an
 *   optional per-item [ActionSheetItem.color] tint.
 * - No full-width dividers — whitespace and padding do the separation work.
 * - Destructive items receive a red-tinted icon box and red label.
 * - The cancel card uses [MaterialTheme.colorScheme.secondaryContainer] to read as a
 *   real button rather than plain text on a surface.
 *
 * @param title      Optional sheet title shown above the action list.
 * @param items      The list of [ActionSheetItem]s to display.
 * @param cancelText Label for the cancel button.
 * @param onDismiss  Called when the sheet is dismissed.
 */
@Composable
fun ActionSheetDialog(
    title: String? = null,
    items: List<ActionSheetItem>,
    cancelText: String = "Cancel",
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismiss: () -> Unit = remember(scope, onDismiss) {
        {
            scope.launch {
                visible = false
                delay(EXIT_MS)
                onDismiss()
            }
        }
    }

    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        val view = LocalView.current
        val windowProvider = view.parent as? DialogWindowProvider
        SideEffect {
            windowProvider?.window?.apply {
                setGravity(Gravity.BOTTOM)
                setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawable(ColorDrawable(AndroidColor.TRANSPARENT))
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                attributes = attributes.apply { dimAmount = 0.45f }
            }
        }

        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = bottomSheetEnterTransition,
            exit = bottomSheetExitTransition
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
            ) {

                // ── Main sheet card ────────────────────────────────────────
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 12.dp
                ) {
                    Column {

                        // Handle bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 36.dp, height = 4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f))
                            )
                        }

                        // Title
                        if (title != null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                                thickness = 0.5.dp
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        // Items
                        items.forEach { item ->
                            val isDestructive = item.isDestructive
                            val iconBg = when {
                                isDestructive -> DestructiveRed.copy(alpha = 0.10f)
                                item.color != null -> item.color.copy(alpha = 0.13f)
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            }
                            val iconTint = when {
                                isDestructive -> DestructiveRed
                                item.color != null -> item.color
                                else -> MaterialTheme.colorScheme.onSecondaryContainer
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple()
                                    ) {
                                        scope.launch {
                                            item.onClick()
                                            visible = false
                                            delay(EXIT_MS)
                                            onDismiss()
                                        }
                                    }
                                    .padding(horizontal = 16.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (item.icon != null) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(iconBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = null,
                                            tint = iconTint,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(14.dp))
                                }
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (isDestructive) DestructiveRed else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isDestructive) FontWeight.Medium else FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(4.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ── Cancel card ────────────────────────────────────────────
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple()
                            ) { dismiss() }
                            .padding(vertical = 17.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cancelText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
