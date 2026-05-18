package com.midnight.vpn.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NetworkPing
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.midnight.vpn.domain.model.VpnServer
import com.midnight.vpn.ui.theme.CyberBlue
import com.midnight.vpn.ui.theme.JetBrainsMonoFamily
import com.midnight.vpn.ui.theme.NeonPurple
import com.midnight.vpn.ui.theme.StatusConnected
import com.midnight.vpn.ui.theme.StatusConnecting
import com.midnight.vpn.ui.theme.StatusDisconnected
import com.midnight.vpn.ui.theme.TextPrimary
import com.midnight.vpn.ui.theme.TextSecondary
import com.midnight.vpn.ui.theme.glassCard
import com.midnight.vpn.util.FormatUtils

@Composable
fun ServerCard(
    server: VpnServer,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pingColor = when {
        server.ping < 50 -> StatusConnected
        server.ping < 150 -> StatusConnecting
        else -> StatusDisconnected
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .glassCard(cornerRadius = 12.dp)
            .then(
                if (isSelected) {
                    Modifier.padding(1.dp) // space for border effect handled by glassCard
                } else {
                    Modifier
                },
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Country flag
        Text(
            text = FormatUtils.countryFlagEmoji(server.countryShort),
            fontSize = 28.sp,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = server.countryLong,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) NeonPurple else TextPrimary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = server.ip,
                fontFamily = JetBrainsMonoFamily,
                fontSize = 12.sp,
                color = TextSecondary,
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NetworkPing,
                    contentDescription = "Ping",
                    tint = pingColor,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = FormatUtils.formatPing(server.ping),
                    fontFamily = JetBrainsMonoFamily,
                    fontSize = 12.sp,
                    color = pingColor,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = "Speed",
                    tint = CyberBlue,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = FormatUtils.formatServerSpeed(server.speed),
                    fontFamily = JetBrainsMonoFamily,
                    fontSize = 12.sp,
                    color = CyberBlue,
                )
            }
        }
    }
}
