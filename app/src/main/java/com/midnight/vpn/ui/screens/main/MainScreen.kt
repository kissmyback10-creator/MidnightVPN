package com.midnight.vpn.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.midnight.vpn.domain.model.ConnectionState
import com.midnight.vpn.ui.components.NeonConnectButton
import com.midnight.vpn.ui.components.StatsCard
import com.midnight.vpn.ui.theme.CyberBlue
import com.midnight.vpn.ui.theme.InterFontFamily
import com.midnight.vpn.ui.theme.JetBrainsMonoFamily
import com.midnight.vpn.ui.theme.MidnightBlack
import com.midnight.vpn.ui.theme.NeonPurple
import com.midnight.vpn.ui.theme.StatusConnected
import com.midnight.vpn.ui.theme.StatusDisconnected
import com.midnight.vpn.ui.theme.TextSecondary
import com.midnight.vpn.ui.theme.glassCard

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBlack),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Title
            Text(
                text = "MidnightVPN",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = NeonPurple,
                letterSpacing = 2.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status badge
            StatusBadge(connectionState = uiState.connectionState)

            Spacer(modifier = Modifier.weight(0.3f))

            // Connect button
            NeonConnectButton(
                connectionState = uiState.connectionState,
                onClick = { viewModel.toggleConnection() },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selected server info
            uiState.connectedServerName?.let { name ->
                Text(
                    text = name,
                    fontFamily = JetBrainsMonoFamily,
                    fontSize = 12.sp,
                    color = TextSecondary,
                )
            }

            if (uiState.selectedServer == null && uiState.connectionState == ConnectionState.DISCONNECTED) {
                Text(
                    text = "Select a server to connect",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))

            // IP address card
            IpCard(publicIp = uiState.publicIp, isConnected = uiState.connectionState == ConnectionState.CONNECTED)

            Spacer(modifier = Modifier.height(16.dp))

            // Speed stats
            StatsCard(
                downloadSpeed = uiState.downloadSpeed,
                uploadSpeed = uiState.uploadSpeed,
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatusBadge(connectionState: ConnectionState) {
    val (text, color) = when (connectionState) {
        ConnectionState.DISCONNECTED -> "Not Protected" to StatusDisconnected
        ConnectionState.CONNECTING -> "Securing Connection..." to CyberBlue
        ConnectionState.CONNECTED -> "Protected" to StatusConnected
        ConnectionState.DISCONNECTING -> "Disconnecting..." to CyberBlue
        ConnectionState.ERROR -> "Connection Error" to StatusDisconnected
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = text,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = color,
        )
    }
}

@Composable
private fun IpCard(publicIp: String, isConnected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(cornerRadius = 12.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Public,
                contentDescription = "IP",
                tint = if (isConnected) StatusConnected else TextSecondary,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = "  Public IP",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )
        }
        Text(
            text = publicIp,
            fontFamily = JetBrainsMonoFamily,
            fontSize = 14.sp,
            color = if (isConnected) StatusConnected else CyberBlue,
        )
    }
}
