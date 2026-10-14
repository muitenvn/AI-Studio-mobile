package com.example.data.repository

import com.example.data.db.PromptDao
import com.example.data.db.PromptEntity
import com.example.data.model.PromptItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PromptRepository(private val promptDao: PromptDao) {

    val allPrompts: Flow<List<PromptItem>> = promptDao.getAllPrompts().map { entities ->
        entities.map { it.toModel() }
    }

    val favoritePrompts: Flow<List<PromptItem>> = promptDao.getFavoritePrompts().map { entities ->
        entities.map { it.toModel() }
    }

    suspend fun initializeDefaultsIfEmpty() {
        if (promptDao.countPrompts() == 0) {
            val defaults = getDefaultTemplates()
            promptDao.insertPrompts(defaults.map { PromptEntity.fromModel(it) })
        }
    }

    suspend fun savePrompt(prompt: PromptItem): Long {
        return promptDao.insertPrompt(PromptEntity.fromModel(prompt))
    }

    suspend fun updatePrompt(prompt: PromptItem) {
        promptDao.updatePrompt(PromptEntity.fromModel(prompt))
    }

    suspend fun deletePrompt(id: Long) {
        promptDao.deletePromptById(id)
    }

    suspend fun toggleFavorite(prompt: PromptItem) {
        val updated = prompt.copy(isFavorite = !prompt.isFavorite)
        promptDao.updatePrompt(PromptEntity.fromModel(updated))
    }

    companion object {
        fun getDefaultTemplates(): List<PromptItem> = listOf(
            PromptItem(
                title = "Android Compose & Kotlin Specialist",
                systemInstruction = "You are a Google Developer Expert in Android development, Kotlin, and Jetpack Compose with Material 3. Prioritize clean architecture, responsive layouts, accessibility, and high-performance state handling.",
                promptContent = "Review the following Jetpack Compose code for performance optimizations, unnecessary recompositions, and edge-to-edge window insets compatibility:\n\n```kotlin\n// Paste code here\n```",
                category = "Coding",
                modelRecommendation = "Gemini 2.5 Flash",
                temperature = 0.4f,
                isFavorite = true
            ),
            PromptItem(
                title = "Strict JSON Schema Generator",
                systemInstruction = "You are a JSON-only response engine. Output exclusively valid JSON according to the schema provided. Never wrap output in markdown code blocks unless requested. Do not include introductory or explanatory conversational text.",
                promptContent = "Convert the following raw text into a normalized JSON array with the fields: id, title, summary, tags, and sentiment (positive/neutral/negative):\n\n[Insert text here]",
                category = "Structured",
                modelRecommendation = "Gemini 2.5 Flash",
                temperature = 0.2f,
                isFavorite = true
            ),
            PromptItem(
                title = "Multimodal Vision & UI Analyst",
                systemInstruction = "You are a computer vision specialist for UI/UX inspection and chart analysis. Identify component hierarchy, typography, colors, accessibility contrast issues, and layout anomalies.",
                promptContent = "Analyze this attached screenshot. Break down:\n1. Key UI hierarchy and M3 components used\n2. Color palette (approximated hex values)\n3. Accessibility issues (touch target sizes, text contrast)\n4. Recommendations for tablet and landscape responsiveness.",
                category = "Multimodal",
                modelRecommendation = "Gemini 1.5 Pro",
                temperature = 0.5f,
                isFavorite = true
            ),
            PromptItem(
                title = "Code Security & Vulnerability Auditor",
                systemInstruction = "You are an elite application security auditor. Analyze code for OWASP Top 10 vulnerabilities, injection attacks, hardcoded secrets, memory leaks, and unsafe concurrency.",
                promptContent = "Perform a thorough security and vulnerability audit on this implementation snippet. Detail severity levels (Critical, High, Medium, Low) and provide secure refactored code for each issue identified:\n\n[Paste code here]",
                category = "Coding",
                modelRecommendation = "Gemini 1.5 Pro",
                temperature = 0.3f,
                isFavorite = false
            ),
            PromptItem(
                title = "Gemini API Function Calling Declarations",
                systemInstruction = "You are an expert on Gemini API tool calling. You generate precise OpenAPI-compliant function declarations with description and parameter definitions for Gemini SDKs.",
                promptContent = "Write Python and Kotlin function declarations for the Gemini API representing a weather forecast tool and an email notification tool with required and optional parameters.",
                category = "API & Tools",
                modelRecommendation = "Gemini 2.5 Flash",
                temperature = 0.3f,
                isFavorite = false
            ),
            PromptItem(
                title = "Technical Documentation & Markdown Refiner",
                systemInstruction = "You are a principal technical writer. You convert messy developer notes and code comments into crisp, elegant GitHub README and API documentation.",
                promptContent = "Transform these developer notes into a polished Markdown documentation guide with badges, prerequisites, quick-start code blocks, and an API reference table:\n\n[Paste notes here]",
                category = "Writing",
                modelRecommendation = "Gemini 2.5 Flash",
                temperature = 0.7f,
                isFavorite = false
            )
        )
    }
}
