package com.midnight.vpn.domain.repository

import com.midnight.vpn.domain.model.VpnServer
import kotlinx.coroutines.flow.Flow

interface VpnServerRepository {
    fun getServers(): Flow<Result<List<VpnServer>>>
    suspend fun refreshServers(): Result<List<VpnServer>>
    suspend fun getServerConfig(server: VpnServer): Result<String>
    suspend fun pingServer(server: VpnServer): Result<Int>
}
