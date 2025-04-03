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
import com.antsfamily.danskflashcards.domain.GetWordsUseCase
import com.antsfamily.danskflashcards.domain.SetPersonalBestUseCase
import com.antsfamily.danskflashcards.domain.model.WordDomain
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
    private val getWordsUseCase: GetWordsUseCase,
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
    private var learningWords = emptyList<WordItem>()
    private var primaryWords = emptyList<WordItem>()
    private var countdownJob: Job? = null
    private var pairsCounter: Int = 0

    private val _state = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val state: StateFlow<GameUiState>
        get() = _state.asStateFlow()

    private val _startAnimationFlow = MutableSharedFlow<Unit>()
    val startAnimationFlow = _startAnimationFlow.asSharedFlow()

    private val _navigateBackFlow = MutableSharedFlow<Unit>()
    val navigateBackFlow = _navigateBackFlow.asSharedFlow()

    private val _gameOverFlow = MutableSharedFlow<GameOverItem>()
    val gameOverFlow = _gameOverFlow.asSharedFlow()

    private val _closeGameOverDialogFlow = MutableSharedFlow<Unit>()
    val closeGameOverDialogFlow = _closeGameOverDialogFlow.asSharedFlow()

    init {
        getWords()
    }

    fun onAnimationFinished() {
        showPackOfWords()
        startTimer()
    }

    fun onRetryClick() {
        _state.value = GameUiState.Loading
        getWords()
    }

    fun onLearningWordClick(word: WordItem) = viewModelScope.launch {
        if (word.isGuessed) return@launch

        if (!word.isSelected) {
            invalidateLearningWordSelection(word.id)
            getPrimarySelectedWord()?.let { primaryWord ->
                if (primaryWord.id == word.id) {
                    markWordsAsGuessed(word.id)
                } else {
                    markWordsAsWrong(learningWordId = word.id, primaryWordId = primaryWord.id)
                }
            } ?: run {
                markLearningWordSelected(word.id)
            }
        }
    }

    fun onPrimaryWordClick(word: WordItem) = viewModelScope.launch {
        if (word.isGuessed) return@launch

        if (!word.isSelected) {
            invalidatePrimaryWordSelection(word.id)
            getLearningSelectedWord()?.let { learningWord ->
                if (learningWord.id == word.id) {
                    markWordsAsGuessed(word.id)
                } else {
                    markWordsAsWrong(learningWordId = learningWord.id, primaryWordId = word.id)
                }
            } ?: run {
                markPrimaryWordSelected(word.id)
            }
        }
    }

    fun onGameOverDialogClose() = viewModelScope.launch {
        pairsCounter = 0
        _navigateBackFlow.emit(Unit)
        _closeGameOverDialogFlow.emit(Unit)
    }

    fun onPlayAgainClick() = viewModelScope.launch {
        invalidateGameData()
        _closeGameOverDialogFlow.emit(Unit)
        _state.value = GameUiState.Countdown
    }

    private fun invalidateGameData() {
        pairsCounter = 0
        learningWords = learningWords.map { it.copy(
            isSelected = false,
            isGuessed = false,
            isWrong = false,
        ) }
        primaryWords = primaryWords.map { it.copy(
            isSelected = false,
            isGuessed = false,
            isWrong = false,
        ) }
        guessingItems = learningWords.map { GuessingItem(it.id, false) }
    }

    private fun markLearningWordSelected(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                learningWords = content.learningWords.map {
                    if (it.id == id) it.copy(isSelected = true) else it
                }
            )
        }
    }

    private fun markPrimaryWordSelected(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                primaryWords = content.primaryWords.map {
                    if (it.id == id) it.copy(isSelected = true) else it
                }
            )
        }
    }

    private fun getWords() = viewModelScope.launch {
        try {
            val words = getWordsUseCase()
            handleSuccessWordsResult(words)
        } catch (e: Exception) {
            _state.value = GameUiState.Error(e.mapToErrorType())
        }
    }

    private fun handleSuccessWordsResult(words: Pair<List<WordDomain>, List<WordDomain>>) {
        learningWords = words.first.map { it.mapToItem() }
        primaryWords = words.second.map { it.mapToItem() }
        guessingItems = learningWords.map { GuessingItem(it.id, false) }
        _state.value = GameUiState.Countdown
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
            learningWords = getPackOfLearningWords(packIds),
            primaryWords = getPackOfPrimaryWords(packIds),
            timerItem = timerItem,
            status = content?.status ?: GameStatus.STARTED
        )
    }

    private fun getPackIds(): List<Int> {
        val pack = guessingItems.filter { !it.isGuessed }.shuffled().take(HOME_SCREEN_PAIRS_AMOUNT)
        return pack.map { it.id }
    }

    private fun getPackOfLearningWords(ids: List<Int>): List<WordItem> {
        return learningWords.filter { it.id in ids }.shuffled()
    }

    private fun getPackOfPrimaryWords(ids: List<Int>): List<WordItem> {
        return primaryWords.filter { it.id in ids }.shuffled()
    }

    private fun startTimer() = viewModelScope.launch {
        countdownJob = launchTimerFlow()
    }

    private fun launchTimerFlow(): Job = viewModelScope.launch {
        timerFlow.run(COUNTDOWN_STEP)
            .cancellable()
            .collect {
                handleTimerChange()
            }
    }

    private fun handleTimerChange() {
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
    }

    private fun getContentOrNull() = (_state.value as? GameUiState.Content)

    private fun getPrimarySelectedWord(): WordItem? =
        getContentOrNull()?.primaryWords?.firstOrNull { it.isSelected }

    private fun getLearningSelectedWord(): WordItem? =
        getContentOrNull()?.learningWords?.firstOrNull { it.isSelected }

    private fun invalidateLearningWordSelection(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                learningWords = content.learningWords.map { it.copy(isSelected = it.id == id) }
            )
        }
    }

    private fun invalidatePrimaryWordSelection(id: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                primaryWords = content.primaryWords.map { it.copy(isSelected = it.id == id) }
            )
        }
    }

    private fun markWordsAsGuessed(id: Int) {
        getContentOrNull()?.let { content ->
            pairsCounter += 1
            _state.value = content.copy(
                learningWords = content.learningWords.map {
                    if (it.id == id) it.copy(isSelected = false, isGuessed = true) else it
                },
                primaryWords = content.primaryWords.map {
                    if (it.id == id) it.copy(isSelected = false, isGuessed = true) else it
                }
            )
        }
        when {
            isWholePackGuessed() -> updatePackOfWords()
            isAllWordsGuessed() -> invalidateAllWords()
        }
    }

    private suspend fun markWordsAsWrong(learningWordId: Int, primaryWordId: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                learningWords = content.learningWords.map {
                    if (it.id == learningWordId) it.copy(isSelected = false, isWrong = true) else it
                },
                primaryWords = content.primaryWords.map {
                    if (it.id == primaryWordId) it.copy(isSelected = false, isWrong = true) else it
                }
            )
            delay(WRONG_GUESS_ERROR_DURATION)
            invalidateWrongWords(learningWordId, primaryWordId)
        }
    }

    private fun invalidateWrongWords(learningWordId: Int, primaryWordId: Int) {
        getContentOrNull()?.let { content ->
            _state.value = content.copy(
                learningWords = content.learningWords.map {
                    if (it.id == learningWordId) it.copy(isSelected = false, isWrong = false) else it
                },
                primaryWords = content.primaryWords.map {
                    if (it.id == primaryWordId) it.copy(isSelected = false, isWrong = false) else it
                }
            )
        }
    }

    private fun isWholePackGuessed(): Boolean {
        val isGuessed = getContentOrNull()?.let { content ->
            val isAllPrimaryWordsGuessed = content.primaryWords.all { it.isGuessed }
            val isAllLearningWordsGuessed = content.learningWords.all { it.isGuessed }
            isAllPrimaryWordsGuessed && isAllLearningWordsGuessed
        } ?: false
        return isGuessed
    }

    private fun updatePackOfWords() {
        val currentPackIds = getContentOrNull()?.learningWords.orEmpty().map { it.id }
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
                learningWords = content.learningWords.map {
                    it.copy(isSelected = false, isGuessed = false, isWrong = false)
                },
                primaryWords = content.primaryWords.map {
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
