package com.midnight.vpn.domain.model

data class ConnectionStats(
    val downloadSpeed: Long = 0L,
    val uploadSpeed: Long = 0L,
    val totalDownloaded: Long = 0L,
    val totalUploaded: Long = 0L,
    val connectedTime: Long = 0L,
    val publicIp: String = "",
)
