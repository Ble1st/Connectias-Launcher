package com.ble1st.connectias_launcher.features.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ble1st.connectias_launcher.R
import com.ble1st.connectias_launcher.data.local.entity.ShortcutEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Homescreen(
    modifier: Modifier = Modifier,
    viewModel: HomescreenViewModel = hiltViewModel(),
    onOpenAppDrawer: () -> Unit,
    onLaunchShortcut: (ShortcutEntity) -> Unit
) {
    val shortcuts by viewModel.shortcuts.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        // Shortcuts Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Space for Dock
        ) {
            items(shortcuts) { shortcut ->
                ShortcutItem(
                    shortcut = shortcut,
                    onClick = { onLaunchShortcut(shortcut) },
                    onLongClick = { viewModel.deleteShortcut(shortcut.id) }
                )
            }
        }

        // Dock
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize() // Adjust height as needed, currently filling to align bottom
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
             IconButton(
                 onClick = onOpenAppDrawer,
                 modifier = Modifier
                     .size(56.dp)
                     .background(Color.White.copy(alpha = 0.2f), shape = MaterialTheme.shapes.extraLarge)
             ) {
                 Icon(
                     imageVector = Icons.Filled.Apps,
                     contentDescription = "App Drawer",
                     tint = Color.White
                 )
             }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShortcutItem(
    shortcut: ShortcutEntity,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        // Placeholder Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, shape = MaterialTheme.shapes.medium)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = shortcut.label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            maxLines = 1
        )
    }
}
