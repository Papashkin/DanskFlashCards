package com.antsfamily.danskflashcards.ui.onboarding2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.domain.usecase.GetLearningLanguageUseCase
import com.antsfamily.danskflashcards.domain.usecase.SetLanguageUseCase
import com.antsfamily.danskflashcards.domain.usecase.SetOnboardingIsPassedUseCase
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
class Onboarding2ViewModel @Inject constructor(
    private val getLearningLanguageUseCase: GetLearningLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setOnboardingIsPassedUseCase: SetOnboardingIsPassedUseCase,
) : ViewModel() {

    private val _navigationToHomeFlow = MutableSharedFlow<Unit>()
    val navigationToHomeFlow: SharedFlow<Unit> = _navigationToHomeFlow.asSharedFlow()

    private val _state = MutableStateFlow<Onboarding2UiState>(Onboarding2UiState.Loading)
    val state: StateFlow<Onboarding2UiState> = _state.asStateFlow()

    private var selectedLanguageItem: LanguageItem? = null

    init {
        getLanguages()
    }

    fun onLanguageSelected(item: LanguageItem) = viewModelScope.launch {
        selectedLanguageItem = item
        (_state.value as? Onboarding2UiState.Content)?.let { content ->
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
            (_state.value as? Onboarding2UiState.Content)?.let { content ->
                _state.value = content.copy(isButtonLoadingVisible = true)
            }
            setLanguageUseCase(it.languageType, true)
            setOnboardingIsPassedUseCase()
            _navigationToHomeFlow.emit(Unit)
        }
    }

    private fun getLanguages() = viewModelScope.launch {
        val learningLanguage = getLearningLanguageUseCase()
        val languages = LanguageType.entries
            .filter { it != learningLanguage }
            .map { LanguageItem(it, false) }
        _state.value = Onboarding2UiState.Content(
            languages = languages,
            isButtonAvailable = false,
            isButtonLoadingVisible = false
        )
    }
}