package com.midnight.vpn.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.midnight.vpn.domain.model.AppSettings
import com.midnight.vpn.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "midnight_settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    private object Keys {
        val KILL_SWITCH = booleanPreferencesKey("kill_switch")
        val SPLIT_TUNNELING = booleanPreferencesKey("split_tunneling")
        val EXCLUDED_APPS = stringSetPreferencesKey("excluded_apps")
        val AUTO_CONNECT = booleanPreferencesKey("auto_connect")
    }

    override fun getSettings(): Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            killSwitchEnabled = prefs[Keys.KILL_SWITCH] ?: false,
            splitTunnelingEnabled = prefs[Keys.SPLIT_TUNNELING] ?: false,
            excludedApps = prefs[Keys.EXCLUDED_APPS] ?: emptySet(),
            autoConnect = prefs[Keys.AUTO_CONNECT] ?: false,
        )
    }

    override suspend fun updateKillSwitch(enabled: Boolean) {
        context.dataStore.edit { it[Keys.KILL_SWITCH] = enabled }
    }

    override suspend fun updateSplitTunneling(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SPLIT_TUNNELING] = enabled }
    }

    override suspend fun updateExcludedApps(apps: Set<String>) {
        context.dataStore.edit { it[Keys.EXCLUDED_APPS] = apps }
    }

    override suspend fun updateAutoConnect(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AUTO_CONNECT] = enabled }
    }
}
