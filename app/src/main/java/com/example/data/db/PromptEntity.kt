package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.PromptItem

@Entity(tableName = "prompts")
data class PromptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val promptContent: String,
    val systemInstruction: String,
    val category: String,
    val modelRecommendation: String,
    val temperature: Float,
    val isFavorite: Boolean,
    val createdAt: Long
) {
    fun toModel(): PromptItem = PromptItem(
        id = id,
        title = title,
        promptContent = promptContent,
        systemInstruction = systemInstruction,
        category = category,
        modelRecommendation = modelRecommendation,
        temperature = temperature,
        isFavorite = isFavorite,
        createdAt = createdAt
    )

    companion object {
        fun fromModel(model: PromptItem): PromptEntity = PromptEntity(
            id = model.id,
            title = model.title,
            promptContent = model.promptContent,
            systemInstruction = model.systemInstruction,
            category = model.category,
            modelRecommendation = model.modelRecommendation,
            temperature = model.temperature,
            isFavorite = model.isFavorite,
            createdAt = model.createdAt
        )
    }
}
