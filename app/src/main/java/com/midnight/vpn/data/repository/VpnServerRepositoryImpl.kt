package com.midnight.vpn.data.repository

import android.util.Base64
import com.midnight.vpn.data.remote.api.VpnGateApi
import com.midnight.vpn.data.remote.dto.VpnGateParser
import com.midnight.vpn.domain.model.VpnServer
import com.midnight.vpn.domain.repository.VpnServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VpnServerRepositoryImpl @Inject constructor(
    private val vpnGateApi: VpnGateApi,
) : VpnServerRepository {

    private val serversFlow = MutableStateFlow<Result<List<VpnServer>>>(Result.success(emptyList()))
    private val _selectedServer = MutableStateFlow<VpnServer?>(null)

    override fun getServers(): Flow<Result<List<VpnServer>>> = serversFlow.asStateFlow()
    override val selectedServer: StateFlow<VpnServer?> = _selectedServer.asStateFlow()

    override fun selectServer(server: VpnServer?) {
        _selectedServer.value = server
    }

    override suspend fun refreshServers(): Result<List<VpnServer>> = withContext(Dispatchers.IO) {
        runCatching {
            val csv = vpnGateApi.getServerList().string()
            val servers = VpnGateParser.parse(csv)
                .sortedBy { it.ping }
            serversFlow.value = Result.success(servers)
            servers
        }.onFailure { e ->
            serversFlow.value = Result.failure(e)
        }
    }

    override suspend fun getServerConfig(server: VpnServer): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                String(
                    Base64.decode(server.ovpnConfigBase64, Base64.DEFAULT),
                    Charsets.UTF_8,
                )
            }
        }

    override suspend fun pingServer(server: VpnServer): Result<Int> =
        withContext(Dispatchers.IO) {
            runCatching {
                val start = System.currentTimeMillis()
                val reachable = InetAddress.getByName(server.ip).isReachable(3000)
                if (reachable) {
                    (System.currentTimeMillis() - start).toInt()
                } else {
                    server.ping
                }
            }
        }
}
