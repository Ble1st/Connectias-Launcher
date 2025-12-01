package com.ble1st.connectias_launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ble1st.connectias_launcher.features.safemode.SafeModeRepository
import com.ble1st.connectias_launcher.ui.theme.ConnectiasLauncherTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var safeModeRepository: SafeModeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isSafeMode = safeModeRepository.isSystemSafeMode() || safeModeRepository.isCrashLoopDetected()
        if (isSafeMode) {
            Timber.w("Safe Mode active! Widgets and third-party plugins should be disabled.")
        }

        setContent {
            ConnectiasLauncherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isSafeMode) {
                        Greeting(
                            name = "SAFE MODE ACTIVE",
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        Greeting(
                            name = "Connectias Launcher",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConnectiasLauncherTheme {
        Greeting("Android")
    }
}
