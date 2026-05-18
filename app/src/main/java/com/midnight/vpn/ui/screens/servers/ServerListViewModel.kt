package com.midnight.vpn.ui.screens.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midnight.vpn.domain.model.VpnServer
import com.midnight.vpn.domain.usecase.GetServersUseCase
import com.midnight.vpn.domain.usecase.RefreshServersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ServerListUiState(
    val servers: List<VpnServer> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortBy: SortOption = SortOption.PING,
    val filterCountry: String? = null,
)

enum class SortOption {
    PING,
    SPEED,
    SCORE,
    COUNTRY,
}

@HiltViewModel
class ServerListViewModel @Inject constructor(
    private val getServersUseCase: GetServersUseCase,
    private val refreshServersUseCase: RefreshServersUseCase,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _sortOption = MutableStateFlow(SortOption.PING)
    private val _filterCountry = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ServerListUiState> = combine(
        getServersUseCase(),
        _isLoading,
        _error,
        _sortOption,
        _filterCountry,
    ) { serversResult, loading, error, sortBy, country ->
        val servers = serversResult.getOrDefault(emptyList())
        val filtered = if (country != null) {
            servers.filter { it.countryShort.equals(country, ignoreCase = true) }
        } else {
            servers
        }
        val sorted = when (sortBy) {
            SortOption.PING -> filtered.sortedBy { it.ping }
            SortOption.SPEED -> filtered.sortedByDescending { it.speed }
            SortOption.SCORE -> filtered.sortedByDescending { it.score }
            SortOption.COUNTRY -> filtered.sortedBy { it.countryLong }
        }
        ServerListUiState(
            servers = sorted,
            isLoading = loading,
            error = serversResult.exceptionOrNull()?.message ?: error,
            sortBy = sortBy,
            filterCountry = country,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ServerListUiState(isLoading = true),
    )

    init {
        refreshServers()
    }

    fun refreshServers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            refreshServersUseCase()
            _isLoading.value = false
        }
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun setCountryFilter(country: String?) {
        _filterCountry.value = country
    }
}
