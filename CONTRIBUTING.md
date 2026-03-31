# Contributing to compose-dialog-kit

Thank you for your interest in contributing! Contributions of all kinds are welcome — bug fixes, new dialogs, documentation improvements, and more.

---

## Table of Contents

- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Adding a New Dialog](#adding-a-new-dialog)
- [Code Style](#code-style)
- [Submitting a Pull Request](#submitting-a-pull-request)
- [Reporting Bugs](#reporting-bugs)
- [Requesting Features](#requesting-features)

---

## Getting Started

1. **Fork** the repository and clone your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/compose-dialog-kit.git
   cd compose-dialog-kit
   ```

2. **Open** the project in Android Studio (Hedgehog or newer recommended).

3. **Run** the `:app` sample to verify the existing dialogs work before making changes:
   ```bash
   ./gradlew :app:installDebug
   ```

4. **Create a branch** for your work:
   ```bash
   git checkout -b feat/your-dialog-name
   ```

---

## Project Structure

```
compose-dialog-kit/
├── app/                            # Sample app — showcases all dialogs
│   └── src/main/.../MainActivity.kt
└── compose-dialog-kit/             # Library module
    └── src/main/java/io/github/iabanoubsamir/dialogkit/
        ├── internal/
        │   ├── BaseDialog.kt       # Animated dialog wrapper — use this in every dialog
        │   └── DialogComponents.kt # Shared composables: DialogScaffold, buttons, icon badge
        ├── AlertDialog.kt
        ├── SuccessDialog.kt
        └── ...                     # One file per dialog
```

Key building blocks:

| Component | Purpose |
|---|---|
| `BaseDialog` | Wraps `Dialog` with spring enter + fade/scale exit animation. Provides a `dismiss` lambda that plays the exit animation before calling `onDismissRequest`. |
| `DialogScaffold` | Standard card layout with icon, title, message, and buttons area. Use for simple dialogs. |
| `DialogIconBadge` | Circular icon badge (outer ring + inner filled circle) used in most icon dialogs. |
| `PrimaryButton` | Full-width filled button. |
| `SecondaryTextButton` | Full-width text button. |
| `SecondaryOutlinedButton` | Full-width outlined button. |

---

## Adding a New Dialog

Follow these steps to add a dialog consistently with the rest of the library.

### 1. Create the composable file

Add `YourDialog.kt` inside `compose-dialog-kit/src/main/java/io/github/iabanoubsamir/dialogkit/`.

**Minimal template using `DialogScaffold`:**

```kotlin
package io.github.iabanoubsamir.dialogkit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SomeIcon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.iabanoubsamir.dialogkit.internal.BaseDialog
import io.github.iabanoubsamir.dialogkit.internal.DialogIconBadge
import io.github.iabanoubsamir.dialogkit.internal.DialogScaffold
import io.github.iabanoubsamir.dialogkit.internal.PrimaryButton
import io.github.iabanoubsamir.dialogkit.internal.SecondaryTextButton

private val YourAccentColor = Color(0xFF1E88E5)

/**
 * KDoc describing what this dialog is for.
 *
 * @param title       Dialog heading.
 * @param message     Supporting description.
 * @param confirmText Label for the confirm button.
 * @param dismissText Label for the cancel button.
 * @param onConfirm   Called when the user confirms.
 * @param onDismiss   Called when the dialog is dismissed.
 */
@Composable
fun YourDialog(
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(onDismissRequest = onDismiss) { dismiss ->
        DialogScaffold(
            icon = { DialogIconBadge(Icons.Outlined.SomeIcon, YourAccentColor) },
            title = title,
            message = message,
            buttons = {
                PrimaryButton("Confirm", onClick = { onConfirm(); dismiss() }, color = YourAccentColor)
                SecondaryTextButton(dismissText, onClick = dismiss)
            }
        )
    }
}
```

**Rules:**
- Always use `BaseDialog` — never call `Dialog` directly in a public composable.
- Always pass a `dismiss` lambda (provided by `BaseDialog`) to button `onClick`s so the exit animation plays.
- Always include KDoc on the public function and every parameter.
- Never hardcode strings visible to users — all user-visible text must come through parameters.
- Never use `GlobalScope`, `viewModelScope`, or any external scope. Animations that need coroutines should use `rememberCoroutineScope()`.

### 2. Add it to the sample app

Open `app/src/main/.../MainActivity.kt` and:

1. Add an entry to `dialogEntries`:
   ```kotlin
   DialogEntry("Your Dialog", "Short description", Color(0xFF1E88E5), Color(0xFF1565C0), Icons.Outlined.SomeIcon),
   ```

2. Add the `when` branch in `ShowcaseScreen`:
   ```kotlin
   "Your Dialog" -> YourDialog(
       title = "Example Title",
       message = "Example supporting message for the showcase.",
       onConfirm = { activeDialog = null },
       onDismiss = { activeDialog = null }
   )
   ```

3. Add the import at the top of the file.

### 3. Verify

Build and run the sample app, open your dialog from the grid, and check:
- Entry animation plays (spring scale + fade in)
- Exit animation plays when dismissed (scale out + fade out)
- Looks correct in both light and dark mode
- Tapping outside the dialog dismisses it

---

## Code Style

- **Kotlin idioms** — use `by remember`, scope functions, trailing lambdas, and named arguments.
- **No hardcoded user-visible strings** — all text must be a parameter.
- **No `Log.d` or debug statements.**
- **No new dependencies** — the library intentionally depends only on Compose + Material3.
- **Imports over inline references** — always add an `import` at the top; never write fully-qualified names inline (e.g. `kotlin.math.abs(...)`).
- **Colors** — define private top-level constants for dialog-specific colors; never hardcode `Color(0xFF...)` inline in the composable body.
- **Modifier order** — follow the standard Compose modifier chain order: size → layout → drawing → behavior → semantics.
- **Dark mode** — always use `MaterialTheme.colorScheme` tokens for backgrounds and text. Only use hardcoded colors for brand-specific accents that are intentionally fixed.

---

## Submitting a Pull Request

1. Make sure the project builds with no errors:
   ```bash
   ./gradlew assembleDebug
   ```

2. Verify the sample app launches and your dialog appears correctly.

3. Push your branch and open a Pull Request against `main`.

4. Fill in the PR description:
   - What the dialog does
   - Screenshot or screen recording of it in action (light + dark mode if possible)
   - Any design decisions worth explaining

5. A maintainer will review and may request changes before merging.

---

## Reporting Bugs

Open a [GitHub Issue](https://github.com/iabanoubsamir/compose-dialog-kit/issues) and include:

- Android API level and device/emulator
- Steps to reproduce
- Expected vs. actual behavior
- A minimal code snippet if possible

---

## Requesting Features

Open a [GitHub Issue](https://github.com/iabanoubsamir/compose-dialog-kit/issues) with the `enhancement` label. Describe the dialog's purpose, its parameters, and ideally a rough sketch or reference design.

---

## License

By contributing, you agree that your contributions will be licensed under the [Apache License 2.0](LICENSE).
