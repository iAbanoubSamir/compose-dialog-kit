package io.github.iabanoubsamir.dialogkit.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.iabanoubsamir.dialogkit.sample.ui.theme.DialogKitSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DialogKitSampleTheme {
                ShowcaseScreen()
            }
        }
    }
}
