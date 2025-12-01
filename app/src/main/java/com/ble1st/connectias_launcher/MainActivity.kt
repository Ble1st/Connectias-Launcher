package com.ble1st.connectias_launcher

import android.content.pm.LauncherApps
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ble1st.connectias_launcher.features.appdrawer.AppDrawerScreen
import com.ble1st.connectias_launcher.features.homescreen.Homescreen
import com.ble1st.connectias_launcher.features.homescreen.HomescreenViewModel
import com.ble1st.connectias_launcher.features.safemode.SafeModeRepository
import com.ble1st.connectias_launcher.navigation.Screen
import com.ble1st.connectias_launcher.ui.theme.ConnectiasLauncherTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var safeModeRepository: SafeModeRepository
    
    @Inject
    lateinit var launcherApps: LauncherApps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isSystemSafeMode = packageManager.isSafeMode
        if (isSystemSafeMode) {
            Timber.w("System is in Safe Mode!")
        }
        
        safeModeRepository.resetCrashCount()

        setContent {
            ConnectiasLauncherTheme {
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(navController = navController, startDestination = Screen.Home.route) {
                            composable(Screen.Home.route) {
                                Homescreen(
                                    onOpenAppDrawer = { navController.navigate(Screen.AppDrawer.route) },
                                    onLaunchShortcut = { shortcut -> 
                                        launchApp(shortcut.packageName)
                                    }
                                )
                            }
                            composable(Screen.AppDrawer.route) {
                                val homescreenViewModel: HomescreenViewModel = hiltViewModel()
                                
                                AppDrawerScreen(
                                    onAppClick = { app -> launchApp(app.packageName) },
                                    onAppLongClick = { app ->
                                        homescreenViewModel.addAppToHomescreen(app)
                                        Toast.makeText(this@MainActivity, "${app.label} zum Homescreen hinzugefügt", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                        
                        if (isSystemSafeMode) {
                            SafeModeOverlay()
                        }
                    }
                }
            }
        }
    }

    private fun launchApp(packageName: String) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "App konnte nicht gestartet werden", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to launch app: $packageName")
            Toast.makeText(this, "Fehler beim Starten der App", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun SafeModeOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "SAFE MODE AKTIV",
            style = MaterialTheme.typography.displayLarge,
            color = Color.White
        )
    }
}
