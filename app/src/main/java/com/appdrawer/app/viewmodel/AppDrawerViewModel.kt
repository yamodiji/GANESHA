package com.appdrawer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appdrawer.app.model.AppDrawerState
import com.appdrawer.app.model.AppInfo
import com.appdrawer.app.repository.AppPreferences
import com.appdrawer.app.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppDrawerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)
    private val preferences = AppPreferences(application)
    
    private val _state = MutableStateFlow(AppDrawerState())
    val state: StateFlow<AppDrawerState> = _state.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        loadApps()
        observePreferences()
        observeSearchQuery()
    }
    
    private fun loadApps() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            try {
                val apps = repository.getInstalledApps()
                _state.value = _state.value.copy(
                    allApps = apps,
                    filteredApps = apps,
                    isLoading = false
                )
                updateAppsWithPreferences()
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
    
    private fun observePreferences() {
        viewModelScope.launch {
            preferences.pinnedApps.collect { pinnedPackages ->
                updateAppsWithPinnedState(pinnedPackages)
            }
        }
        
        viewModelScope.launch {
            preferences.customNames.collect { customNames ->
                updateAppsWithCustomNames(customNames)
            }
        }
        
        viewModelScope.launch {
            preferences.appUsage.collect { usage ->
                updateAppsWithUsage(usage)
            }
        }
    }
    
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collect { query ->
                    filterApps(query)
                }
        }
    }
    
    private fun updateAppsWithPreferences() {
        val currentState = _state.value
        val pinnedPackages = preferences.pinnedApps.replayCache.firstOrNull() ?: emptySet()
        val customNames = preferences.customNames.replayCache.firstOrNull() ?: emptyMap()
        val usage = preferences.appUsage.replayCache.firstOrNull() ?: emptyMap()
        
        val updatedApps = currentState.allApps.map { app ->
            val (launchCount, lastUsed) = usage[app.packageName] ?: (0 to 0L)
            app.copy(
                isPinned = app.packageName in pinnedPackages,
                displayName = customNames[app.packageName] ?: app.appName,
                launchCount = launchCount,
                lastUsed = lastUsed
            )
        }
        
        val pinnedApps = updatedApps.filter { it.isPinned }
            .sortedByDescending { it.launchCount }
        
        _state.value = currentState.copy(
            allApps = updatedApps,
            pinnedApps = pinnedApps
        )
        
        filterApps(_searchQuery.value)
    }
    
    private fun updateAppsWithPinnedState(pinnedPackages: Set<String>) {
        val currentState = _state.value
        val updatedApps = currentState.allApps.map { app ->
            app.copy(isPinned = app.packageName in pinnedPackages)
        }
        
        val pinnedApps = updatedApps.filter { it.isPinned }
            .sortedByDescending { it.launchCount }
        
        _state.value = currentState.copy(
            allApps = updatedApps,
            pinnedApps = pinnedApps
        )
        
        filterApps(_searchQuery.value)
    }
    
    private fun updateAppsWithCustomNames(customNames: Map<String, String>) {
        val currentState = _state.value
        val updatedApps = currentState.allApps.map { app ->
            app.copy(displayName = customNames[app.packageName] ?: app.appName)
        }
        
        _state.value = currentState.copy(allApps = updatedApps)
        filterApps(_searchQuery.value)
    }
    
    private fun updateAppsWithUsage(usage: Map<String, Pair<Int, Long>>) {
        val currentState = _state.value
        val updatedApps = currentState.allApps.map { app ->
            val (launchCount, lastUsed) = usage[app.packageName] ?: (0 to 0L)
            app.copy(launchCount = launchCount, lastUsed = lastUsed)
        }
        
        val pinnedApps = updatedApps.filter { it.isPinned }
            .sortedByDescending { it.launchCount }
        
        _state.value = currentState.copy(
            allApps = updatedApps,
            pinnedApps = pinnedApps
        )
        
        filterApps(_searchQuery.value)
    }
    
    private fun filterApps(query: String) {
        val currentState = _state.value
        val filteredApps = if (query.isBlank()) {
            currentState.allApps
        } else {
            currentState.allApps.filter { it.matchesSearch(query) }
        }.sortedWith(compareByDescending<AppInfo> { it.launchCount }.thenBy { it.displayName.lowercase() })
        
        _state.value = currentState.copy(
            filteredApps = filteredApps,
            searchQuery = query
        )
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
    }
    
    fun launchApp(app: AppInfo) {
        viewModelScope.launch {
            if (repository.launchApp(app.packageName)) {
                preferences.incrementAppUsage(app.packageName)
            }
        }
    }
    
    fun onAppLongPress(app: AppInfo, position: Pair<Float, Float>) {
        _state.value = _state.value.copy(
            selectedApp = app,
            showContextMenu = true,
            contextMenuPosition = position
        )
    }
    
    fun hideContextMenu() {
        _state.value = _state.value.copy(
            showContextMenu = false,
            selectedApp = null
        )
    }
    
    fun pinApp(app: AppInfo) {
        viewModelScope.launch {
            if (app.isPinned) {
                preferences.unpinApp(app.packageName)
            } else {
                preferences.pinApp(app.packageName)
            }
        }
        hideContextMenu()
    }
    
    fun showRenameDialog(app: AppInfo) {
        _state.value = _state.value.copy(
            selectedApp = app,
            showRenameDialog = true,
            showContextMenu = false
        )
    }
    
    fun hideRenameDialog() {
        _state.value = _state.value.copy(
            showRenameDialog = false,
            selectedApp = null
        )
    }
    
    fun renameApp(app: AppInfo, newName: String) {
        viewModelScope.launch {
            preferences.renameApp(app.packageName, newName)
        }
        hideRenameDialog()
    }
    
    fun openAppSettings(app: AppInfo) {
        repository.openAppSettings(app.packageName)
        hideContextMenu()
    }
    
    fun toggleTheme() {
        val isDark = preferences.isDarkTheme()
        preferences.setDarkTheme(!isDark)
        _state.value = _state.value.copy(isDarkTheme = !isDark)
    }
    
    fun refreshApps() {
        loadApps()
    }
} 