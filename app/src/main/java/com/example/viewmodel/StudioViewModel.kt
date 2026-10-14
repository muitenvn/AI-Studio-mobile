package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    // Default to Google AI Studio New App
    private val _currentUrl = MutableStateFlow("https://aistudio.google.com/apps/new")
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()

    private val _webTitle = MutableStateFlow("AI Studio - New App")
    val webTitle: StateFlow<String> = _webTitle.asStateFlow()

    private val _webProgress = MutableStateFlow(0)
    val webProgress: StateFlow<Int> = _webProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Desktop view toggle
    private val _isDesktopMode = MutableStateFlow(false)
    val isDesktopMode: StateFlow<Boolean> = _isDesktopMode.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val _canGoForward = MutableStateFlow(false)
    val canGoForward: StateFlow<Boolean> = _canGoForward.asStateFlow()

    private val _loadError = MutableStateFlow<String?>(null)
    val loadError: StateFlow<String?> = _loadError.asStateFlow()

    // Toast / Feedback message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Liquid Gallant Hub Sheet state
    private val _isHubSheetOpen = MutableStateFlow(false)
    val isHubSheetOpen: StateFlow<Boolean> = _isHubSheetOpen.asStateFlow()

    fun navigateToUrl(url: String) {
        _loadError.value = null
        _currentUrl.value = url
        _isHubSheetOpen.value = false
    }

    fun openNewApp() {
        navigateToUrl("https://aistudio.google.com/apps/new")
    }

    fun openGitHub() {
        navigateToUrl("https://github.com")
    }

    fun openGitHubNew() {
        navigateToUrl("https://github.com/new")
    }

    fun openStudioHome() {
        navigateToUrl("https://aistudio.google.com")
    }

    fun openApiKey() {
        navigateToUrl("https://aistudio.google.com/app/apikey")
    }

    fun openChatPrompt() {
        navigateToUrl("https://aistudio.google.com/prompts/new_chat")
    }

    fun openFreeformPrompt() {
        navigateToUrl("https://aistudio.google.com/prompts/new_freeform")
    }

    fun toggleDesktopMode() {
        _isDesktopMode.value = !_isDesktopMode.value
        showToast(if (_isDesktopMode.value) "Desktop Mode Enabled" else "Mobile Mode Enabled")
    }

    fun toggleHubSheet() {
        _isHubSheetOpen.value = !_isHubSheetOpen.value
    }

    fun setHubSheetOpen(isOpen: Boolean) {
        _isHubSheetOpen.value = isOpen
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
}
