package io.github.iabanoubsamir.dialogkit

import android.graphics.Color as AndroidColor
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

private const val MEDIA_EXIT_MS = 200L
private val CameraBlue = Color(0xFF1E88E5)
private val CameraBlueLight = Color(0xFF42A5F5)
private val GalleryPurple = Color(0xFF7B1FA2)
private val GalleryPurpleLight = Color(0xFFAB47BC)

/**
 * A bottom-anchored media source picker with large, tappable camera and gallery cards.
 *
 * This is a purpose-built alternative to building a generic [ActionSheetDialog] every time
 * you need photo input. The two cards are visually weighted equally so neither option feels
 * like an afterthought.
 *
 * @param title       Optional title shown above the cards.
 * @param cameraText  Label under the camera card.
 * @param galleryText Label under the gallery card.
 * @param cancelText  Label for the cancel button.
 * @param onCamera    Called when the user picks camera.
 * @param onGallery   Called when the user picks gallery.
 * @param onDismiss   Called when the sheet is dismissed.
 */
@Composable
fun MediaPickerDialog(
    title: String? = null,
    cameraText: String = "Camera",
    galleryText: String = "Gallery",
    cancelText: String = "Cancel",
    onCamera: () -> Unit,
    onGallery: () -> Unit,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismiss: () -> Unit = remember(scope, onDismiss) {
        {
            scope.launch {
                visible = false
                delay(MEDIA_EXIT_MS)
                onDismiss()
            }
        }
    }

    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val view = LocalView.current
        val windowProvider = view.parent as? DialogWindowProvider
        SideEffect {
            windowProvider?.window?.apply {
                setGravity(Gravity.BOTTOM)
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
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
                // ── Main card ──────────────────────────────────────────────
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 12.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // Handle bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 36.dp, height = 4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f)
                                    )
                            )
                        }

                        if (title != null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 14.dp)
                            )
                        }

                        // ── Camera + Gallery cards ─────────────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Camera card
                            MediaCard(
                                label = cameraText,
                                gradient = Brush.linearGradient(
                                    colors = listOf(CameraBlueLight, CameraBlue),
                                    start = Offset(0f, 0f),
                                    end = Offset(
                                        Float.POSITIVE_INFINITY,
                                        Float.POSITIVE_INFINITY
                                    )
                                ),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    scope.launch {
                                        onCamera()
                                        visible = false
                                        delay(MEDIA_EXIT_MS)
                                        onDismiss()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Camera,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            // Gallery card
                            MediaCard(
                                label = galleryText,
                                gradient = Brush.linearGradient(
                                    colors = listOf(GalleryPurpleLight, GalleryPurple),
                                    start = Offset(0f, 0f),
                                    end = Offset(
                                        Float.POSITIVE_INFINITY,
                                        Float.POSITIVE_INFINITY
                                    )
                                ),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    scope.launch {
                                        onGallery()
                                        visible = false
                                        delay(MEDIA_EXIT_MS)
                                        onDismiss()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PhotoLibrary,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
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

@Composable
private fun MediaCard(
    label: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(gradient)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White)
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            icon()
            Spacer(Modifier.height(10.dp))
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}
