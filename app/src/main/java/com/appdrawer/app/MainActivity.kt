package com.appdrawer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appdrawer.app.model.AppInfo
import com.appdrawer.app.ui.components.*
import com.appdrawer.app.ui.theme.AppDrawerTheme
import com.appdrawer.app.viewmodel.AppDrawerViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: AppDrawerViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            AppDrawerTheme(
                darkTheme = false // Will be handled by ViewModel
            ) {
                AppDrawerScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerScreen(
    viewModel: AppDrawerViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top App Bar
                TopAppBar(
                    title = {
                        Text(
                            text = "App Drawer",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.refreshApps() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh apps",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        IconButton(
                            onClick = { viewModel.toggleTheme() }
                        ) {
                            Icon(
                                imageVector = if (state.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle theme",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                
                // Search Bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        onClearQuery = { 
                            viewModel.clearSearch()
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(16.dp),
                        autoFocus = true
                    )
                }
                
                // Apps Grid/List
                AppGrid(
                    apps = state.filteredApps,
                    pinnedApps = state.pinnedApps,
                    onAppClick = { app ->
                        viewModel.launchApp(app)
                        focusManager.clearFocus()
                    },
                    onAppLongClick = { app, position ->
                        viewModel.onAppLongPress(app, position)
                        focusManager.clearFocus()
                    },
                    isLoading = state.isLoading,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    showPinnedSection = searchQuery.isBlank()
                )
            }
            
            // Context Menu
            state.selectedApp?.let { app ->
                AppContextMenu(
                    app = app,
                    isVisible = state.showContextMenu,
                    position = state.contextMenuPosition,
                    onDismiss = { viewModel.hideContextMenu() },
                    onPin = { viewModel.pinApp(app) },
                    onRename = { viewModel.showRenameDialog(app) },
                    onAppInfo = { viewModel.openAppSettings(app) }
                )
            }
            
            // Rename Dialog
            RenameAppDialog(
                app = state.selectedApp,
                isVisible = state.showRenameDialog,
                onDismiss = { viewModel.hideRenameDialog() },
                onRename = { newName ->
                    state.selectedApp?.let { app ->
                        viewModel.renameApp(app, newName)
                    }
                }
            )
        }
    }
} 