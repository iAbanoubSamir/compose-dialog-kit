package io.github.iabanoubsamir.dialogkit.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Returns a copy of this [Color] with each RGB channel multiplied by (1 - [fraction]). */
internal fun Color.darken(fraction: Float = 0.2f): Color = Color(
    red = (red * (1f - fraction)).coerceIn(0f, 1f),
    green = (green * (1f - fraction)).coerceIn(0f, 1f),
    blue = (blue * (1f - fraction)).coerceIn(0f, 1f),
    alpha = alpha
)

internal val DialogShape = RoundedCornerShape(28.dp)
internal val ButtonShape = RoundedCornerShape(16.dp)

@Composable
internal fun DialogScaffold(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: String,
    message: String? = null,
    extraContent: @Composable (ColumnScope.() -> Unit)? = null,
    buttons: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = DialogShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != null) {
                icon()
                Spacer(Modifier.height(20.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (message != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
            }
            extraContent?.invoke(this)
            Spacer(Modifier.height(28.dp))
            buttons()
        }
    }
}

@Composable
internal fun DialogIconBadge(imageVector: ImageVector, tint: Color) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(tint),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
internal fun PrimaryButton(text: String, onClick: () -> Unit, color: Color, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun SecondaryTextButton(text: String, onClick: () -> Unit, color: Color = Color.Unspecified, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = if (color == Color.Unspecified) MaterialTheme.colorScheme.onSurfaceVariant else color
        )
    }
}

@Composable
internal fun SecondaryOutlinedButton(text: String, onClick: () -> Unit, color: Color, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = ButtonShape,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}
