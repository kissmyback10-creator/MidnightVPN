package com.midnight.vpn.ui.screens.main

import android.app.Application
import android.content.Intent
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.midnight.vpn.data.remote.api.IpApi
import com.midnight.vpn.domain.model.ConnectionState
import com.midnight.vpn.domain.model.VpnServer
import com.midnight.vpn.domain.repository.VpnServerRepository
import com.midnight.vpn.service.MidnightVpnService
import com.midnight.vpn.util.FormatUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val publicIp: String = "---",
    val downloadSpeed: String = "0 B/s",
    val uploadSpeed: String = "0 B/s",
    val connectedServerName: String? = null,
    val selectedServer: VpnServer? = null,
    val connectedTime: String = "00:00:00",
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val serverRepository: VpnServerRepository,
    private val ipApi: IpApi,
) : AndroidViewModel(application) {

    private val _selectedServer = MutableStateFlow<VpnServer?>(null)
    val selectedServer: StateFlow<VpnServer?> = _selectedServer.asStateFlow()

    private val _publicIp = MutableStateFlow("---")

    val uiState: StateFlow<MainUiState> = combine(
        MidnightVpnService.connectionState,
        MidnightVpnService.downloadSpeed,
        MidnightVpnService.uploadSpeed,
        MidnightVpnService.connectedServer,
        _publicIp,
    ) { connState, downSpeed, upSpeed, serverName, ip ->
        MainUiState(
            connectionState = connState,
            publicIp = ip,
            downloadSpeed = FormatUtils.formatSpeed(downSpeed),
            uploadSpeed = FormatUtils.formatSpeed(upSpeed),
            connectedServerName = serverName,
            selectedServer = _selectedServer.value,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState(),
    )

    init {
        fetchPublicIp()
    }

    fun selectServer(server: VpnServer) {
        _selectedServer.value = server
    }

    fun toggleConnection() {
        val currentState = MidnightVpnService.connectionState.value
        when (currentState) {
            ConnectionState.DISCONNECTED, ConnectionState.ERROR -> connect()
            ConnectionState.CONNECTED -> disconnect()
            else -> { /* ignore during transitions */ }
        }
    }

    private fun connect() {
        val server = _selectedServer.value ?: return
        viewModelScope.launch {
            val configResult = serverRepository.getServerConfig(server)
            configResult.onSuccess { config ->
                val intent = Intent(application, MidnightVpnService::class.java).apply {
                    action = MidnightVpnService.ACTION_CONNECT
                    putExtra(MidnightVpnService.EXTRA_CONFIG, config)
                    putExtra(MidnightVpnService.EXTRA_SERVER_IP, server.ip)
                    putExtra(MidnightVpnService.EXTRA_SERVER_NAME, "${server.countryLong} (${server.ip})")
                }
                application.startForegroundService(intent)

                // Refresh IP after a short delay
                kotlinx.coroutines.delay(3000)
                fetchPublicIp()
            }
        }
    }

    private fun disconnect() {
        val intent = Intent(application, MidnightVpnService::class.java).apply {
            action = MidnightVpnService.ACTION_DISCONNECT
        }
        application.startService(intent)
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            fetchPublicIp()
        }
    }

    private fun fetchPublicIp() {
        viewModelScope.launch {
            try {
                _publicIp.value = ipApi.getPublicIp().trim()
            } catch (_: Exception) {
                _publicIp.value = "Unavailable"
            }
        }
    }
}
