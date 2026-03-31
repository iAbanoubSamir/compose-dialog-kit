package io.github.iabanoubsamir.dialogkit.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val EXIT_DURATION_MS = 200L

internal val defaultEnterTransition: EnterTransition =
    scaleIn(
        initialScale = 0.82f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    ) + fadeIn(animationSpec = tween(180))

internal val defaultExitTransition: ExitTransition =
    scaleOut(targetScale = 0.9f, animationSpec = tween(EXIT_DURATION_MS.toInt())) +
        fadeOut(animationSpec = tween(EXIT_DURATION_MS.toInt()))

internal val bottomSheetEnterTransition: EnterTransition =
    slideInVertically(
        initialOffsetY = { it },
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
    ) + fadeIn(animationSpec = tween(200))

internal val bottomSheetExitTransition: ExitTransition =
    slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(EXIT_DURATION_MS.toInt())
    ) + fadeOut(animationSpec = tween(EXIT_DURATION_MS.toInt()))

@Composable
internal fun BaseDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    enterTransition: EnterTransition = defaultEnterTransition,
    exitTransition: ExitTransition = defaultExitTransition,
    content: @Composable (dismiss: () -> Unit) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismiss: () -> Unit = remember(scope, onDismissRequest) {
        {
            scope.launch {
                visible = false
                delay(EXIT_DURATION_MS)
                onDismissRequest()
            }
        }
    }

    LaunchedEffect(Unit) { visible = true }

    Dialog(onDismissRequest = dismiss, properties = properties) {
        AnimatedVisibility(visible = visible, enter = enterTransition, exit = exitTransition) {
            content(dismiss)
        }
    }
}
