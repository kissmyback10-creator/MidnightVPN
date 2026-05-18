package com.midnight.vpn.domain.model

data class VpnServer(
    val hostName: String,
    val ip: String,
    val port: Int = 443,
    val countryShort: String,
    val countryLong: String,
    val speed: Long,
    val ping: Int,
    val numSessions: Int,
    val uptime: Long,
    val score: Int,
    val ovpnConfigBase64: String,
    val protocol: String = "openvpn",
)
