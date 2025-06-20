package com.appdrawer.app.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_drawer_prefs", Context.MODE_PRIVATE)
    
    private val _pinnedApps = MutableStateFlow(getPinnedApps())
    val pinnedApps: Flow<Set<String>> = _pinnedApps.asStateFlow()
    
    private val _customNames = MutableStateFlow(getCustomNames())
    val customNames: Flow<Map<String, String>> = _customNames.asStateFlow()
    
    private val _appUsage = MutableStateFlow(getAppUsage())
    val appUsage: Flow<Map<String, Pair<Int, Long>>> = _appUsage.asStateFlow()
    
    fun pinApp(packageName: String) {
        val currentPinned = _pinnedApps.value.toMutableSet()
        currentPinned.add(packageName)
        savePinnedApps(currentPinned)
        _pinnedApps.value = currentPinned
    }
    
    fun unpinApp(packageName: String) {
        val currentPinned = _pinnedApps.value.toMutableSet()
        currentPinned.remove(packageName)
        savePinnedApps(currentPinned)
        _pinnedApps.value = currentPinned
    }
    
    fun renameApp(packageName: String, newName: String) {
        val currentNames = _customNames.value.toMutableMap()
        if (newName.isBlank()) {
            currentNames.remove(packageName)
        } else {
            currentNames[packageName] = newName
        }
        saveCustomNames(currentNames)
        _customNames.value = currentNames
    }
    
    fun incrementAppUsage(packageName: String) {
        val currentUsage = _appUsage.value.toMutableMap()
        val (count, _) = currentUsage[packageName] ?: (0 to 0L)
        currentUsage[packageName] = (count + 1) to System.currentTimeMillis()
        saveAppUsage(currentUsage)
        _appUsage.value = currentUsage
    }
    
    fun isDarkTheme(): Boolean = prefs.getBoolean("dark_theme", false)
    
    fun setDarkTheme(isDark: Boolean) {
        prefs.edit().putBoolean("dark_theme", isDark).apply()
    }
    
    private fun getPinnedApps(): Set<String> {
        return prefs.getStringSet("pinned_apps", emptySet()) ?: emptySet()
    }
    
    private fun savePinnedApps(pinnedApps: Set<String>) {
        prefs.edit().putStringSet("pinned_apps", pinnedApps).apply()
    }
    
    private fun getCustomNames(): Map<String, String> {
        val customNamesJson = prefs.getString("custom_names", "{}") ?: "{}"
        return try {
            customNamesJson.split(",")
                .filter { it.contains(":") }
                .associate {
                    val parts = it.split(":")
                    parts[0].trim() to parts[1].trim()
                }
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    private fun saveCustomNames(customNames: Map<String, String>) {
        val customNamesJson = customNames.entries.joinToString(",") { "${it.key}:${it.value}" }
        prefs.edit().putString("custom_names", customNamesJson).apply()
    }
    
    private fun getAppUsage(): Map<String, Pair<Int, Long>> {
        val usageJson = prefs.getString("app_usage", "{}") ?: "{}"
        return try {
            usageJson.split(",")
                .filter { it.contains(":") }
                .associate {
                    val parts = it.split(":")
                    if (parts.size >= 3) {
                        parts[0].trim() to (parts[1].trim().toInt() to parts[2].trim().toLong())
                    } else {
                        parts[0].trim() to (0 to 0L)
                    }
                }
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    private fun saveAppUsage(appUsage: Map<String, Pair<Int, Long>>) {
        val usageJson = appUsage.entries.joinToString(",") { "${it.key}:${it.value.first}:${it.value.second}" }
        prefs.edit().putString("app_usage", usageJson).apply()
    }
} 