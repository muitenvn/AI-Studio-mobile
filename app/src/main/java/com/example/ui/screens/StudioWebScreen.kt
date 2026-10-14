package com.example.ui.screens

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.LiquidGallantHubSheet
import com.example.ui.components.StudioWebView
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidDarkBg
import com.example.ui.theme.LiquidEmerald
import com.example.ui.theme.LiquidGlassBorder
import com.example.ui.theme.LiquidMagenta
import com.example.ui.theme.LiquidTextMuted
import com.example.ui.theme.LiquidTextPrimary
import com.example.ui.theme.LiquidTextSecondary
import com.example.ui.theme.LiquidViolet
import com.example.viewmodel.StudioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioWebScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val currentUrl by viewModel.currentUrl.collectAsStateWithLifecycle()
    val webTitle by viewModel.webTitle.collectAsStateWithLifecycle()
    val progress by viewModel.webProgress.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isDesktopMode by viewModel.isDesktopMode.collectAsStateWithLifecycle()
    val canGoBack by viewModel.canGoBack.collectAsStateWithLifecycle()
    val canGoForward by viewModel.canGoForward.collectAsStateWithLifecycle()
    val loadError by viewModel.loadError.collectAsStateWithLifecycle()
    val isHubSheetOpen by viewModel.isHubSheetOpen.collectAsStateWithLifecycle()

    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Back handling: if hub sheet is open, close sheet; else let WebView handle
    BackHandler(enabled = isHubSheetOpen) {
        coroutineScope.launch { sheetState.hide() }
        viewModel.setHubSheetOpen(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LiquidDarkBg)
    ) {
        // Full Edge-to-Edge WebView
        StudioWebView(
            url = currentUrl,
            isDesktopMode = isDesktopMode,
            onProgressChange = { viewModel.updateWebProgress(it) },
            onTitleChange = { viewModel.updateWebTitle(it) },
            onHistoryChange = { back, forward -> viewModel.updateWebHistory(back, forward) },
            onError = { viewModel.setLoadError(it) },
            webViewInstance = { webViewRef = it },
            modifier = Modifier.fillMaxSize()
        )

        // Top Translucent Liquid Glass Header
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Surface(
                color = Color(0xD9090C18),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Glowing status indicator
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isLoading) LiquidCyan else LiquidEmerald)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        // App & Page Title
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "AI Studio Mobile",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                ),
                                color = LiquidCyan
                            )
                            Text(
                                text = webTitle.ifBlank { currentUrl },
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = LiquidTextSecondary
                            )
                        }

                        // Desktop Mode Status Chip
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isDesktopMode) LiquidCyan.copy(alpha = 0.2f) else Color(0x20FFFFFF),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    if (isDesktopMode) LiquidCyan.copy(alpha = 0.6f) else Color(0x26FFFFFF),
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { viewModel.toggleDesktopMode() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isDesktopMode) Icons.Default.Computer else Icons.Default.PhoneAndroid,
                                    contentDescription = null,
                                    tint = if (isDesktopMode) LiquidCyan else LiquidTextMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isDesktopMode) "PC" else "Mobile",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = if (isDesktopMode) LiquidCyan else LiquidTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Glowing Neon Gradient Loading Bar
            AnimatedVisibility(visible = isLoading) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp),
                    color = LiquidCyan,
                    trackColor = Color.Transparent
                )
            }
        }

        // Bottom Liquid Gallant Floating Dock Capsule
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = Color(0xEA0D1122),
                shadowElevation = 16.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .border(
                        1.2.dp,
                        Brush.horizontalGradient(
                            listOf(
                                LiquidCyan.copy(alpha = 0.5f),
                                LiquidViolet.copy(alpha = 0.5f),
                                LiquidMagenta.copy(alpha = 0.4f)
                            )
                        ),
                        RoundedCornerShape(32.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Back
                    IconButton(
                        onClick = { webViewRef?.goBack() },
                        enabled = canGoBack,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back",
                            tint = if (canGoBack) LiquidTextPrimary else LiquidTextMuted.copy(alpha = 0.4f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // 🚀 Radiant "New App" Glowing Pill
                    LiquidDockPill(
                        icon = Icons.Default.AutoAwesome,
                        label = "New App",
                        isPrimary = true,
                        isSelected = currentUrl.contains("apps/new"),
                        onClick = { viewModel.openNewApp() },
                        modifier = Modifier.testTag("dock_new_app")
                    )

                    // 🐙 "GitHub" Glass Pill
                    LiquidDockPill(
                        icon = Icons.Default.Code,
                        label = "GitHub",
                        isPrimary = false,
                        isSelected = currentUrl.contains("github.com"),
                        onClick = { viewModel.openGitHub() },
                        modifier = Modifier.testTag("dock_github")
                    )

                    // ⚡ "Liquid Hub" Control Center Button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = LiquidViolet.copy(alpha = 0.22f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, LiquidViolet.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                            .clickable { viewModel.toggleHubSheet() }
                            .testTag("dock_hub_trigger")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = "Hub",
                                tint = LiquidViolet,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Hub",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = LiquidTextPrimary
                            )
                        }
                    }

                    // Reload
                    IconButton(
                        onClick = { webViewRef?.reload() },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reload",
                            tint = LiquidCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Error Screen
        if (loadError != null) {
            Surface(
                color = LiquidDarkBg.copy(alpha = 0.94f),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0x331E2442)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, LiquidGlassBorder, RoundedCornerShape(20.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(46.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Network Connection Error",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LiquidTextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = loadError ?: "Failed to reach AI Studio servers.",
                                style = MaterialTheme.typography.bodySmall,
                                color = LiquidTextSecondary
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = {
                                    viewModel.clearLoadError()
                                    webViewRef?.reload()
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Retry Connection")
                            }
                        }
                    }
                }
            }
        }

        // Liquid Gallant Control Hub Sheet
        if (isHubSheetOpen) {
            LiquidGallantHubSheet(
                viewModel = viewModel,
                sheetState = sheetState,
                onDismiss = { viewModel.setHubSheetOpen(false) }
            )
        }
    }
}

@Composable
fun LiquidDockPill(
    icon: ImageVector,
    label: String,
    isPrimary: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerBrush = if (isPrimary || isSelected) {
        Brush.horizontalGradient(listOf(LiquidCyan.copy(alpha = 0.35f), Color(0xFF2563EB).copy(alpha = 0.35f)))
    } else {
        Brush.horizontalGradient(listOf(Color(0x1AFFFFFF), Color(0x10FFFFFF)))
    }

    val borderBrush = if (isPrimary || isSelected) {
        Brush.horizontalGradient(listOf(LiquidCyan, Color(0xFF60A5FA)))
    } else {
        Brush.horizontalGradient(listOf(Color(0x26FFFFFF), Color(0x10FFFFFF)))
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(containerBrush)
            .border(1.dp, borderBrush, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isPrimary || isSelected) LiquidCyan else LiquidTextSecondary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = if (isPrimary || isSelected) LiquidTextPrimary else LiquidTextSecondary
            )
        }
    }
}
