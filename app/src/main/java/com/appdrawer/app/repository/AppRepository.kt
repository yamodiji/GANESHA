package com.appdrawer.app.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import com.appdrawer.app.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {
    private val packageManager = context.packageManager
    
    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        val queryFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PackageManager.MATCH_ALL
        } else {
            0
        }
        
        val resolveInfoList = packageManager.queryIntentActivities(intent, queryFlags)
        
        resolveInfoList.mapNotNull { resolveInfo ->
            try {
                createAppInfo(resolveInfo)
            } catch (e: Exception) {
                null // Skip apps that can't be loaded
            }
        }.sortedBy { it.appName.lowercase() }
    }
    
    private fun createAppInfo(resolveInfo: ResolveInfo): AppInfo? {
        val packageName = resolveInfo.activityInfo.packageName
        val appName = resolveInfo.loadLabel(packageManager).toString()
        val icon = resolveInfo.loadIcon(packageManager)
        
        val applicationInfo = try {
            packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
        
        val isSystemApp = (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
        
        return AppInfo(
            packageName = packageName,
            appName = appName,
            icon = icon,
            isSystemApp = isSystemApp
        )
    }
    
    fun launchApp(packageName: String): Boolean {
        return try {
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
    
    fun openAppSettings(packageName: String) {
        try {
            val intent = Intent().apply {
                action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = android.net.Uri.parse("package:$packageName")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handle error silently
        }
    }
} 