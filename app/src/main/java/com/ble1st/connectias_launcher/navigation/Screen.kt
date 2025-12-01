package com.ble1st.connectias_launcher.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AppDrawer : Screen("app_drawer")
}
