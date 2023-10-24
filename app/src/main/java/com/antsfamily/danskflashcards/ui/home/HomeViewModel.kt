package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.DialogData
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.data.GuessingItem
import com.antsfamily.danskflashcards.data.Word.Companion.mapToModel
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.domain.CountdownTimerFlow
import com.antsfamily.danskflashcards.domain.FetchDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
    private val timerFlow: CountdownTimerFlow,
) : ViewModel() {

    companion object {
        private const val ZERO = 0L
        private const val DURATION = 200L
        private const val ADDITIONAL_TIME = 5L
        private const val COUNTDOWN_STEP = 1000L
        private const val COUNTDOWN_TIME_SEC = 120L
    }

    private var guessingItems = emptyList<GuessingItem>()
    private var danishWords = emptyList<WordModel>()
    private var englishWords = emptyList<WordModel>()
    private var isTimerStarted: Boolean = false
    private var countdownJob: Job? = null
    private var pairsCounter: Int = 0

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _dialogData = MutableStateFlow(DialogData())
    val dialogData: StateFlow<DialogData>
        get() = _dialogData.asStateFlow()

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

    fun onStartClick() {
        getContentOrNull()?.let { content ->
            if (!isTimerStarted) {
                val packIds = getPackIds()
                _state.value = content.copy(
                    status = GameStatus.STARTED,
                    danish = getPackOfDanishWords(packIds),
                    english = getPackOfEnglishWords(packIds)
                )
                startTimer()
            }
        }
    }

    fun hideDialog() {
        pairsCounter = 0
        _dialogData.value = DialogData(false, pairsCounter)
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
                showPackOfWords()
            }
        } catch (e: Exception) {
            _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
        }
    }

    private fun showPackOfWords(additionalTime: Long = 0) {
        val packIds = getPackIds()
        _state.value = HomeUiState.Content(
            danish = getPackOfDanishWords(packIds),
            english = getPackOfEnglishWords(packIds),
            getContentOrNull()?.totalCountdownTime ?: COUNTDOWN_TIME_SEC,
            (getContentOrNull()?.remainingCountdownTime ?: COUNTDOWN_TIME_SEC) + additionalTime,
            status = getContentOrNull()?.status ?: GameStatus.READY
        )
    }

    private fun getPackIds(): List<Int> {
        val pack = guessingItems.filter { !it.isGuessed }.shuffled().take(5)
        return pack.map { it.id }
    }

    private fun getPackOfDanishWords(ids: List<Int>): List<WordModel> {
        return danishWords.filter { it.id in ids }.shuffled()
    }

    private fun getPackOfEnglishWords(ids: List<Int>): List<WordModel> {
        return englishWords.filter { it.id in ids }.shuffled()
    }

    private fun startTimer() = viewModelScope.launch {
        isTimerStarted = true
        countdownJob = launchTimerFlow()
    }

    private fun launchTimerFlow(): Job = viewModelScope.launch {
        timerFlow(COUNTDOWN_STEP)
            .cancellable()
            .collect {
                getContentOrNull()?.let { content ->
                    val remainingTime = content.remainingCountdownTime - 1
                    _state.value = content.copy(remainingCountdownTime = remainingTime)
                }
                checkRemainingTime()
            }
    }

    private fun checkRemainingTime() {
        getContentOrNull()?.let { content ->
            if (content.remainingCountdownTime < ZERO) {
                countdownJob?.cancel()
                isTimerStarted = false
                _state.value = content.copy(
                    remainingCountdownTime = COUNTDOWN_TIME_SEC,
                    totalCountdownTime = COUNTDOWN_TIME_SEC,
                    status = GameStatus.FINISHED,
                    danish = content.danish.map {
                        it.copy(isSelected = false, isGuessed = false, isWrong = false)
                    },
                    english = content.english.map {
                        it.copy(isSelected = false, isGuessed = false, isWrong = false)
                    }
                )
                guessingItems = guessingItems.map { if (it.isGuessed) it.copy(isGuessed = false) else it }
                _dialogData.value = DialogData(true, pairsCounter)
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
            pairsCounter += 1
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
        showPackOfWords(ADDITIONAL_TIME)
    }

    private fun isAllWordsGuessed(): Boolean {
        return guessingItems.all { it.isGuessed }
    }

    private fun invalidateAllWords() {
        guessingItems = guessingItems.map { it.copy(isGuessed = false) }
        showPackOfWords(ADDITIONAL_TIME)
    }
}
