package com.antsfamily.danskflashcards.ui.onboarding

import androidx.lifecycle.ViewModel
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.ui.auth.AuthUiState
import com.antsfamily.danskflashcards.ui.home.HomeViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = OnboardingViewModel.Factory::class)
class OnboardingViewModel @AssistedInject constructor(
    @Assisted("user") private val user: CurrentUserItem
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("user") user: CurrentUserItem): OnboardingViewModel
    }

    private val _navigationToHomeFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToHomeFlow: SharedFlow<CurrentUserItem> = _navigationToHomeFlow.asSharedFlow()

    private val _state = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Loading)
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()
}