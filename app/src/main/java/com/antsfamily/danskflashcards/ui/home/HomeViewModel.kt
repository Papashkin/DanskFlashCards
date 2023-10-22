package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.Word
import com.antsfamily.danskflashcards.data.Word.Companion.mapToModel
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.domain.FetchDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
) : ViewModel() {

    private var words = emptyList<Word?>()

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    init {
        getData()
    }

    fun onDanishWordCardClick(word: WordModel) = viewModelScope.launch {
        if (word.isGuessed) return@launch

        if (!word.isSelected) {
            invalidateDanishWordSelection(word.id)
            val englishWord = getEnglishSelectedWord()
            when {
                (englishWord == null) -> {
                    markDanishWordSelected(word.id)
                }
            }
        }
    }

    fun onEnglishWordCardClick(word: WordModel) = viewModelScope.launch {
        if (word.isGuessed) return@launch

        if (!word.isSelected) {
            invalidateEnglishWordSelection(word.id)
            val danishWord = getDanishSelectedWord()
            when {
                (danishWord == null) -> {
                    markEnglishWordSelected(word.id)
                }
            }
        }
    }

    private fun markDanishWordSelected(id: Int) {
        getContentOrNull()?.let { content ->
            _state.update {
                content.copy(
                    danish = content.danish.map {
                        if (it.id == id) it.copy(isSelected = true) else it
                    }
                )
            }
        }
    }

    private fun markEnglishWordSelected(id: Int) {
        getContentOrNull()?.let { content ->
            _state.update {
                content.copy(
                    english = content.english.map {
                        if (it.id == id) it.copy(isSelected = true) else it
                    }
                )
            }
        }
    }

    private fun getData() = viewModelScope.launch {
        try {
            words = fetchDataUseCase.run(Unit)

            val danishWords = words.mapNotNull { it.mapToModel(true) }
            val englishWords = words.mapNotNull { it.mapToModel(false) }

            _state.update {
                HomeUiState.Content(
                    danish = danishWords.takeWhile { !it.isGuessed }.take(5),
                    english = englishWords.takeWhile { !it.isGuessed }.take(5),
                )
            }
        } catch (e: Exception) {
            _state.update { HomeUiState.Error(errorMessage = e.message.orEmpty()) }
        }
    }

    private fun getContentOrNull() = (_state.value as? HomeUiState.Content)

    private fun getEnglishSelectedWord(): WordModel? =
        getContentOrNull()?.english?.firstOrNull { it.isSelected }

    private fun getDanishSelectedWord(): WordModel? =
        getContentOrNull()?.danish?.firstOrNull { it.isSelected }

    private fun invalidateDanishWordSelection(id: Int) {
        getContentOrNull()?.let { content ->
            _state.update {
                content.copy(
                    danish = content.danish.map { it.copy(isSelected = it.id == id) }
                )
            }
        }
    }

    private fun invalidateEnglishWordSelection(id: Int) {
        getContentOrNull()?.let { content ->
            _state.update {
                content.copy(
                    english = content.english.map { it.copy(isSelected = it.id == id) }
                )
            }
        }
    }

    private fun unselectAllWords() {
        getContentOrNull()?.let { content ->
            _state.update {
                content.copy(
                    danish = content.danish.map { it.copy(isSelected = false) },
                    english = content.english.map { it.copy(isSelected = false) }
                )
            }
        }
    }
}