package com.example.data.model

data class PromptItem(
    val id: Long = 0,
    val title: String,
    val promptContent: String,
    val systemInstruction: String = "",
    val category: String = "General",
    val modelRecommendation: String = "Gemini 2.5 Flash",
    val temperature: Float = 1.0f,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
