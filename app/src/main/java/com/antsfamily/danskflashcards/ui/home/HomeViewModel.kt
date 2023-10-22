package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.Word
import com.antsfamily.danskflashcards.data.Word.Companion.mapToModel
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.domain.FetchDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            getEnglishSelectedWord()?.let { englishWord ->
                if (englishWord.id == word.id) {
                    markWordsAsGuessed(word.id)
                } else {
                    markWordsAsWrong(danishWordId = word.id, englishWordId = englishWord.id, duration = 200)
                }
            } ?: run {
                markDanishWordSelected(word.id)
            }
        }
    }

    fun onEnglishWordCardClick(word: WordModel) = viewModelScope.launch {
        if (word.isGuessed) return@launch

        if (!word.isSelected) {
            invalidateEnglishWordSelection(word.id)
            getDanishSelectedWord()?.let { danishWord ->
                if (danishWord.id == word.id) {
                    markWordsAsGuessed(word.id)
                } else {
                    markWordsAsWrong(danishWordId = danishWord.id, englishWordId = word.id, duration = 200)
                }
            } ?: run {
                markEnglishWordSelected(word.id)
            }
        }
    }

    private fun markDanishWordSelected(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                danish = content.danish.map { if (it.id == id) it.copy(isSelected = true) else it }
            )
        }
    }

    private fun markEnglishWordSelected(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                english = content.english.map {
                    if (it.id == id) it.copy(isSelected = true) else it
                }
            )
        }
    }

    private fun getData() = viewModelScope.launch {
        try {
            words = fetchDataUseCase.run(Unit)

            val danishWords = words.mapNotNull { it.mapToModel(true) }
            val englishWords = words.mapNotNull { it.mapToModel(false) }

            _state.value = HomeUiState.Content(
                danish = danishWords.takeWhile { !it.isGuessed }.take(5),
                english = englishWords.takeWhile { !it.isGuessed }.take(5),
            )
        } catch (e: Exception) {
            _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
        }
    }

    private fun getContentOrNull() = (_state.value as? HomeUiState.Content)

    private fun getEnglishSelectedWord(): WordModel? =
        getContentOrNull()?.english?.firstOrNull { it.isSelected }

    private fun getDanishSelectedWord(): WordModel? =
        getContentOrNull()?.danish?.firstOrNull { it.isSelected }

    private fun invalidateDanishWordSelection(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                danish = content.danish.map { it.copy(isSelected = it.id == id) }
            )
        }
    }

    private fun invalidateEnglishWordSelection(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                english = content.english.map { it.copy(isSelected = it.id == id) }
            )
        }
    }

    private fun markWordsAsGuessed(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                danish = content.danish.map {
                    if (it.id == id) it.copy(isSelected = false, isGuessed = true) else it
                },
                english = content.english.map {
                    if (it.id == id) it.copy(isSelected = false, isGuessed = true) else it
                }
            )
        }
    }

    private suspend fun markWordsAsWrong(danishWordId: Int, englishWordId: Int, duration: Long) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                danish = content.danish.map {
                    if (it.id == danishWordId) it.copy(isSelected = false, isWrong = true) else it
                },
                english = content.english.map {
                    if (it.id == englishWordId) it.copy(isSelected = false, isWrong = true) else it
                }
            )
            delay(duration)
            invalidateWrongWords(danishWordId, englishWordId)
        }
    }

    private fun invalidateWrongWords(danishWordId: Int, englishWordId: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                danish = content.danish.map {
                    if (it.id == danishWordId) it.copy(isSelected = false, isWrong = false) else it
                },
                english = content.english.map {
                    if (it.id == englishWordId) it.copy(isSelected = false, isWrong = false) else it
                }
            )
        }
    }
}
