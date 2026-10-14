package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GeminiBlue
import com.example.ui.theme.GeminiCyan
import com.example.ui.theme.GeminiPurple
import com.example.ui.theme.StudioPrimary
import com.example.viewmodel.StudioViewModel

data class ModelSpec(
    val name: String,
    val contextWindow: String,
    val strengths: String,
    val bestFor: String,
    val badgeColor: Color
)

@Composable
fun ModelGuideScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    var sampleText by remember { mutableStateOf("") }
    val characterCount by remember { derivedStateOf { sampleText.length } }
    val wordCount by remember {
        derivedStateOf {
            if (sampleText.isBlank()) 0 else sampleText.trim().split("\\s+".toRegex()).size
        }
    }
    // Approximation: 1 token ≈ 4 characters in English or ~0.75 words
    val estimatedTokens by remember {
        derivedStateOf {
            if (characterCount == 0) 0 else (characterCount / 4.0).toInt().coerceAtLeast(1)
        }
    }

    val models = listOf(
        ModelSpec(
            name = "Gemini 2.5 Flash",
            contextWindow = "1,000,000 Tokens",
            strengths = "Ultra low latency, next-gen coding precision, multimodal vision & audio reasoning",
            bestFor = "Real-time apps, coding copilots, high-volume classification, interactive agents",
            badgeColor = GeminiCyan
        ),
        ModelSpec(
            name = "Gemini 1.5 Pro",
            contextWindow = "2,000,000 Tokens",
            strengths = "Massive 2M context window, deep cross-document synthesis, complex mathematical reasoning",
            bestFor = "Auditing massive codebases, book-length document Q&A, hour-long video analysis",
            badgeColor = GeminiBlue
        ),
        ModelSpec(
            name = "Gemini 1.5 Flash",
            contextWindow = "1,000,000 Tokens",
            strengths = "Cost-optimized, rapid throughput, solid multimodal capabilities",
            bestFor = "Chatbots, summarization pipelines, structured data extraction",
            badgeColor = GeminiPurple
        ),
        ModelSpec(
            name = "Imagen 3",
            contextWindow = "Text-to-Image",
            strengths = "Photorealistic rendering, fine detail textures, exceptional typography in images",
            bestFor = "Marketing assets, UI concept design, creative illustrations",
            badgeColor = Color(0xFFFFB300)
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title
        item {
            Column {
                Text(
                    text = "Gemini Models & Parameters",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Specifications, token calculations, and tuning parameters for AI Studio",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Token Estimator Tool
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("token_estimator_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = StudioPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Token Estimator",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = sampleText,
                        onValueChange = { sampleText = it },
                        placeholder = { Text("Paste prompt text here to calculate estimated tokens...") },
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CounterStat(label = "Estimated Tokens", value = "$estimatedTokens", highlight = true)
                        CounterStat(label = "Words", value = "$wordCount")
                        CounterStat(label = "Characters", value = "$characterCount")
                    }
                }
            }
        }

        // Models Reference Section
        item {
            Text(
                text = "Model Specifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(models.size) { index ->
            val model = models[index]
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(model.badgeColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = model.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = model.badgeColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = model.contextWindow,
                                style = MaterialTheme.typography.labelSmall,
                                color = model.badgeColor,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Strengths:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = model.strengths,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Recommended for:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = model.bestFor,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Parameter Reference Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = GeminiCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Studio Parameter Cheat-Sheet",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ParameterExplainer(
                        name = "Temperature (0.0 - 2.0)",
                        desc = "Controls randomness. Use 0.0 - 0.3 for deterministic coding and JSON; 0.7 - 1.0 for general assistant; 1.2+ for creative writing."
                    )
                    ParameterExplainer(
                        name = "Top-P (Nucleus Sampling)",
                        desc = "Selects among the smallest set of tokens whose cumulative probability exceeds P. 0.95 is standard."
                    )
                    ParameterExplainer(
                        name = "Top-K",
                        desc = "Limits the model to choose from the top K most likely tokens. Lower values reduce hallucinations."
                    )
                    ParameterExplainer(
                        name = "System Instructions",
                        desc = "Gives persistent context, role instructions, and formatting constraints that remain active across all user turns."
                    )
                }
            }
        }

        // Launch in AI Studio link
        item {
            Button(
                onClick = { viewModel.navigateToUrl("https://aistudio.google.com") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open Google AI Studio")
            }
        }
    }
}

@Composable
private fun CounterStat(label: String, value: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (highlight) StudioPrimary else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ParameterExplainer(name: String, desc: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
