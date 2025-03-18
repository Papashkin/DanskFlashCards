package com.antsfamily.danskflashcards.ui.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.data.SignInResult
import com.antsfamily.danskflashcards.data.model.SignInType
import com.antsfamily.danskflashcards.domain.GetAppVersionUseCase
import com.antsfamily.danskflashcards.domain.SignInWithCredentialsUseCase
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
class AboutViewModel @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AboutUiState>(AboutUiState.Content(null))
    val state: StateFlow<AboutUiState>
        get() = _state.asStateFlow()

    init {
        getAppVersion()
    }

    private fun getAppVersion() = viewModelScope.launch {
        val version = getAppVersionUseCase()
        version?.let {
            _state.value = AboutUiState.Content(it)
        }
    }


}
