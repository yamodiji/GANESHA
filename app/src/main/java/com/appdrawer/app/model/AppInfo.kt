package com.appdrawer.app.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    val displayName: String = appName, // Custom name for renamed apps
    val icon: Drawable,
    val isPinned: Boolean = false,
    val isSystemApp: Boolean = false,
    val launchCount: Int = 0,
    val lastUsed: Long = 0L
) {
    // For fuzzy search matching
    fun matchesSearch(query: String): Boolean {
        if (query.isBlank()) return true
        
        val searchTerms = query.lowercase().split(" ")
        val appNameLower = displayName.lowercase()
        val packageNameLower = packageName.lowercase()
        
        return searchTerms.all { term ->
            appNameLower.contains(term) || 
            packageNameLower.contains(term) ||
            fuzzyMatch(appNameLower, term)
        }
    }
    
    private fun fuzzyMatch(source: String, term: String): Boolean {
        if (term.length < 2) return false
        
        // Simple fuzzy matching: check if most characters of term are in source
        val termChars = term.toCharArray().distinct()
        val sourceChars = source.toCharArray().distinct()
        
        val matchedChars = termChars.count { it in sourceChars }
        return matchedChars >= (termChars.size * 0.6).toInt()
    }
} 