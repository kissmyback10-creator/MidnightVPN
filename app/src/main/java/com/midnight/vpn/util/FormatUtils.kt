package com.midnight.vpn.util

import java.util.Locale

object FormatUtils {

    fun formatSpeed(bytesPerSecond: Long): String {
        return when {
            bytesPerSecond < 1_024 -> "$bytesPerSecond B/s"
            bytesPerSecond < 1_048_576 -> String.format(Locale.US, "%.1f KB/s", bytesPerSecond / 1_024.0)
            bytesPerSecond < 1_073_741_824 -> String.format(Locale.US, "%.2f MB/s", bytesPerSecond / 1_048_576.0)
            else -> String.format(Locale.US, "%.2f GB/s", bytesPerSecond / 1_073_741_824.0)
        }
    }

    fun formatBytes(bytes: Long): String {
        return when {
            bytes < 1_024 -> "$bytes B"
            bytes < 1_048_576 -> String.format(Locale.US, "%.1f KB", bytes / 1_024.0)
            bytes < 1_073_741_824 -> String.format(Locale.US, "%.2f MB", bytes / 1_048_576.0)
            else -> String.format(Locale.US, "%.2f GB", bytes / 1_073_741_824.0)
        }
    }

    fun formatDuration(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format(Locale.US, "%02d:%02d:%02d", h, m, s)
    }

    fun formatPing(ms: Int): String = "${ms}ms"

    fun formatServerSpeed(bps: Long): String {
        val mbps = bps / 1_000_000.0
        return String.format(Locale.US, "%.1f Mbps", mbps)
    }

    fun countryFlagEmoji(countryCode: String): String {
        if (countryCode.length != 2) return ""
        val first = Character.toChars(0x1F1E6 - 'A'.code + countryCode[0].uppercaseChar().code)
        val second = Character.toChars(0x1F1E6 - 'A'.code + countryCode[1].uppercaseChar().code)
        return String(first) + String(second)
    }
}
