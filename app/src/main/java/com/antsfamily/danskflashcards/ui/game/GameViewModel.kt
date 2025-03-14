package com.antsfamily.danskflashcards.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.util.COUNTDOWN_STEP
import com.antsfamily.danskflashcards.core.util.COUNTDOWN_TIME_SEC
import com.antsfamily.danskflashcards.core.util.GUESSED_ADDITIONAL_TIME
import com.antsfamily.danskflashcards.core.util.HOME_SCREEN_PAIRS_AMOUNT
import com.antsfamily.danskflashcards.core.util.WRONG_GUESS_ERROR_DURATION
import com.antsfamily.danskflashcards.core.util.ZERO
import com.antsfamily.danskflashcards.core.util.orZero
import com.antsfamily.danskflashcards.domain.CountdownTimerFlow
import com.antsfamily.danskflashcards.domain.GetFlashCardsUseCase
import com.antsfamily.danskflashcards.domain.SetPersonalBestUseCase
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.game.model.GameOverItem
import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.GuessingItem
import com.antsfamily.danskflashcards.ui.game.model.TimerItem
import com.antsfamily.danskflashcards.ui.game.model.WordItem
import com.antsfamily.danskflashcards.ui.game.model.mapToItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    private val getFlashDataUseCase: GetFlashCardsUseCase,
    private val setPersonalBestUseCase: SetPersonalBestUseCase,
    private val timerFlow: CountdownTimerFlow,
    @Assisted("userId") private val userId: String,
    @Assisted("username") private val username: String,
    @Assisted("score") private val score: Int,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String,
            @Assisted("username") username: String,
            @Assisted("score") score: Int,
        ): GameViewModel
    }

    private var guessingItems = emptyList<GuessingItem>()
    private var danishWords = emptyList<WordItem>()
    private var englishWords = emptyList<WordItem>()
    private var isTimerStarted: Boolean = false
    private var countdownJob: Job? = null
    private var pairsCounter: Int = 0

    private val _state = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val state: StateFlow<GameUiState>
        get() = _state.asStateFlow()

    private val _startAnimationFlow = MutableSharedFlow<Unit>()
    val startAnimationFlow = _startAnimationFlow.asSharedFlow()

    private val _gameOverFlow = MutableSharedFlow<GameOverItem>()
    val gameOverFlow = _gameOverFlow.asSharedFlow()

    init {
        getData()
    }

    fun onAnimationFinished() {
        showPackOfWords()
        startTimer()
    }

    fun onDanishWordCardClick(word: WordItem) = viewModelScope.launch {
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

    fun onEnglishWordCardClick(word: WordItem) = viewModelScope.launch {
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

    fun onGameOverDialogClose() {
        pairsCounter = 0
    }

    private fun markDanishWordSelected(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                danish = content.danish.map {
                    if (it.id == id) it.copy(isSelected = true) else it
                }
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
            danishWords = getFlashDataUseCase.invoke(LanguageType.DK).map { it.mapToItem() }
            englishWords = getFlashDataUseCase.invoke(LanguageType.EN).map { it.mapToItem() }
            guessingItems = danishWords.map { GuessingItem(it.id, false) }

        } catch (e: Exception) {
            _state.value = GameUiState.Error(e.mapToErrorType())
        }
    }

    private fun showPackOfWords(additionalTime: Long = ZERO) {
        val content = getContentOrNull()
        val packIds = getPackIds()
        val totalTime = COUNTDOWN_TIME_SEC
        val remainTime = (content?.timerItem?.remainTime ?: COUNTDOWN_TIME_SEC) + additionalTime
        val timerItem = TimerItem(
            remainTime = remainTime,
            progress = remainTime.toFloat().div(totalTime)
        )

        _state.value = GameUiState.Content(
            danish = getPackOfDanishWords(packIds),
            english = getPackOfEnglishWords(packIds),
            timerItem = timerItem,
            status = content?.status ?: GameStatus.STARTED
        )
    }

    private fun getPackIds(): List<Int> {
        val pack = guessingItems.filter { !it.isGuessed }.shuffled().take(HOME_SCREEN_PAIRS_AMOUNT)
        return pack.map { it.id }
    }

    private fun getPackOfDanishWords(ids: List<Int>): List<WordItem> {
        return danishWords.filter { it.id in ids }.shuffled()
    }

    private fun getPackOfEnglishWords(ids: List<Int>): List<WordItem> {
        return englishWords.filter { it.id in ids }.shuffled()
    }

    private fun startTimer() = viewModelScope.launch {
        isTimerStarted = true
        countdownJob = launchTimerFlow()
    }

    private fun launchTimerFlow(): Job = viewModelScope.launch {
        timerFlow.run(COUNTDOWN_STEP)
            .cancellable()
            .collect {
                getContentOrNull()?.let { content ->
                    val remainTime = content.timerItem.remainTime - 1
                    _state.value = content.copy(
                        timerItem = content.timerItem.copy(
                            remainTime = remainTime,
                            progress = remainTime.toFloat().div(COUNTDOWN_TIME_SEC)
                        ),
                    )
                }
                checkRemainingTime()
            }
    }

    private fun checkRemainingTime() = viewModelScope.launch {
        if (getContentOrNull()?.timerItem?.remainTime.orZero() <= ZERO) {
            stopTimer()
            resetScreenContent()
            invalidateGuessingItems()
            if (pairsCounter > score) {
                saveBestResult(pairsCounter)
            }
            showFinalDialog(score, pairsCounter)
        }
    }

    private fun showFinalDialog(oldRecord: Int, newRecord: Int) = viewModelScope.launch {
        val gameOver = GameOverItem(
            bestResult = if (oldRecord > newRecord) oldRecord else newRecord,
            newResult = newRecord,
        )
        _gameOverFlow.emit(gameOver)
    }

    private fun stopTimer() {
        countdownJob?.cancel()
        isTimerStarted = false
    }

    private fun getContentOrNull() = (_state.value as? GameUiState.Content)

    private fun getEnglishSelectedWord(): WordItem? =
        getContentOrNull()?.english?.firstOrNull { it.isSelected }

    private fun getDanishSelectedWord(): WordItem? =
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
            delay(WRONG_GUESS_ERROR_DURATION)
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
        triggerTimeUpAnimation()
        showPackOfWords(GUESSED_ADDITIONAL_TIME)
    }

    private fun isAllWordsGuessed(): Boolean {
        return guessingItems.all { it.isGuessed }
    }

    private fun invalidateAllWords() {
        guessingItems = guessingItems.map { it.copy(isGuessed = false) }
        triggerTimeUpAnimation()
        showPackOfWords(GUESSED_ADDITIONAL_TIME)
    }

    private fun triggerTimeUpAnimation() = viewModelScope.launch {
        _startAnimationFlow.emit(Unit)
    }

    private fun invalidateGuessingItems() {
        guessingItems = guessingItems.map { if (it.isGuessed) it.copy(isGuessed = false) else it }
    }

    private fun resetScreenContent() {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                timerItem = TimerItem(),
                status = GameStatus.FINISHED,
                danish = content.danish.map {
                    it.copy(isSelected = false, isGuessed = false, isWrong = false)
                },
                english = content.english.map {
                    it.copy(isSelected = false, isGuessed = false, isWrong = false)
                }
            )
        }
    }

    private fun saveBestResult(result: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            setPersonalBestUseCase(id = userId, name = username, score = result)
        } catch (e: Exception) {
            Log.e(this@GameViewModel::class.simpleName, e.message ?: e.toString())
        }
    }
}
