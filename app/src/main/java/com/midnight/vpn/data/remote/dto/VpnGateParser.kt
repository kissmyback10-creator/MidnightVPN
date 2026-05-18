package com.midnight.vpn.data.remote.dto

import com.midnight.vpn.domain.model.VpnServer

object VpnGateParser {

    /**
     * Parses VPN Gate CSV response into a list of [VpnServer].
     *
     * CSV columns (0-indexed):
     *  0: HostName
     *  1: IP
     *  2: Score
     *  3: Ping
     *  4: Speed
     *  5: CountryLong
     *  6: CountryShort
     *  7: NumVpnSessions
     *  8: Uptime
     *  9: TotalUsers
     * 10: TotalTraffic
     * 11: LogType
     * 12: Operator
     * 13: Message
     * 14: OpenVPN_ConfigData_Base64
     */
    fun parse(csv: String): List<VpnServer> {
        val lines = csv.lines()
            .drop(1)                       // skip header description line
            .filter { it.contains(",") }
            .filterNot { it.startsWith("*") || it.startsWith("#") }

        return lines.mapNotNull { line ->
            runCatching {
                val cols = line.split(",")
                if (cols.size < 15) return@mapNotNull null
                val configBase64 = cols[14].trim()
                if (configBase64.isBlank()) return@mapNotNull null

                VpnServer(
                    hostName = cols[0].trim(),
                    ip = cols[1].trim(),
                    score = cols[2].trim().toIntOrNull() ?: 0,
                    ping = cols[3].trim().toIntOrNull() ?: 999,
                    speed = cols[4].trim().toLongOrNull() ?: 0L,
                    countryLong = cols[5].trim(),
                    countryShort = cols[6].trim(),
                    numSessions = cols[7].trim().toIntOrNull() ?: 0,
                    uptime = cols[8].trim().toLongOrNull() ?: 0L,
                    ovpnConfigBase64 = configBase64,
                )
            }.getOrNull()
        }
    }
}
