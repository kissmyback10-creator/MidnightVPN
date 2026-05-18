package com.midnight.vpn.domain.usecase

import com.midnight.vpn.domain.model.VpnServer
import com.midnight.vpn.domain.repository.VpnServerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetServersUseCase @Inject constructor(
    private val repository: VpnServerRepository,
) {
    operator fun invoke(): Flow<Result<List<VpnServer>>> = repository.getServers()
}
