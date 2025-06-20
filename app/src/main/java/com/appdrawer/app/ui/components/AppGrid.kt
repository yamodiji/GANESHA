package com.appdrawer.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.appdrawer.app.R
import com.appdrawer.app.model.AppInfo

@Composable
fun AppGrid(
    apps: List<AppInfo>,
    pinnedApps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo, Pair<Float, Float>) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    showPinnedSection: Boolean = true
) {
    var isGridView by remember { mutableStateOf(true) }
    
    Column(modifier = modifier.fillMaxSize()) {
        // View toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (apps.isEmpty() && !isLoading) {
                    stringResource(R.string.no_apps_found)
                } else {
                    "${apps.size} apps"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            IconButton(
                onClick = { isGridView = !isGridView }
            ) {
                Icon(
                    imageVector = if (isGridView) Icons.Default.List else Icons.Default.Apps,
                    contentDescription = if (isGridView) "List view" else "Grid view",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            if (isGridView) {
                AppGridView(
                    apps = apps,
                    pinnedApps = pinnedApps,
                    onAppClick = onAppClick,
                    onAppLongClick = onAppLongClick,
                    showPinnedSection = showPinnedSection
                )
            } else {
                AppListView(
                    apps = apps,
                    pinnedApps = pinnedApps,
                    onAppClick = onAppClick,
                    onAppLongClick = onAppLongClick,
                    showPinnedSection = showPinnedSection
                )
            }
        }
    }
}

@Composable
private fun AppGridView(
    apps: List<AppInfo>,
    pinnedApps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo, Pair<Float, Float>) -> Unit,
    showPinnedSection: Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Pinned apps section
        if (showPinnedSection && pinnedApps.isNotEmpty()) {
            item(span = { maxLineSpan }) {
                SectionHeader(title = stringResource(R.string.pinned_apps))
            }
            
            items(pinnedApps) { app ->
                AppItem(
                    app = app,
                    onClick = { onAppClick(app) },
                    onLongClick = { position -> onAppLongClick(app, position) },
                    showPin = false
                )
            }
            
            item(span = { maxLineSpan }) {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader(title = stringResource(R.string.all_apps))
            }
        }
        
        // All apps
        items(apps.filter { !it.isPinned || !showPinnedSection }) { app ->
            AppItem(
                app = app,
                onClick = { onAppClick(app) },
                onLongClick = { position -> onAppLongClick(app, position) },
                showPin = !showPinnedSection
            )
        }
    }
}

@Composable
private fun AppListView(
    apps: List<AppInfo>,
    pinnedApps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo, Pair<Float, Float>) -> Unit,
    showPinnedSection: Boolean
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Pinned apps section
        if (showPinnedSection && pinnedApps.isNotEmpty()) {
            item {
                SectionHeader(title = stringResource(R.string.pinned_apps))
            }
            
            items(pinnedApps) { app ->
                AppItemList(
                    app = app,
                    onClick = { onAppClick(app) },
                    onLongClick = { position -> onAppLongClick(app, position) }
                )
            }
            
            item {
                SectionHeader(title = stringResource(R.string.all_apps))
            }
        }
        
        // All apps
        items(apps.filter { !it.isPinned || !showPinnedSection }) { app ->
            AppItemList(
                app = app,
                onClick = { onAppClick(app) },
                onLongClick = { position -> onAppLongClick(app, position) }
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
} 