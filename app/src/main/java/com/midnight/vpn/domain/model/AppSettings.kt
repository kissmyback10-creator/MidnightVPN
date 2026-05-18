package com.midnight.vpn.domain.model

data class AppSettings(
    val killSwitchEnabled: Boolean = false,
    val splitTunnelingEnabled: Boolean = false,
    val excludedApps: Set<String> = emptySet(),
    val autoConnect: Boolean = false,
    val selectedProtocol: String = "openvpn",
)
