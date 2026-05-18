package com.midnight.vpn.domain.repository

import com.midnight.vpn.domain.model.VpnServer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface VpnServerRepository {
    fun getServers(): Flow<Result<List<VpnServer>>>
    val selectedServer: StateFlow<VpnServer?>
    fun selectServer(server: VpnServer?)
    suspend fun refreshServers(): Result<List<VpnServer>>
    suspend fun getServerConfig(server: VpnServer): Result<String>
    suspend fun pingServer(server: VpnServer): Result<Int>
}
