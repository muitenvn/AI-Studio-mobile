package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.PromptItem
import com.example.data.repository.PromptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    STUDIO_WEB,
    HUB,
    PROMPTS,
    MODELS
}

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PromptRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PromptRepository(database.promptDao())
        viewModelScope.launch {
            repository.initializeDefaultsIfEmpty()
        }
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow(ScreenTab.STUDIO_WEB)
    val currentScreen: StateFlow<ScreenTab> = _currentScreen.asStateFlow()

    // Web State
    private val _currentUrl = MutableStateFlow("https://aistudio.google.com")
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()

    private val _webTitle = MutableStateFlow("Google AI Studio")
    val webTitle: StateFlow<String> = _webTitle.asStateFlow()

    private val _webProgress = MutableStateFlow(0)
    val webProgress: StateFlow<Int> = _webProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Desktop view toggle - AI Studio is designed for desktop, so desktop user-agent is available
    private val _isDesktopMode = MutableStateFlow(false)
    val isDesktopMode: StateFlow<Boolean> = _isDesktopMode.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val _canGoForward = MutableStateFlow(false)
    val canGoForward: StateFlow<Boolean> = _canGoForward.asStateFlow()

    private val _loadError = MutableStateFlow<String?>(null)
    val loadError: StateFlow<String?> = _loadError.asStateFlow()

    // Snackbar / Feedback message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Prompts State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _showCreatePromptDialog = MutableStateFlow(false)
    val showCreatePromptDialog: StateFlow<Boolean> = _showCreatePromptDialog.asStateFlow()

    private val _editingPrompt = MutableStateFlow<PromptItem?>(null)
    val editingPrompt: StateFlow<PromptItem?> = _editingPrompt.asStateFlow()

    // Filtered prompts
    val filteredPrompts: StateFlow<List<PromptItem>> = combine(
        repository.allPrompts,
        _searchQuery,
        _selectedCategory
    ) { prompts, query, category ->
        prompts.filter { item ->
            val matchesCategory = when (category) {
                "All" -> true
                "Favorites" -> item.isFavorite
                else -> item.category.equals(category, ignoreCase = true)
            }
            val matchesQuery = query.isBlank() ||
                item.title.contains(query, ignoreCase = true) ||
                item.promptContent.contains(query, ignoreCase = true) ||
                item.systemInstruction.contains(query, ignoreCase = true)

            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Actions
    fun setScreen(screen: ScreenTab) {
        _currentScreen.value = screen
    }

    fun navigateToUrl(url: String) {
        _loadError.value = null
        _currentUrl.value = url
        _currentScreen.value = ScreenTab.STUDIO_WEB
    }

    fun toggleDesktopMode() {
        _isDesktopMode.value = !_isDesktopMode.value
    }

    fun updateWebProgress(progress: Int) {
        _webProgress.value = progress
        _isLoading.value = progress in 1..99
    }

    fun updateWebTitle(title: String) {
        if (title.isNotBlank()) {
            _webTitle.value = title
        }
    }

    fun updateWebHistory(canBack: Boolean, canForward: Boolean) {
        _canGoBack.value = canBack
        _canGoForward.value = canForward
    }

    fun setLoadError(error: String?) {
        _loadError.value = error
    }

    fun clearLoadError() {
        _loadError.value = null
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Prompts Actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun openCreatePromptDialog() {
        _editingPrompt.value = null
        _showCreatePromptDialog.value = true
    }

    fun openEditPromptDialog(prompt: PromptItem) {
        _editingPrompt.value = prompt
        _showCreatePromptDialog.value = true
    }

    fun closePromptDialog() {
        _showCreatePromptDialog.value = false
        _editingPrompt.value = null
    }

    fun savePrompt(title: String, content: String, systemInstruction: String, category: String, model: String, temp: Float) {
        viewModelScope.launch {
            val current = _editingPrompt.value
            if (current != null) {
                val updated = current.copy(
                    title = title,
                    promptContent = content,
                    systemInstruction = systemInstruction,
                    category = category,
                    modelRecommendation = model,
                    temperature = temp
                )
                repository.updatePrompt(updated)
                showToast("Prompt updated")
            } else {
                val newPrompt = PromptItem(
                    title = title,
                    promptContent = content,
                    systemInstruction = systemInstruction,
                    category = category,
                    modelRecommendation = model,
                    temperature = temp
                )
                repository.savePrompt(newPrompt)
                showToast("New prompt saved to library")
            }
            closePromptDialog()
        }
    }

    fun deletePrompt(id: Long) {
        viewModelScope.launch {
            repository.deletePrompt(id)
            showToast("Prompt deleted")
        }
    }

    fun toggleFavorite(prompt: PromptItem) {
        viewModelScope.launch {
            repository.toggleFavorite(prompt)
        }
    }
}
