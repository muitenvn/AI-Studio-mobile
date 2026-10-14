package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    // Tab 0: AI Studio, Tab 1: GitHub
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Desktop view toggle
    private val _isDesktopMode = MutableStateFlow(false)
    val isDesktopMode: StateFlow<Boolean> = _isDesktopMode.asStateFlow()

    // Feedback message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // --- Tab 0: Google AI Studio ---
    val aiStudioUrl = "https://aistudio.google.com/apps/new"
    private val _aiStudioTitle = MutableStateFlow("Google AI Studio")
    val aiStudioTitle: StateFlow<String> = _aiStudioTitle.asStateFlow()

    private val _aiStudioProgress = MutableStateFlow(0)
    val aiStudioProgress: StateFlow<Int> = _aiStudioProgress.asStateFlow()

    private val _aiStudioLoading = MutableStateFlow(false)
    val aiStudioLoading: StateFlow<Boolean> = _aiStudioLoading.asStateFlow()

    private val _aiStudioCanBack = MutableStateFlow(false)
    val aiStudioCanBack: StateFlow<Boolean> = _aiStudioCanBack.asStateFlow()

    // --- Tab 1: GitHub ---
    val githubUrl = "https://github.com"
    private val _githubTitle = MutableStateFlow("GitHub")
    val githubTitle: StateFlow<String> = _githubTitle.asStateFlow()

    private val _githubProgress = MutableStateFlow(0)
    val githubProgress: StateFlow<Int> = _githubProgress.asStateFlow()

    private val _githubLoading = MutableStateFlow(false)
    val githubLoading: StateFlow<Boolean> = _githubLoading.asStateFlow()

    private val _githubCanBack = MutableStateFlow(false)
    val githubCanBack: StateFlow<Boolean> = _githubCanBack.asStateFlow()

    fun setTab(index: Int) {
        _currentTab.value = index
    }

    fun toggleDesktopMode() {
        _isDesktopMode.value = !_isDesktopMode.value
        showToast(if (_isDesktopMode.value) "Desktop view enabled" else "Mobile view enabled")
    }

    fun updateAIStudio(title: String?, progress: Int, canBack: Boolean) {
        if (!title.isNullOrBlank()) _aiStudioTitle.value = title
        _aiStudioProgress.value = progress
        _aiStudioLoading.value = progress in 1..99
        _aiStudioCanBack.value = canBack
    }

    fun updateGitHub(title: String?, progress: Int, canBack: Boolean) {
        if (!title.isNullOrBlank()) _githubTitle.value = title
        _githubProgress.value = progress
        _githubLoading.value = progress in 1..99
        _githubCanBack.value = canBack
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
