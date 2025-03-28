package com.antsfamily.danskflashcards.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.domain.SetOnboardingIsPassedUseCase
import com.antsfamily.danskflashcards.domain.SetSelectedLanguageUseCase
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setOnboardingIsPassedUseCase: SetOnboardingIsPassedUseCase,
    private val setSelectedLanguageUseCase: SetSelectedLanguageUseCase,
) : ViewModel() {

    private val _navigationToHomeFlow = MutableSharedFlow<Unit>()
    val navigationToHomeFlow: SharedFlow<Unit> = _navigationToHomeFlow.asSharedFlow()

    private val _state = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Loading)
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    private var selectedLanguageItem: LanguageItem? = null

    init {
        getLanguages()
    }

    fun onLanguageSelected(item: LanguageItem) = viewModelScope.launch {
        selectedLanguageItem = item
        (_state.value as? OnboardingUiState.Content)?.let { content ->
            _state.value = content.copy(
                languages = content.languages.map {
                    it.copy(isSelected = it.languageType == item.languageType)
                },
                isButtonAvailable = true
            )
        }
    }

    fun onContinueClick() = viewModelScope.launch {
        selectedLanguageItem?.let {
            (_state.value as? OnboardingUiState.Content)?.let { content ->
                _state.value = content.copy(isButtonLoadingVisible = true)
            }
            setSelectedLanguageUseCase(it.languageType)
            setOnboardingIsPassedUseCase()
            _navigationToHomeFlow.emit(Unit)
        }
    }

    private fun getLanguages() {
        val languages = LanguageType.entries
            .filter { !it.isEnglish() }
            .map { LanguageItem(it, false) }
        _state.value = OnboardingUiState.Content(
            languages = languages,
            isButtonAvailable = false,
            isButtonLoadingVisible = false
        )
    }
}