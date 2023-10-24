package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.GuessingItem
import com.antsfamily.danskflashcards.data.Word.Companion.mapToModel
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.domain.CountdownTimerFlow
import com.antsfamily.danskflashcards.domain.FetchDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
    private val timerFlow: CountdownTimerFlow,
) : ViewModel() {

    companion object {
        private const val DURATION = 200L
        private const val COUNTDOWN_STEP = 1000L
        private const val COUNTDOWN_TIME_SEC = 120L
    }

    private var guessingItems = emptyList<GuessingItem>()
    private var danishWords = emptyList<WordModel>()
    private var englishWords = emptyList<WordModel>()
    private var isTimerStarted: Boolean = false

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
                    markWordsAsWrong(danishWordId = word.id, englishWordId = englishWord.id)
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
                    markWordsAsWrong(danishWordId = danishWord.id, englishWordId = word.id)
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
            fetchDataUseCase(Unit) { data ->
                guessingItems = data.filterNotNull().map { GuessingItem(it.id, false) }
                danishWords = data.mapNotNull { it.mapToModel(true) }
                englishWords = data.mapNotNull { it.mapToModel(false) }
                showPackOfWords(true)
            }
        } catch (e: Exception) {
            _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
        }
    }

    private fun showPackOfWords(isInit: Boolean) {
        val additionalTime = if (isInit) 0 else 5
        val pack = guessingItems.filter { !it.isGuessed }.shuffled().take(5)
        val packIds = pack.map { it.id }
        _state.value = HomeUiState.Content(
            danish = danishWords.filter { it.id in packIds }.shuffled(),
            english = englishWords.filter { it.id in packIds }.shuffled(),
            getContentOrNull()?.totalCountdownTime ?: COUNTDOWN_TIME_SEC,
            (getContentOrNull()?.remainingCountdownTime ?: COUNTDOWN_TIME_SEC) + additionalTime,
        )

        if (isInit && !isTimerStarted) {
            startTimer()
        }
    }

    private fun startTimer() = viewModelScope.launch {
        isTimerStarted = true
        val flow = timerFlow(COUNTDOWN_STEP)
        flow
            .cancellable()
            .collect {
            getContentOrNull()?.let { content ->
                val remainingTime = content.remainingCountdownTime - 1
                _state.value = content.copy(remainingCountdownTime = remainingTime)
            }
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
        when {
            isWholePackGuessed() -> updatePackOfWords()
            isAllWordsGuessed() -> invalidateAllWords()
        }
    }

    private suspend fun markWordsAsWrong(danishWordId: Int, englishWordId: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                danish = content.danish.map {
                    if (it.id == danishWordId) it.copy(isSelected = false, isWrong = true) else it
                },
                english = content.english.map {
                    if (it.id == englishWordId) it.copy(isSelected = false, isWrong = true) else it
                }
            )
            delay(DURATION)
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

    private fun isWholePackGuessed(): Boolean {
        val isGuessed = getContentOrNull()?.let { content ->
            val isAllEnglishWordsGuessed = content.english.all { it.isGuessed }
            val isAllDanishWordsGuessed = content.danish.all { it.isGuessed }
            isAllEnglishWordsGuessed && isAllDanishWordsGuessed
        } ?: false
        return isGuessed
    }

    private fun updatePackOfWords() {
        val currentPackIds = getContentOrNull()?.danish.orEmpty().map { it.id }
        guessingItems = guessingItems.map { item ->
            if (item.id in currentPackIds) item.copy(isGuessed = true) else item
        }
        showPackOfWords(false)
    }

    private fun isAllWordsGuessed(): Boolean {
        return guessingItems.all { it.isGuessed }
    }

    private fun invalidateAllWords() {
        guessingItems = guessingItems.map { it.copy(isGuessed = false) }
        showPackOfWords(false)
    }
}
