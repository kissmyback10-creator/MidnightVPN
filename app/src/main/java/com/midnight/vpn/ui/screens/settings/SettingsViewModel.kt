package com.midnight.vpn.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midnight.vpn.domain.model.AppSettings
import com.midnight.vpn.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings(),
        )

    fun toggleKillSwitch(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateKillSwitch(enabled) }
    }

    fun toggleSplitTunneling(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateSplitTunneling(enabled) }
    }

    fun updateExcludedApps(apps: Set<String>) {
        viewModelScope.launch { settingsRepository.updateExcludedApps(apps) }
    }

    fun toggleAutoConnect(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateAutoConnect(enabled) }
    }
}
