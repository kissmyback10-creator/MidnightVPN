package com.midnight.vpn.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.midnight.vpn.R
import com.midnight.vpn.domain.model.ConnectionState
import com.midnight.vpn.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

class MidnightVpnService : VpnService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var vpnInterface: ParcelFileDescriptor? = null

    companion object {
        const val ACTION_CONNECT = "com.midnight.vpn.CONNECT"
        const val ACTION_DISCONNECT = "com.midnight.vpn.DISCONNECT"
        const val EXTRA_CONFIG = "vpn_config"
        const val EXTRA_SERVER_IP = "server_ip"
        const val EXTRA_SERVER_NAME = "server_name"

        private const val CHANNEL_ID = "midnight_vpn_channel"
        private const val NOTIFICATION_ID = 1

        private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
        val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

        private val _downloadSpeed = MutableStateFlow(0L)
        val downloadSpeed: StateFlow<Long> = _downloadSpeed.asStateFlow()

        private val _uploadSpeed = MutableStateFlow(0L)
        val uploadSpeed: StateFlow<Long> = _uploadSpeed.asStateFlow()

        private val _connectedServer = MutableStateFlow<String?>(null)
        val connectedServer: StateFlow<String?> = _connectedServer.asStateFlow()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CONNECT -> {
                val config = intent.getStringExtra(EXTRA_CONFIG) ?: return START_NOT_STICKY
                val serverName = intent.getStringExtra(EXTRA_SERVER_NAME) ?: "Unknown"
                startForeground(NOTIFICATION_ID, buildNotification("Connecting to $serverName..."))
                connect(config, serverName)
            }
            ACTION_DISCONNECT -> {
                disconnect()
            }
        }
        return START_STICKY
    }

    private fun connect(config: String, serverName: String) {
        serviceScope.launch {
            _connectionState.value = ConnectionState.CONNECTING
            _connectedServer.value = serverName

            try {
                val builder = Builder()
                    .setSession("MidnightVPN")
                    .addAddress("10.0.0.2", 32)
                    .addRoute("0.0.0.0", 0)
                    .addDnsServer("8.8.8.8")
                    .addDnsServer("8.8.4.4")
                    .setMtu(1500)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    builder.setMetered(false)
                }

                vpnInterface = builder.establish()

                if (vpnInterface != null) {
                    _connectionState.value = ConnectionState.CONNECTED
                    updateNotification("Connected to $serverName")
                    startTrafficMonitoring()
                } else {
                    _connectionState.value = ConnectionState.ERROR
                    stopSelf()
                }
            } catch (e: Exception) {
                _connectionState.value = ConnectionState.ERROR
                stopSelf()
            }
        }
    }

    private fun disconnect() {
        serviceScope.launch {
            _connectionState.value = ConnectionState.DISCONNECTING
            try {
                vpnInterface?.close()
                vpnInterface = null
            } catch (_: Exception) { }
            _connectionState.value = ConnectionState.DISCONNECTED
            _connectedServer.value = null
            _downloadSpeed.value = 0L
            _uploadSpeed.value = 0L
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun startTrafficMonitoring() {
        serviceScope.launch {
            val fd = vpnInterface ?: return@launch
            val input = FileInputStream(fd.fileDescriptor)
            val output = FileOutputStream(fd.fileDescriptor)
            val buffer = ByteArray(32767)

            var lastDownload = 0L
            var lastUpload = 0L
            var totalDown = 0L
            var totalUp = 0L

            while (_connectionState.value == ConnectionState.CONNECTED) {
                try {
                    val bytesRead = input.read(buffer)
                    if (bytesRead > 0) {
                        totalDown += bytesRead
                        output.write(buffer, 0, bytesRead)
                        totalUp += bytesRead
                    }
                } catch (_: Exception) {
                    break
                }

                val currentDown = totalDown - lastDownload
                val currentUp = totalUp - lastUpload
                _downloadSpeed.value = currentDown
                _uploadSpeed.value = currentUp
                lastDownload = totalDown
                lastUpload = totalUp

                delay(1000)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "MidnightVPN Connection",
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = "Active VPN connection status"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(text: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE,
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MidnightVPN")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_vpn_lock)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun updateNotification(text: String) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, buildNotification(text))
    }

    override fun onDestroy() {
        serviceScope.cancel()
        vpnInterface?.close()
        _connectionState.value = ConnectionState.DISCONNECTED
        super.onDestroy()
    }
}
