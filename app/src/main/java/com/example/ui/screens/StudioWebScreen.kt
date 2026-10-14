package com.example.ui.screens

import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.StudioWebView
import com.example.ui.theme.LiquidAccentBlue
import com.example.ui.theme.LiquidAccentCyan
import com.example.ui.theme.LiquidAccentGreen
import com.example.ui.theme.LiquidBgDark
import com.example.ui.theme.LiquidBorderActive
import com.example.ui.theme.LiquidBorderGlass
import com.example.ui.theme.LiquidBorderGlassSubtle
import com.example.ui.theme.LiquidGlassFill
import com.example.ui.theme.LiquidGlassHeader
import com.example.ui.theme.LiquidTextDim
import com.example.ui.theme.LiquidTextGray
import com.example.ui.theme.LiquidTextWhite
import com.example.viewmodel.StudioViewModel
import kotlinx.coroutines.launch

@Composable
fun StudioWebScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    val isDesktopMode by viewModel.isDesktopMode.collectAsStateWithLifecycle()
    val aiStudioTitle by viewModel.aiStudioTitle.collectAsStateWithLifecycle()
    val aiStudioProgress by viewModel.aiStudioProgress.collectAsStateWithLifecycle()
    val aiStudioLoading by viewModel.aiStudioLoading.collectAsStateWithLifecycle()
    val aiStudioCanBack by viewModel.aiStudioCanBack.collectAsStateWithLifecycle()

    val githubTitle by viewModel.githubTitle.collectAsStateWithLifecycle()
    val githubProgress by viewModel.githubProgress.collectAsStateWithLifecycle()
    val githubLoading by viewModel.githubLoading.collectAsStateWithLifecycle()
    val githubCanBack by viewModel.githubCanBack.collectAsStateWithLifecycle()

    var aiStudioWebViewRef by remember { mutableStateOf<WebView?>(null) }
    var githubWebViewRef by remember { mutableStateOf<WebView?>(null) }

    val isAIStudioActive = pagerState.currentPage == 0
    val currentTitle = if (isAIStudioActive) aiStudioTitle else githubTitle
    val currentProgress = if (isAIStudioActive) aiStudioProgress else githubProgress
    val currentLoading = if (isAIStudioActive) aiStudioLoading else githubLoading
    val currentCanBack = if (isAIStudioActive) aiStudioCanBack else githubCanBack

    LaunchedEffect(pagerState.currentPage) {
        viewModel.setTab(pagerState.currentPage)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LiquidBgDark)
    ) {
        // --- HORIZONTAL PAGER: SWIPE LEFT / RIGHT TO SWITCH TABS ---
        // Both web pages stay 100% alive in memory! No reloads on swipe!
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                // Tab 0: Google AI Studio
                StudioWebView(
                    url = viewModel.aiStudioUrl,
                    isActive = pagerState.currentPage == 0,
                    isDesktopMode = isDesktopMode,
                    onStateUpdate = { title, progress, canBack ->
                        viewModel.updateAIStudio(title, progress, canBack)
                    },
                    webViewInstance = { aiStudioWebViewRef = it },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Tab 1: GitHub
                StudioWebView(
                    url = viewModel.githubUrl,
                    isActive = pagerState.currentPage == 1,
                    isDesktopMode = isDesktopMode,
                    onStateUpdate = { title, progress, canBack ->
                        viewModel.updateGitHub(title, progress, canBack)
                    },
                    webViewInstance = { githubWebViewRef = it },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // --- BROWSER TOP BAR (MINIMALIST & FULL SCREEN) ---
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Surface(
                color = LiquidGlassHeader,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 0.5.dp, color = LiquidBorderGlassSubtle)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back navigation button
                        if (currentCanBack) {
                            IconButton(
                                onClick = {
                                    if (isAIStudioActive) {
                                        aiStudioWebViewRef?.goBack()
                                    } else {
                                        githubWebViewRef?.goBack()
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = LiquidTextWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Minimalist Browser Address & Tab Switcher Bar
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = LiquidGlassFill,
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .border(1.dp, LiquidBorderGlassSubtle, RoundedCornerShape(18.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Secure",
                                    tint = LiquidAccentGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))

                                // Active Domain / Page Name
                                Text(
                                    text = if (isAIStudioActive) "aistudio.google.com" else "github.com",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = LiquidTextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )

                                // Swipe Tabs Indicator: [ AI Studio ] | [ GitHub ]
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 6.dp)
                                ) {
                                    TabIndicatorDot(
                                        label = "Studio",
                                        isSelected = isAIStudioActive,
                                        onClick = {
                                            coroutineScope.launch { pagerState.animateScrollToPage(0) }
                                        }
                                    )
                                    TabIndicatorDot(
                                        label = "GitHub",
                                        isSelected = !isAIStudioActive,
                                        onClick = {
                                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Desktop / Mobile Mode Toggle
                        IconButton(
                            onClick = { viewModel.toggleDesktopMode() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isDesktopMode) Icons.Default.Computer else Icons.Default.PhoneAndroid,
                                contentDescription = "Toggle Desktop View",
                                tint = if (isDesktopMode) LiquidAccentCyan else LiquidTextGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Reload Button
                        IconButton(
                            onClick = {
                                if (isAIStudioActive) {
                                    aiStudioWebViewRef?.reload()
                                } else {
                                    githubWebViewRef?.reload()
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reload",
                                tint = LiquidTextGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Slim Loading Progress Bar
            AnimatedVisibility(visible = currentLoading) {
                LinearProgressIndicator(
                    progress = { currentProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = LiquidAccentCyan,
                    trackColor = Color.Transparent
                )
            }
        }

        // --- BOTTOM SWIPE GESTURE BAR (INDICATOR) ---
        // Subtle hint bar at the bottom: swipe horizontally to glide between tabs
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x33000000),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        val nextPage = if (pagerState.currentPage == 0) 1 else 0
                        coroutineScope.launch { pagerState.animateScrollToPage(nextPage) }
                    }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = if (isAIStudioActive) 18.dp else 6.dp, height = 4.dp)
                            .clip(CircleShape)
                            .background(if (isAIStudioActive) LiquidAccentCyan else LiquidTextDim)
                    )
                    Box(
                        modifier = Modifier
                            .size(width = if (!isAIStudioActive) 18.dp else 6.dp, height = 4.dp)
                            .clip(CircleShape)
                            .background(if (!isAIStudioActive) LiquidAccentBlue else LiquidTextDim)
                    )
                    Text(
                        text = if (isAIStudioActive) "Swipe to GitHub →" else "← Swipe to AI Studio",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = LiquidTextGray
                    )
                }
            }
        }
    }
}

@Composable
private fun TabIndicatorDot(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) LiquidAccentBlue.copy(alpha = 0.35f) else Color.Transparent,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                1.dp,
                if (isSelected) LiquidBorderActive else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp
            ),
            color = if (isSelected) LiquidTextWhite else LiquidTextDim,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}
