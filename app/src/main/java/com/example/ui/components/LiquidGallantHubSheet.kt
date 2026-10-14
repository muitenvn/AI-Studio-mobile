package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidEmerald
import com.example.ui.theme.LiquidGlassBorder
import com.example.ui.theme.LiquidGlassSurface
import com.example.ui.theme.LiquidMagenta
import com.example.ui.theme.LiquidTextMuted
import com.example.ui.theme.LiquidTextPrimary
import com.example.ui.theme.LiquidTextSecondary
import com.example.ui.theme.LiquidViolet
import com.example.viewmodel.StudioViewModel

data class LiquidDestination(
    val title: String,
    val subtitle: String,
    val url: String,
    val icon: ImageVector,
    val glowColor: Color,
    val isHighlighted: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiquidGallantHubSheet(
    viewModel: StudioViewModel,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val destinations = listOf(
        LiquidDestination(
            title = "🚀 New App Builder",
            subtitle = "Google AI Studio New App workspace",
            url = "https://aistudio.google.com/apps/new",
            icon = Icons.Default.AutoAwesome,
            glowColor = LiquidCyan,
            isHighlighted = true
        ),
        LiquidDestination(
            title = "🐙 GitHub Web",
            subtitle = "Repositories, code, commits and APK builds",
            url = "https://github.com",
            icon = Icons.Default.Code,
            glowColor = Color(0xFF60A5FA),
            isHighlighted = true
        ),
        LiquidDestination(
            title = "➕ New GitHub Repository",
            subtitle = "Create a repo to host and compile your app",
            url = "https://github.com/new",
            icon = Icons.Default.FolderSpecial,
            glowColor = LiquidEmerald
        ),
        LiquidDestination(
            title = "🏠 Studio Dashboard",
            subtitle = "AI Studio homepage, prompt files & library",
            url = "https://aistudio.google.com",
            icon = Icons.Default.Home,
            glowColor = LiquidViolet
        ),
        LiquidDestination(
            title = "🔑 Get & Manage API Keys",
            subtitle = "Manage Gemini API credentials",
            url = "https://aistudio.google.com/app/apikey",
            icon = Icons.Default.Key,
            glowColor = Color(0xFFFFB703)
        ),
        LiquidDestination(
            title = "💬 Chat Prompt Playground",
            subtitle = "Interactive multi-turn prompt sandbox",
            url = "https://aistudio.google.com/prompts/new_chat",
            icon = Icons.Default.Chat,
            glowColor = LiquidMagenta
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xF50D1120),
        scrimColor = Color(0x99000000),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(LiquidCyan.copy(alpha = 0.5f), LiquidViolet.copy(alpha = 0.5f))
                        )
                    )
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sheet Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(LiquidCyan)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "LIQUID GALLANT HUB",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.8.sp,
                            fontWeight = FontWeight.Black
                        ),
                        color = LiquidCyan
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x26FFFFFF)
                    ) {
                        Text(
                            text = "v2.0 Liquid",
                            style = MaterialTheme.typography.labelSmall,
                            color = LiquidTextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Control Center & Direct Navigators",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = LiquidTextPrimary
                )
            }

            // Desktop View Switch Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x401B223C)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LiquidGlassBorder, RoundedCornerShape(18.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (viewModel.isDesktopMode.value) LiquidCyan.copy(alpha = 0.2f)
                                    else Color(0x20FFFFFF)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (viewModel.isDesktopMode.value) Icons.Default.Computer else Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = if (viewModel.isDesktopMode.value) LiquidCyan else LiquidTextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Desktop View Engine",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = LiquidTextPrimary
                            )
                            Text(
                                text = if (viewModel.isDesktopMode.value) "Active (Full PC Studio layout)" else "Mobile viewport enabled",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (viewModel.isDesktopMode.value) LiquidCyan else LiquidTextMuted
                            )
                        }
                        Switch(
                            checked = viewModel.isDesktopMode.value,
                            onCheckedChange = { viewModel.toggleDesktopMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = LiquidCyan,
                                uncheckedTrackColor = Color(0x33FFFFFF)
                            )
                        )
                    }
                }
            }

            // Quick Destinations Grid / Cards
            items(destinations.size) { index ->
                val dest = destinations[index]
                val borderBrush = if (dest.isHighlighted) {
                    Brush.linearGradient(listOf(dest.glowColor.copy(alpha = 0.8f), LiquidViolet.copy(alpha = 0.5f)))
                } else {
                    Brush.linearGradient(listOf(Color(0x26FFFFFF), Color(0x10FFFFFF)))
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (dest.isHighlighted) Color(0x4D1B2446) else Color(0x26141A30)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, borderBrush, RoundedCornerShape(16.dp))
                        .clickable { viewModel.navigateToUrl(dest.url) }
                        .testTag("hub_dest_$index")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(dest.glowColor.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = dest.icon,
                                contentDescription = null,
                                tint = dest.glowColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = dest.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = LiquidTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dest.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = LiquidTextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Navigate",
                            tint = dest.glowColor.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Browser Quick Actions Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionGlassButton(
                        icon = Icons.Default.ContentCopy,
                        label = "Copy URL",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("URL", viewModel.currentUrl.value)
                            clipboard.setPrimaryClip(clip)
                            viewModel.showToast("URL copied to clipboard")
                        }
                    )
                    QuickActionGlassButton(
                        icon = Icons.Default.Share,
                        label = "Share",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            try {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, viewModel.currentUrl.value)
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share"))
                            } catch (e: Exception) {
                                viewModel.showToast("Cannot share link")
                            }
                        }
                    )
                    QuickActionGlassButton(
                        icon = Icons.AutoMirrored.Filled.OpenInNew,
                        label = "External",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.currentUrl.value))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                viewModel.showToast("Cannot open external browser")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionGlassButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0x29FFFFFF),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LiquidCyan,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = LiquidTextPrimary
            )
        }
    }
}
