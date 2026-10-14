package com.example.ui.screens

import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.ui.theme.LiquidBorderGlassSubtle
import com.example.ui.theme.LiquidGlassFill
import com.example.ui.theme.LiquidGlassHeader
import com.example.ui.theme.LiquidTextGray
import com.example.ui.theme.LiquidTextWhite
import com.example.viewmodel.StudioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioWebScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    val isDesktopMode by viewModel.isDesktopMode.collectAsStateWithLifecycle()
    val aiStudioProgress by viewModel.aiStudioProgress.collectAsStateWithLifecycle()
    val aiStudioLoading by viewModel.aiStudioLoading.collectAsStateWithLifecycle()
    val githubProgress by viewModel.githubProgress.collectAsStateWithLifecycle()
    val githubLoading by viewModel.githubLoading.collectAsStateWithLifecycle()

    var aiStudioWebViewRef by remember { mutableStateOf<WebView?>(null) }
    var githubWebViewRef by remember { mutableStateOf<WebView?>(null) }

    val isAIStudioActive = pagerState.currentPage == 0
    val currentProgress = if (isAIStudioActive) aiStudioProgress else githubProgress
    val currentLoading = if (isAIStudioActive) aiStudioLoading else githubLoading

    // Pull to refresh states
    var isRefreshingAI by remember { mutableStateOf(false) }
    var isRefreshingGH by remember { mutableStateOf(false) }
    val refreshStateAI = rememberPullToRefreshState()
    val refreshStateGH = rememberPullToRefreshState()

    LaunchedEffect(pagerState.currentPage) {
        viewModel.setTab(pagerState.currentPage)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LiquidBgDark)
    ) {
        // --- HORIZONTAL PAGER: WEBVIEWS ---
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                // Tab 0: AI Studio with Pull-to-Refresh
                PullToRefreshBox(
                    isRefreshing = isRefreshingAI,
                    state = refreshStateAI,
                    onRefresh = {
                        isRefreshingAI = true
                        aiStudioWebViewRef?.reload()
                        coroutineScope.launch {
                            delay(1000)
                            isRefreshingAI = false
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    StudioWebView(
                        url = viewModel.aiStudioUrl,
                        isActive = pagerState.currentPage == 0,
                        isDesktopMode = isDesktopMode,
                        onStateUpdate = { title, progress, canBack ->
                            viewModel.updateAIStudio(title, progress, canBack)
                            if (progress == 100) isRefreshingAI = false
                        },
                        webViewInstance = { aiStudioWebViewRef = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // Tab 1: GitHub with Pull-to-Refresh
                PullToRefreshBox(
                    isRefreshing = isRefreshingGH,
                    state = refreshStateGH,
                    onRefresh = {
                        isRefreshingGH = true
                        githubWebViewRef?.reload()
                        coroutineScope.launch {
                            delay(1000)
                            isRefreshingGH = false
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    StudioWebView(
                        url = viewModel.githubUrl,
                        isActive = pagerState.currentPage == 1,
                        isDesktopMode = isDesktopMode,
                        onStateUpdate = { title, progress, canBack ->
                            viewModel.updateGitHub(title, progress, canBack)
                            if (progress == 100) isRefreshingGH = false
                        },
                        webViewInstance = { githubWebViewRef = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // --- SLΕEK TOP LINK BAR WITH SWIPE-TO-SWITCH GESTURE ---
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
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Swipe horizontally strictly on this top link bar area to switch tabs
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onHorizontalDrag = { _, dragAmount ->
                                        if (dragAmount > 35 && pagerState.currentPage > 0) {
                                            coroutineScope.launch { pagerState.animateScrollToPage(0) }
                                        } else if (dragAmount < -35 && pagerState.currentPage < 1) {
                                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                        }
                                    }
                                )
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Address bar capsule
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = LiquidGlassFill,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .border(1.dp, LiquidBorderGlassSubtle, RoundedCornerShape(20.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Secure",
                                    tint = LiquidAccentGreen,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = if (isAIStudioActive) "aistudio.google.com" else "github.com",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = LiquidTextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                // Spacious, perfectly formatted tab pills (Studio | GitHub)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TopHeaderTabButton(
                                        label = "Studio",
                                        isSelected = isAIStudioActive,
                                        activeColor = LiquidAccentCyan,
                                        onClick = {
                                            coroutineScope.launch { pagerState.animateScrollToPage(0) }
                                        }
                                    )
                                    TopHeaderTabButton(
                                        label = "GitHub",
                                        isSelected = !isAIStudioActive,
                                        activeColor = LiquidAccentBlue,
                                        onClick = {
                                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                        }
                                    )
                                }
                            }
                        }

                        // Desktop / Mobile Mode Toggle (Clean button)
                        IconButton(
                            onClick = { viewModel.toggleDesktopMode() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isDesktopMode) Icons.Default.Computer else Icons.Default.PhoneAndroid,
                                contentDescription = "Toggle Desktop View",
                                tint = if (isDesktopMode) LiquidAccentCyan else LiquidTextGray,
                                modifier = Modifier.size(18.dp)
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
    }
}

@Composable
private fun TopHeaderTabButton(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) activeColor.copy(alpha = 0.3f) else Color.Transparent
    val border = if (isSelected) LiquidBorderActive else Color.Transparent
    val textColor = if (isSelected) LiquidTextWhite else LiquidTextGray

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, border, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp
            ),
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
