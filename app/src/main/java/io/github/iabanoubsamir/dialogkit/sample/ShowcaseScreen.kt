package io.github.iabanoubsamir.dialogkit.sample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Downloading
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PermMedia
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.iabanoubsamir.dialogkit.ActionSheetDialog
import io.github.iabanoubsamir.dialogkit.ActionSheetItem
import io.github.iabanoubsamir.dialogkit.AlertDialog
import io.github.iabanoubsamir.dialogkit.AppUpdateDialog
import io.github.iabanoubsamir.dialogkit.BiometricDialog
import io.github.iabanoubsamir.dialogkit.ColorPickerDialog
import io.github.iabanoubsamir.dialogkit.CookieConsentDialog
import io.github.iabanoubsamir.dialogkit.CountdownDialog
import io.github.iabanoubsamir.dialogkit.DatePickerDialog
import io.github.iabanoubsamir.dialogkit.DeleteDialog
import io.github.iabanoubsamir.dialogkit.ErrorDialog
import io.github.iabanoubsamir.dialogkit.FeedbackDialog
import io.github.iabanoubsamir.dialogkit.InputDialog
import io.github.iabanoubsamir.dialogkit.LoadingDialog
import io.github.iabanoubsamir.dialogkit.LogoutDialog
import io.github.iabanoubsamir.dialogkit.MediaPickerDialog
import io.github.iabanoubsamir.dialogkit.MultiSelectDialog
import io.github.iabanoubsamir.dialogkit.NoInternetDialog
import io.github.iabanoubsamir.dialogkit.OnboardingTipDialog
import io.github.iabanoubsamir.dialogkit.PermissionRationaleDialog
import io.github.iabanoubsamir.dialogkit.ProgressDialog
import io.github.iabanoubsamir.dialogkit.RateAppDialog
import io.github.iabanoubsamir.dialogkit.SessionExpiredDialog
import io.github.iabanoubsamir.dialogkit.SingleSelectDialog
import io.github.iabanoubsamir.dialogkit.SubscriptionDialog
import io.github.iabanoubsamir.dialogkit.SuccessDialog
import io.github.iabanoubsamir.dialogkit.TimePickerDialog
import io.github.iabanoubsamir.dialogkit.WarningDialog

// ── Data ──────────────────────────────────────────────────────────────────────

private data class DialogEntry(
    val name: String,
    val description: String,
    val color: Color,
    val gradientEnd: Color,
    val icon: ImageVector
)

