# compose-dialog-kit

[![CI](https://github.com/iAbanoubSamir/compose-dialog-kit/actions/workflows/ci.yml/badge.svg)](https://github.com/iAbanoubSamir/compose-dialog-kit/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.iabanoubsamir/compose-dialog-kit)](https://central.sonatype.com/artifact/io.github.iabanoubsamir/compose-dialog-kit)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://developer.android.com/about/versions/android-7.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-ready-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)


**27 ready-made dialog composables for Jetpack Compose.**
Modern Material Design 3, dark mode support, smooth animations, zero boilerplate.

---

## Screenshots

<table>
  <tr>
    <td align="center"><img src="screenshots/alert.png" width="240"/><br/><b>Alert</b></td>
    <td align="center"><img src="screenshots/warning.png" width="240"/><br/><b>Warning</b></td>
    <td align="center"><img src="screenshots/delete.png" width="240"/><br/><b>Delete</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/success.png" width="240"/><br/><b>Success</b></td>
    <td align="center"><img src="screenshots/error.png" width="240"/><br/><b>Error</b></td>
    <td align="center"><img src="screenshots/loading.png" width="240"/><br/><b>Loading</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/progress.png" width="240"/><br/><b>Progress</b></td>
    <td align="center"><img src="screenshots/no_internet.png" width="240"/><br/><b>No Internet</b></td>
    <td align="center"><img src="screenshots/logout.png" width="240"/><br/><b>Logout</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/session_expired.png" width="240"/><br/><b>Session Expired</b></td>
    <td align="center"><img src="screenshots/app_update.png" width="240"/><br/><b>App Update</b></td>
    <td align="center"><img src="screenshots/permission.png" width="240"/><br/><b>Permission Rationale</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/cookie_consent.png" width="240"/><br/><b>Cookie Consent</b></td>
    <td align="center"><img src="screenshots/biometric.png" width="240"/><br/><b>Biometric</b></td>
    <td align="center"><img src="screenshots/rate_app.png" width="240"/><br/><b>Rate App</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/feedback.png" width="240"/><br/><b>Feedback</b></td>
    <td align="center"><img src="screenshots/subscription.png" width="240"/><br/><b>Subscription</b></td>
    <td align="center"><img src="screenshots/countdown.png" width="240"/><br/><b>Countdown</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/onboarding_tip.png" width="240"/><br/><b>Onboarding Tip</b></td>
    <td align="center"><img src="screenshots/input.png" width="240"/><br/><b>Input</b></td>
    <td align="center"><img src="screenshots/multi_select.png" width="240"/><br/><b>Multi Select</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/single_select.png" width="240"/><br/><b>Single Select</b></td>
    <td align="center"><img src="screenshots/action_sheet.png" width="240"/><br/><b>Action Sheet</b></td>
    <td align="center"><img src="screenshots/media_picker.png" width="240"/><br/><b>Media Picker</b></td>
  </tr>
  <tr>
    <td align="center"><img src="screenshots/date_picker.png" width="240"/><br/><b>Date Picker</b></td>
    <td align="center"><img src="screenshots/time_picker.png" width="240"/><br/><b>Time Picker</b></td>
    <td align="center"><img src="screenshots/color_picker.png" width="240"/><br/><b>Color Picker</b></td>
  </tr>
</table>

---

## Table of Contents

- [Screenshots](#screenshots)
- [Dialogs at a Glance](#dialogs-at-a-glance)
- [Installation](#installation)
- [Usage](#usage)
- [Dialog Reference](#dialog-reference)
  - [Alert](#alert)
  - [Warning](#warning)
  - [Delete](#delete)
  - [Success](#success)
  - [Error](#error)
  - [Loading](#loading)
  - [Progress](#progress)
  - [No Internet](#no-internet)
  - [Logout](#logout)
  - [Session Expired](#session-expired)
  - [App Update](#app-update)
  - [Permission Rationale](#permission-rationale)
  - [Cookie Consent](#cookie-consent)
  - [Biometric](#biometric)
  - [Rate App](#rate-app)
  - [Feedback](#feedback)
  - [Subscription](#subscription)
  - [Countdown](#countdown)
  - [Onboarding Tip](#onboarding-tip)
  - [Input](#input)
  - [Multi Select](#multi-select)
  - [Single Select](#single-select)
  - [Action Sheet](#action-sheet)
  - [Media Picker](#media-picker)
  - [Date Picker](#date-picker)
  - [Time Picker](#time-picker)
  - [Color Picker](#color-picker)
- [Requirements](#requirements)
- [Contributing](#contributing)
- [Changelog](#changelog)
- [License](#license)

---

## Dialogs at a Glance

| Category | Dialogs |
|---|---|
| Informational | Alert, Success, Error, Loading, No Internet |
| Confirmation | Warning, Delete, Logout, Session Expired |
| Input & Selection | Input, Multi Select, Single Select, Feedback |
| Pickers | Date Picker, Time Picker, Color Picker, Media Picker |
| App Lifecycle | App Update, Permission Rationale, Cookie Consent, Biometric |
| Engagement | Rate App, Subscription, Countdown, Onboarding Tip, Progress, Action Sheet |

---

## Installation

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.iabanoubsamir:compose-dialog-kit:1.0.0")
}
```

Or in Groovy DSL:

```groovy
dependencies {
    implementation 'io.github.iabanoubsamir:compose-dialog-kit:1.0.0'
}
```

Make sure Maven Central is in your repository list (it is by default in new projects):

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

---

## Usage

Every dialog is a `@Composable` function. Show it conditionally based on a state variable.

```kotlin
var showDialog by remember { mutableStateOf(false) }

if (showDialog) {
    AlertDialog(
        title = "Account Verified",
        message = "Your email has been verified. You now have full access.",
        confirmText = "Great!",
        dismissText = "Dismiss",
        onConfirm = { showDialog = false },
        onDismiss = { showDialog = false }
    )
}
```

---

## Dialog Reference

### Alert

General-purpose informational dialog with confirm and optional dismiss actions.

```kotlin
AlertDialog(
    title = "Account Verified",
    message = "Your email has been successfully verified.",
    confirmText = "Got it",
    dismissText = "Dismiss",        // optional
    onConfirm = { },
    onDismiss = { }
)
```

---

### Warning

Draws attention to a risky or irreversible action with a pulsing warning ring.

```kotlin
WarningDialog(
    title = "Unsaved Changes",
    message = "Changes will be lost if you leave. Continue?",
    confirmText = "Leave Anyway",
    dismissText = "Stay",
    onConfirm = { },
    onDismiss = { }
)
```

---

### Delete

Destructive confirmation dialog. Cancel is the visually primary button to reduce accidental taps.

```kotlin
DeleteDialog(
    title = "Delete Photo?",
    message = "This photo will be permanently deleted. This action cannot be undone.",
    onConfirm = { },
    onDismiss = { }
)
```

---

### Success

Animated checkmark with a particle burst and green gradient header.

```kotlin
SuccessDialog(
    title = "Payment Sent!",
    message = "Your payment has been successfully processed.",
    dismissText = "Awesome!",       // default: "Done"
    onDismiss = { }
)
```

---

### Error

Failure state with an optional retry action.

```kotlin
ErrorDialog(
    title = "Upload Failed",
    message = "Check your internet connection and try again.",
    retryText = "Try Again",
    dismissText = "Cancel",
    onRetry = { },
    onDismiss = { }
)
```

---

### Loading

Non-dismissable blocking dialog with a pulsing animation.

```kotlin
LoadingDialog(
    message = "Uploading your file…",
    onDismiss = { }
)
```

---

### Progress

Determinate progress bar with animated fill and optional step label.

```kotlin
ProgressDialog(
    title = "Uploading Photo",
    message = "Please wait…",                   // optional
    progress = 0.65f,                           // 0f..1f
    stepText = "Step 2 of 3 — Processing",      // optional
    cancelText = "Cancel",                      // null hides button
    onCancel = { },
    onDismiss = { }
)
```

---

### No Internet

Connectivity-lost dialog with a retry action.

```kotlin
NoInternetDialog(
    onRetry = { },
    onDismiss = { }
)
```

---

### Logout

Session-end confirmation.

```kotlin
LogoutDialog(
    onConfirm = { },
    onDismiss = { }
)
```

---

### Session Expired

Non-dismissable auth-timeout dialog that forces the user to sign in again.

```kotlin
SessionExpiredDialog(
    onSignIn = { }
)
```

---

### App Update

Shows a list of release notes with update and defer actions.

```kotlin
AppUpdateDialog(
    newVersion = "3.2.0",
    updateNotes = listOf(
        "Dark mode improvements",
        "Faster startup time",
        "Bug fixes and stability"
    ),
    onUpdate = { },
    onLater = { }
)
```

---

### Permission Rationale

Explains why a permission is needed before the system prompt appears.

```kotlin
PermissionRationaleDialog(
    permissionName = "Camera",
    rationale = "Camera access lets you scan QR codes and take profile photos.",
    benefits = listOf(
        "Scan QR codes instantly",
        "Upload profile photos",
        "Take document scans"
    ),
    icon = Icons.Outlined.Camera,
    onGrant = { },
    onDeny = { }
)
```

---

### Cookie Consent

GDPR-compliant cookie consent dialog with accept, manage, and privacy policy actions.

```kotlin
CookieConsentDialog(
    onAccept = { },
    onManage = { },
    onPolicy = { },
    onDismiss = { }
)
```

---

### Biometric

Fingerprint/face authentication prompt with a fallback action.

```kotlin
BiometricDialog(
    onFallback = { /* launch PIN/password flow */ },
    onDismiss = { }
)
```

---

### Rate App

Five-star rating dialog. Title and subtitle animate as the user selects stars.

```kotlin
RateAppDialog(
    appName = "My App",
    rateText = "Rate Now",          // default
    laterText = "Not Now",          // default
    neverText = "Never",            // default
    onRate = { stars -> },          // called with selected star count
    onLater = { },
    onNever = { }
)
```

---

### Feedback

In-app text feedback dialog with an optional star rating row.

```kotlin
FeedbackDialog(
    title = "Send Feedback",        // default
    hint = "Tell us what you think…",
    includeRating = true,           // shows star row above text field
    submitText = "Send",
    dismissText = "Cancel",
    onSubmit = { text, stars -> },
    onDismiss = { }
)
```

---

### Subscription

Premium upsell dialog with a feature list and subscribe action.

```kotlin
SubscriptionDialog(
    features = listOf(
        "Unlimited projects",
        "Priority support",
        "Advanced analytics",
        "Early access to new features"
    ),
    onSubscribe = { },
    onDismiss = { }
)
```

---

### Countdown

Auto-dismissing dialog with a depleting ring animation. Calls `onTimeout` when it reaches zero.

```kotlin
CountdownDialog(
    title = "Limited Offer!",
    message = "This deal expires when the timer runs out.",
    seconds = 10,                   // must be > 0
    accentColor = Color(0xFFE53935),
    cancelText = "Cancel",          // null hides button
    onTimeout = { },                // called at zero before dismiss
    onCancel = { },
    onDismiss = { }
)
```

---

### Onboarding Tip

Feature spotlight with step indicator, primary action, and optional skip.

```kotlin
OnboardingTipDialog(
    title = "Quick Replies",
    message = "Swipe right on any message to reply instantly.",
    stepText = "Step 3 of 5",
    primaryText = "Next",
    skipText = "Skip Tour",         // null hides skip button
    onPrimary = { },
    onSkip = { },
    onDismiss = { }
)
```

---

### Input

Text input dialog with a colored accent strip and gradient confirm button.

```kotlin
InputDialog(
    title = "Rename File",
    message = "Enter a new name.",              // optional
    hint = "File name",
    initialValue = "document_final",
    confirmText = "Rename",
    dismissText = "Cancel",
    keyboardType = KeyboardType.Text,           // default
    icon = Icons.Outlined.Edit,                 // default
    iconColor = Color(0xFF1E88E5),              // default
    onConfirm = { text -> },
    onDismiss = { }
)
```

---

### Multi Select

Checkbox list dialog. Returns the updated set of selected items.

```kotlin
var selected by remember { mutableStateOf(setOf("Push Notifications")) }

MultiSelectDialog(
    title = "Notification Settings",
    message = "Choose which notifications to receive.",
    items = listOf("Push Notifications", "Email Updates", "Weekly Digest"),
    selectedItems = selected,
    onConfirm = { selected = it },
    onDismiss = { }
)
```

---

### Single Select

Radio list dialog. Returns the selected item string.

```kotlin
var theme by remember { mutableStateOf<String?>("System Default") }

SingleSelectDialog(
    title = "App Theme",
    message = "Choose how the app looks.",
    items = listOf("System Default", "Light Mode", "Dark Mode"),
    selectedItem = theme,
    onSelect = { theme = it },
    onDismiss = { }
)
```

---

### Action Sheet

Bottom-anchored sheet with a list of actions. Supports per-item colors and destructive styling.

```kotlin
ActionSheetDialog(
    title = "Photo Options",                    // optional
    items = listOf(
        ActionSheetItem("Take Photo",          Icons.Outlined.Camera,       Color(0xFF1E88E5)) { },
        ActionSheetItem("Choose from Library", Icons.Outlined.PhotoLibrary, Color(0xFF8E24AA)) { },
        ActionSheetItem("Delete Photo",        Icons.Outlined.Delete,       isDestructive = true) { }
    ),
    cancelText = "Cancel",                      // default
    onDismiss = { }
)
```

`ActionSheetItem` signature:

```kotlin
data class ActionSheetItem(
    val label: String,
    val icon: ImageVector? = null,
    val color: Color? = null,                   // icon container tint
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)
```

---

### Media Picker

Bottom-anchored camera / gallery picker with large gradient cards.

```kotlin
MediaPickerDialog(
    title = "Add a Photo",                      // optional
    cameraText = "Camera",                      // default
    galleryText = "Gallery",                    // default
    cancelText = "Cancel",                      // default
    onCamera = { },
    onGallery = { },
    onDismiss = { }
)
```

---

### Date Picker

Custom calendar grid date picker. Month names respect the device locale.

```kotlin
DatePickerDialog(
    title = "Select Date",
    accentColor = Color(0xFF1E88E5),
    confirmText = "Confirm",
    dismissText = "Cancel",
    onConfirm = { day, month, year -> },        // month is 1–12
    onDismiss = { }
)
```

---

### Time Picker

Scrollable hour / minute wheels with 12-hour (AM/PM toggle) or 24-hour mode.

```kotlin
TimePickerDialog(
    title = "Select Time",
    accentColor = Color(0xFF7B1FA2),
    is24Hour = false,                           // default
    confirmText = "Confirm",
    dismissText = "Cancel",
    onConfirm = { hour, minute -> },            // hour is 0–23
    onDismiss = { }
)
```

---

### Color Picker

HSV color picker with Hue, Saturation, and Brightness sliders. Live hex preview.

```kotlin
ColorPickerDialog(
    initialColor = Color(0xFF1E88E5),
    title = "Pick a Color",
    confirmText = "Select",
    dismissText = "Cancel",
    onConfirm = { color -> },
    onDismiss = { }
)
```

---

## Requirements

- **Android API 24+** (Android 7.0)
- **Jetpack Compose** with Material3 support

---

## Contributing

Contributions are welcome! Whether it's a bug fix, a new dialog, or a documentation improvement — all PRs are appreciated.

**Quick steps:**

1. Fork the repo and create a branch: `git checkout -b feat/your-dialog-name`
2. Make your changes following the existing patterns
3. Run the sample app to verify: `./gradlew :app:installDebug`
4. Open a Pull Request with a description and screenshot

For full details — project structure, dialog template, code style rules, and PR checklist — see [CONTRIBUTING.md](CONTRIBUTING.md).

---

## Changelog

### 1.0.0
- Initial release with 27 dialog composables
- Material Design 3 theming and dark mode support
- Spring-based enter animations, smooth exit animations
- Bottom-sheet dialogs (Action Sheet, Media Picker)
- Custom pickers (Date, Time, Color)

---

## License

```
Copyright 2026 Abanoub Samir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
