package com.appdrawer.app.model

data class AppDrawerState(
    val allApps: List<AppInfo> = emptyList(),
    val filteredApps: List<AppInfo> = emptyList(),
    val pinnedApps: List<AppInfo> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val selectedApp: AppInfo? = null,
    val showContextMenu: Boolean = false,
    val showRenameDialog: Boolean = false,
    val isDarkTheme: Boolean = false,
    val contextMenuPosition: Pair<Float, Float> = 0f to 0f
) 