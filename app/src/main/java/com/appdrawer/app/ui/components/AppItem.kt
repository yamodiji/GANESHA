package com.appdrawer.app.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.appdrawer.app.model.AppInfo

@Composable
fun AppItem(
    app: AppInfo,
    onClick: () -> Unit,
    onLongClick: (Pair<Float, Float>) -> Unit,
    modifier: Modifier = Modifier,
    showPin: Boolean = true
) {
    val density = LocalDensity.current
    var position by remember { mutableStateOf(0f to 0f) }
    
    Column(
        modifier = modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { onLongClick(position) }
            )
            .onGloballyPositioned { coordinates ->
                val positionInParent = coordinates.positionInParent()
                position = with(density) {
                    positionInParent.x.toDp().value to positionInParent.y.toDp().value
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            val iconBitmap = remember(app.icon) {
                app.icon.toBitmap(72, 72).asImageBitmap()
            }
            
            Image(
                bitmap = iconBitmap,
                contentDescription = app.displayName,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    )
            )
            
            if (app.isPinned && showPin) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = "Pinned",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                        .padding(2.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = app.displayName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(72.dp)
        )
    }
}

@Composable
fun AppItemList(
    app: AppInfo,
    onClick: () -> Unit,
    onLongClick: (Pair<Float, Float>) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var position by remember { mutableStateOf(0f to 0f) }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = { onLongClick(position) }
            )
            .onGloballyPositioned { coordinates ->
                val positionInParent = coordinates.positionInParent()
                position = with(density) {
                    positionInParent.x.toDp().value to positionInParent.y.toDp().value
                }
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            val iconBitmap = remember(app.icon) {
                app.icon.toBitmap(48, 48).asImageBitmap()
            }
            
            Image(
                bitmap = iconBitmap,
                contentDescription = app.displayName,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    )
            )
            
            if (app.isPinned) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = "Pinned",
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                        .padding(1.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = app.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (app.launchCount > 0) {
                Text(
                    text = "Opened ${app.launchCount} times",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 