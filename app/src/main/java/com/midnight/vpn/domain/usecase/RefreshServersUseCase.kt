package com.midnight.vpn.domain.usecase

import com.midnight.vpn.domain.model.VpnServer
import com.midnight.vpn.domain.repository.VpnServerRepository
import javax.inject.Inject

class RefreshServersUseCase @Inject constructor(
    private val repository: VpnServerRepository,
) {
    suspend operator fun invoke(): Result<List<VpnServer>> = repository.refreshServers()
}