private val dialogEntries = listOf(
    DialogEntry("Alert", "General info", Color(0xFF1E88E5), Color(0xFF1565C0), Icons.Outlined.Info),
    DialogEntry(
        "Warning",
        "Risky action",
        Color(0xFFF59E0B),
        Color(0xFFD97706),
        Icons.Outlined.Warning
    ),
    DialogEntry(
        "Delete",
        "Destructive action",
        Color(0xFFE53935),
        Color(0xFFB71C1C),
        Icons.Outlined.Delete
    ),
    DialogEntry(
        "App Update",
        "New version ready",
        Color(0xFF3949AB),
        Color(0xFF1A237E),
        Icons.Outlined.SystemUpdate
    ),
    DialogEntry(
        "Rate App",
        "Star rating prompt",
        Color(0xFFF59E0B),
        Color(0xFFE65100),
        Icons.Outlined.Star
    ),
    DialogEntry(
        "Action Sheet",
        "Bottom sheet menu",
        Color(0xFF00897B),
        Color(0xFF004D40),
        Icons.Outlined.MoreHoriz
    ),
    DialogEntry(
        "Input",
        "Text input prompt",
        Color(0xFF1E88E5),
        Color(0xFF0D47A1),
        Icons.Outlined.Edit
    ),
    DialogEntry(
        "Success",
        "Animated checkmark",
        Color(0xFF43A047),
        Color(0xFF1B5E20),
        Icons.Outlined.CheckCircle
    ),
    DialogEntry(
        "Error",
        "Failure + retry",
        Color(0xFFE53935),
        Color(0xFF7F0000),
        Icons.Outlined.ErrorOutline
    ),
    DialogEntry(
        "Loading",
        "Blocking progress",
        Color(0xFF8E24AA),
        Color(0xFF4A148C),
        Icons.Outlined.HourglassTop
    ),
    DialogEntry(
        "Permission",
        "Permission ask",
        Color(0xFF8E24AA),
        Color(0xFF4A148C),
        Icons.Outlined.Security
    ),
    DialogEntry(
        "No Internet",
        "Connectivity lost",
        Color(0xFF546E7A),
        Color(0xFF263238),
        Icons.Outlined.WifiOff
    ),
    DialogEntry(
        "Logout",
        "Session end",
        Color(0xFFF4511E),
        Color(0xFFBF360C),
        Icons.AutoMirrored.Outlined.Logout
    ),
    DialogEntry(
        "Progress",
        "Determinate step",
        Color(0xFF0097A7),
        Color(0xFF006064),
        Icons.Outlined.Downloading
    ),
    DialogEntry(
        "Multi Select",
        "Checkbox list",
        Color(0xFF3949AB),
        Color(0xFF283593),
        Icons.Outlined.CheckBox
    ),
    DialogEntry(
        "Single Select",
        "Radio list",
        Color(0xFF6D4C9F),
        Color(0xFF4527A0),
        Icons.Outlined.RadioButtonChecked
    ),
    DialogEntry(
        "Session Expired",
        "Auth timeout",
        Color(0xFF283593),
        Color(0xFF1A237E),
        Icons.Outlined.Lock
    ),
    DialogEntry(
        "Cookie Consent",
        "GDPR privacy",
        Color(0xFFF57C00),
        Color(0xFFE65100),
        Icons.Outlined.Cookie
    ),
    DialogEntry(
        "Subscription",
        "Premium upsell",
        Color(0xFF7C4DFF),
        Color(0xFF4527A0),
        Icons.Outlined.WorkspacePremium
    ),
    DialogEntry(
        "Feedback",
        "In-app feedback",
        Color(0xFF00897B),
        Color(0xFF00695C),
        Icons.Outlined.RateReview
    ),
    DialogEntry(
        "Countdown",
        "Auto-dismiss timer",
        Color(0xFFE53935),
        Color(0xFFC62828),
        Icons.Outlined.Timer
    ),
    DialogEntry(
        "Onboarding Tip",
        "Feature spotlight",
        Color(0xFF1E88E5),
        Color(0xFF1565C0),
        Icons.Outlined.Lightbulb
    ),
    DialogEntry(
        "Media Picker",
        "Camera or gallery",
        Color(0xFF00897B),
        Color(0xFF004D40),
        Icons.Outlined.PermMedia
    ),
    DialogEntry(
        "Date Picker",
        "Calendar date pick",
        Color(0xFF1E88E5),
        Color(0xFF0D47A1),
        Icons.Outlined.CalendarToday
    ),
    DialogEntry(
        "Time Picker",
        "Hour & minute pick",
        Color(0xFF7B1FA2),
        Color(0xFF4A148C),
        Icons.Outlined.AccessTime
    ),
    DialogEntry(
        "Biometric",
        "Fingerprint auth",
        Color(0xFF00897B),
        Color(0xFF004D40),
        Icons.Outlined.Fingerprint
    ),
    DialogEntry(
        "Color Picker",
        "HSV color picker",
        Color(0xFF1E88E5),
        Color(0xFF0D47A1),
        Icons.Outlined.Palette
    )
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
internal fun ShowcaseScreen() {
    var activeDialog by remember { mutableStateOf<String?>(null) }
    var multiSelected by remember { mutableStateOf(setOf("Push Notifications")) }
    var singleSelected by remember { mutableStateOf<String?>("System Default") }
    var pickedColor by remember { mutableStateOf(Color(0xFF1E88E5)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                HeroSection()
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Tap any card to preview",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }

            items(dialogEntries) { entry ->
                DialogCard(entry = entry, onClick = { activeDialog = entry.name })
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(Modifier.navigationBarsPadding())
            }
        }

        // ── Active dialog ─────────────────────────────────────────────────
        when (activeDialog) {
            "Alert" -> AlertDialog(
                title = "Account Verified",
                message = "Your email address has been successfully verified. You now have full access to all features.",
                confirmText = "Great!",
                dismissText = "Dismiss",
                onConfirm = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Warning" -> WarningDialog(
                title = "Unsaved Changes",
                message = "You have unsaved changes that will be lost if you leave this page. Are you sure you want to proceed?",
                confirmText = "Leave Anyway",
                dismissText = "Stay",
                onConfirm = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Delete" -> DeleteDialog(
                title = "Delete Photo?",
                message = "This photo will be permanently deleted from your library. This action cannot be undone.",
                onConfirm = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "App Update" -> AppUpdateDialog(
                newVersion = "3.2.0",
                updateNotes = listOf(
                    "Dark mode improvements",
                    "Faster startup time",
                    "Bug fixes and stability"
                ),
                onUpdate = { activeDialog = null },
                onLater = { activeDialog = null }
            )

            "Rate App" -> RateAppDialog(
                appName = "DialogKit",
                onRate = { activeDialog = null },
                onLater = { activeDialog = null },
                onNever = { activeDialog = null }
            )

            "Action Sheet" -> ActionSheetDialog(
                title = "Photo Options",
                items = listOf(
                    ActionSheetItem(
                        "Take Photo",
                        Icons.Outlined.Camera,
                        Color(0xFF1E88E5)
                    ) { activeDialog = null },
                    ActionSheetItem(
                        "Choose from Library",
                        Icons.Outlined.PhotoLibrary,
                        Color(0xFF8E24AA)
                    ) { activeDialog = null },
                    ActionSheetItem(
                        "Share",
                        Icons.Outlined.Share,
                        Color(0xFF00897B)
                    ) { activeDialog = null },
                    ActionSheetItem("Edit", Icons.Outlined.Edit, Color(0xFFF59E0B)) {
                        activeDialog = null
                    },
                    ActionSheetItem(
                        "Delete Photo",
                        Icons.Outlined.Delete,
                        isDestructive = true
                    ) { activeDialog = null }
                ),
                onDismiss = { activeDialog = null }
            )

            "Input" -> InputDialog(
                title = "Rename File",
                message = "Enter a new name for this file.",
                hint = "File name",
                initialValue = "document_final",
                confirmText = "Rename",
                onConfirm = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Success" -> SuccessDialog(
                title = "Payment Sent!",
                message = "Your payment of \$49.99 has been successfully processed and is on its way.",
                dismissText = "Awesome!",
                onDismiss = { activeDialog = null }
            )

            "Error" -> ErrorDialog(
                title = "Upload Failed",
                message = "We couldn\u2019t upload your file. Please check your internet connection and try again.",
                retryText = "Try Again",
                dismissText = "Cancel",
                onDismiss = { activeDialog = null },
                onRetry = { activeDialog = null }
            )

            "Loading" -> LoadingDialog(
                message = "Uploading your file\u2026",
                onDismiss = { activeDialog = null }
            )

            "Permission" -> PermissionRationaleDialog(
                permissionName = "Camera",
                rationale = "DialogKit needs camera access to let you scan QR codes and take profile photos.",
                benefits = listOf(
                    "Scan QR codes instantly",
                    "Upload profile photos",
                    "Take document scans"
                ),
                icon = Icons.Outlined.Camera,
                onGrant = { activeDialog = null },
                onDeny = { activeDialog = null }
            )

            "No Internet" -> NoInternetDialog(
                onRetry = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Logout" -> LogoutDialog(
                onConfirm = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Progress" -> ProgressDialog(
                title = "Uploading Photo",
                message = "Please wait while your photo is being uploaded.",
                progress = 0.65f,
                stepText = "Step 2 of 3 — Processing image",
                cancelText = "Cancel",
                onCancel = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Multi Select" -> MultiSelectDialog(
                title = "Notification Settings",
                message = "Choose which notifications you'd like to receive.",
                items = listOf(
                    "Push Notifications",
                    "Email Updates",
                    "Weekly Digest",
                    "Product Announcements",
                    "Security Alerts"
                ),
                selectedItems = multiSelected,
                onConfirm = { multiSelected = it; activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Single Select" -> SingleSelectDialog(
                title = "App Theme",
                message = "Choose how the app looks on your device.",
                items = listOf(
                    "System Default",
                    "Light Mode",
                    "Dark Mode",
                    "High Contrast"
                ),
                selectedItem = singleSelected,
                onSelect = { singleSelected = it; activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Session Expired" -> SessionExpiredDialog(
                onSignIn = { activeDialog = null }
            )

            "Cookie Consent" -> CookieConsentDialog(
                onAccept = { activeDialog = null },
                onManage = { activeDialog = null },
                onPolicy = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Subscription" -> SubscriptionDialog(
                features = listOf(
                    "Unlimited projects & workspaces",
                    "Priority customer support",
                    "Advanced analytics & reports",
                    "Early access to new features"
                ),
                onSubscribe = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Feedback" -> FeedbackDialog(
                onSubmit = { _, _ -> activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Countdown" -> CountdownDialog(
                title = "Limited Offer!",
                message = "This deal expires when the timer runs out.",
                seconds = 8,
                onTimeout = { activeDialog = null },
                onCancel = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Onboarding Tip" -> OnboardingTipDialog(
                title = "Quick Replies",
                message = "Swipe right on any message to reply instantly without opening the full thread.",
                stepText = "Step 3 of 5",
                primaryText = "Next",
                skipText = "Skip Tour",
                onPrimary = { activeDialog = null },
                onSkip = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Media Picker" -> MediaPickerDialog(
                title = "Add a Photo",
                onCamera = { activeDialog = null },
                onGallery = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Date Picker" -> DatePickerDialog(
                onConfirm = { _, _, _ -> activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Time Picker" -> TimePickerDialog(
                onConfirm = { _, _ -> activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Biometric" -> BiometricDialog(
                onFallback = { activeDialog = null },
                onDismiss = { activeDialog = null }
            )

            "Color Picker" -> ColorPickerDialog(
                initialColor = pickedColor,
                onConfirm = { pickedColor = it; activeDialog = null },
                onDismiss = { activeDialog = null }
            )
        }
    }
}

// ── Hero ──────────────────────────────────────────────────────────────────────

@Composable
private fun HeroSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 24.dp, bottom = 20.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = "compose-dialog-kit",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "27 ready-made dialogs\nfor Jetpack Compose.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 32.sp,
            letterSpacing = (-0.3).sp
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Modern design. Dark mode. Zero boilerplate.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── Dialog card ───────────────────────────────────────────────────────────────

@Composable
private fun DialogCard(entry: DialogEntry, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(entry.color, entry.gradientEnd),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White)
            ) { onClick() }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = size.width * 0.65f,
                center = Offset(size.width * 1.0f, size.height * 0.0f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = entry.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = entry.name,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    letterSpacing = (-0.2).sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = entry.description,
                    color = Color.White.copy(alpha = 0.70f),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
