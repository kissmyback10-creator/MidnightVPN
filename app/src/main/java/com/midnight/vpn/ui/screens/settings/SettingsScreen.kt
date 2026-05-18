package com.midnight.vpn.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppBlocking
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.midnight.vpn.ui.theme.CyberBlue
import com.midnight.vpn.ui.theme.InterFontFamily
import com.midnight.vpn.ui.theme.MidnightBlack
import com.midnight.vpn.ui.theme.MidnightCard
import com.midnight.vpn.ui.theme.NeonPurple
import com.midnight.vpn.ui.theme.TextPrimary
import com.midnight.vpn.ui.theme.TextSecondary
import com.midnight.vpn.ui.theme.glassCard

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBlack)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Settings",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TextPrimary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Security section
        SectionHeader("Security")
        Spacer(modifier = Modifier.height(8.dp))

        SettingToggleCard(
            icon = Icons.Default.WifiOff,
            title = "Kill Switch",
            description = "Block internet access if VPN drops",
            isChecked = settings.killSwitchEnabled,
            onCheckedChange = { viewModel.toggleKillSwitch(it) },
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingToggleCard(
            icon = Icons.Default.AppBlocking,
            title = "Split Tunneling",
            description = "Exclude specific apps from VPN",
            isChecked = settings.splitTunnelingEnabled,
            onCheckedChange = { viewModel.toggleSplitTunneling(it) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Connection section
        SectionHeader("Connection")
        Spacer(modifier = Modifier.height(8.dp))

        SettingToggleCard(
            icon = Icons.Default.SettingsEthernet,
            title = "Auto Connect",
            description = "Connect to VPN on app launch",
            isChecked = settings.autoConnect,
            onCheckedChange = { viewModel.toggleAutoConnect(it) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        // About section
        SectionHeader("About")
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassCard(cornerRadius = 12.dp)
                .padding(16.dp),
        ) {
            Text(
                text = "MidnightVPN v1.0.0",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Secure. Fast. Free.",
                style = MaterialTheme.typography.bodySmall,
                color = NeonPurple,
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = NeonPurple,
        letterSpacing = 2.sp,
    )
}

@Composable
private fun SettingToggleCard(
    icon: ImageVector,
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(cornerRadius = 12.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CyberBlue,
            modifier = Modifier.size(24.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonPurple,
                checkedTrackColor = NeonPurple.copy(alpha = 0.3f),
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = MidnightCard,
            ),
        )
    }
}
