package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GeminiBlue
import com.example.ui.theme.GeminiCyan
import com.example.ui.theme.GeminiGradientEnd
import com.example.ui.theme.GeminiGradientStart
import com.example.ui.theme.GeminiPurple
import com.example.ui.theme.StudioPrimary
import com.example.viewmodel.StudioViewModel

data class HubShortcut(
    val title: String,
    val description: String,
    val url: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun HubScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val shortcuts = listOf(
        HubShortcut(
            title = "Chat Prompt",
            description = "Conversational multi-turn playground with system instructions",
            url = "https://aistudio.google.com/prompts/new_chat",
            icon = Icons.Default.Chat,
            accentColor = GeminiBlue
        ),
        HubShortcut(
            title = "Freeform Prompt",
            description = "Combine multimodal media, text, and code with zero constraints",
            url = "https://aistudio.google.com/prompts/new_freeform",
            icon = Icons.Default.Description,
            accentColor = GeminiCyan
        ),
        HubShortcut(
            title = "Structured Prompt",
            description = "Define few-shot examples, input-output tables, and test cases",
            url = "https://aistudio.google.com/prompts/new_structured",
            icon = Icons.Default.DataObject,
            accentColor = GeminiPurple
        ),
        HubShortcut(
            title = "Get API Key",
            description = "Create and manage Gemini API keys for your applications",
            url = "https://aistudio.google.com/app/apikey",
            icon = Icons.Default.Key,
            accentColor = Color(0xFFFFB300)
        ),
        HubShortcut(
            title = "My Prompt Library",
            description = "Access and open your saved prompts and projects in AI Studio",
            url = "https://aistudio.google.com/library",
            icon = Icons.Default.FolderSpecial,
            accentColor = Color(0xFF00E676)
        ),
        HubShortcut(
            title = "Model Tuning",
            description = "Fine-tune Gemini models with custom task datasets",
            url = "https://aistudio.google.com/tuning",
            icon = Icons.Default.Tune,
            accentColor = Color(0xFFFF4081)
        ),
        HubShortcut(
            title = "Gemini Developer Docs",
            description = "API reference, quickstarts, and client SDK documentation",
            url = "https://ai.google.dev/gemini-api/docs",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            accentColor = StudioPrimary
        ),
        HubShortcut(
            title = "Google AI Cookbook",
            description = "Interactive recipes and sample notebooks on GitHub",
            url = "https://github.com/google-gemini/cookbook",
            icon = Icons.Default.Code,
            accentColor = Color(0xFF64B5F6)
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hub_hero_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(GeminiGradientStart, GeminiGradientEnd)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Google AI Studio",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Fastest way to prototype and build with Google's most capable Gemini multimodal AI models.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.navigateToUrl("https://aistudio.google.com") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = GeminiGradientStart
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("launch_ai_studio_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Launch AI Studio Web",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Quick Actions
        item {
            Text(
                text = "Direct Shortcuts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Shortcut Grid / Cards
        items(shortcuts.size) { index ->
            val item = shortcuts[index]
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.navigateToUrl(item.url) }
                    .testTag("shortcut_item_$index")
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
                            .clip(CircleShape)
                            .background(item.accentColor.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = item.accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Tips & Guidance Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Mobile Experience Tip",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Google AI Studio is optimized for desktop browsers. Tap the 'Desktop' chip on the top bar anytime to switch to full desktop layout with pan & pinch zoom!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
