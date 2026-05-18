package com.midnight.vpn.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.midnight.vpn.ui.theme.CyberBlue
import com.midnight.vpn.ui.theme.JetBrainsMonoFamily
import com.midnight.vpn.ui.theme.NeonPurple
import com.midnight.vpn.ui.theme.TextSecondary
import com.midnight.vpn.ui.theme.glassCard

@Composable
fun StatsCard(
    downloadSpeed: String,
    uploadSpeed: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glassCard(cornerRadius = 12.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        // Download
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "Download",
                tint = CyberBlue,
            )
            Text(
                text = downloadSpeed,
                fontFamily = JetBrainsMonoFamily,
                color = CyberBlue,
            )
            Text(
                text = "Download",
                color = TextSecondary,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
            )
        }

        // Upload
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Upload",
                tint = NeonPurple,
            )
            Text(
                text = uploadSpeed,
                fontFamily = JetBrainsMonoFamily,
                color = NeonPurple,
            )
            Text(
                text = "Upload",
                color = TextSecondary,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
            )
        }
    }
}
