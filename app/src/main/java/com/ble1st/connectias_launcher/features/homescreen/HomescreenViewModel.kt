package com.ble1st.connectias_launcher.features.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ble1st.connectias_launcher.data.local.entity.ShortcutEntity
import com.ble1st.connectias_launcher.domain.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomescreenViewModel @Inject constructor(
    private val repository: HomescreenRepository
) : ViewModel() {

    private val _shortcuts = MutableStateFlow<List<ShortcutEntity>>(emptyList())
    val shortcuts: StateFlow<List<ShortcutEntity>> = _shortcuts.asStateFlow()

    init {
        // Load shortcuts for screen 0 by default
        loadShortcuts(0)
    }

    private fun loadShortcuts(screenId: Int) {
        viewModelScope.launch {
            repository.getShortcutsForScreen(screenId).collect {
                _shortcuts.value = it
            }
        }
    }

    fun addAppToHomescreen(app: AppInfo) {
        viewModelScope.launch {
            // Simple logic: find first available spot (omitted for brevity, just adding to next position)
            val currentCount = _shortcuts.value.size
            val newShortcut = ShortcutEntity(
                packageName = app.packageName,
                className = "", // We'll need to resolve this properly later
                label = app.label,
                gridX = currentCount % 4, // Simple grid logic
                gridY = currentCount / 4,
                screenId = 0
            )
            repository.addShortcut(newShortcut)
        }
    }
    
    fun deleteShortcut(id: Long) {
        viewModelScope.launch {
            repository.removeShortcut(id)
        }
    }
}
