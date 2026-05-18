package com.midnight.vpn.domain.repository

import com.midnight.vpn.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun updateKillSwitch(enabled: Boolean)
    suspend fun updateSplitTunneling(enabled: Boolean)
    suspend fun updateExcludedApps(apps: Set<String>)
    suspend fun updateAutoConnect(enabled: Boolean)
}
