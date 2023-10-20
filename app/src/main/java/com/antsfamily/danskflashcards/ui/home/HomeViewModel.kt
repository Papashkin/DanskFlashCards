package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.Word
import com.antsfamily.danskflashcards.data.Word.Companion.mapToModel
import com.antsfamily.danskflashcards.data.WordModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    companion object {
        private const val FIREBASE_DB_CHILD_PATH = "words"
    }

    private var databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val gson: Gson by lazy {
        Gson()
    }

    private var words = emptyList<Word?>()

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _showSnackbarEvent = MutableSharedFlow<String>()
    val showSnackbarEvent: SharedFlow<String> = _showSnackbarEvent.asSharedFlow()

    init {
        getData()
    }

    fun onWordCardClick(word: WordModel, isDanish: Boolean) = viewModelScope.launch {
        (_state.value as? HomeUiState.Content)?.let { content ->
            _state.update { _ ->
                content.copy(
                    danish = if (!isDanish) content.danish else content.danish.map { danishWord ->
                        danishWord.copy(isSelected = danishWord.id == word.id)
                    },
                    english = if (isDanish) content.english else content.english.map { englishWord ->
                        englishWord.copy(isSelected = englishWord.id == word.id)
                    },
                )
            }
        }
        checkGuessedPairs()
    }

    private fun getData() = viewModelScope.launch {
        try {
            words = async(Dispatchers.IO) { fetchWords() }.await()

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

    private suspend fun fetchWords(): List<Word?> = suspendCancellableCoroutine {
        try {
            databaseRef.child(FIREBASE_DB_CHILD_PATH).get().addOnSuccessListener { snapshot ->
                val words = if (snapshot.exists()) {
                    val snapshotValue =
                        snapshot.getValue<ArrayList<HashMap<String, Object>>>().orEmpty()
                    val jsonData = gson.toJson(snapshotValue)
                    val listType = object : TypeToken<List<Word>>() {}.type
                    Gson().fromJson<List<Word?>>(jsonData, listType)
                } else {
                    emptyList()
                }
                it.resume(words)
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
        } catch (e: Exception) {
            it.resumeWithException(e)
        }
    }

    private suspend fun checkGuessedPairs() {
        (_state.value as? HomeUiState.Content)?.let { content ->
            _state.update { _ ->
                content.copy(
                    danish = content.danish.map { word ->
                        if (word.isGuessed) {
                            word
                        } else {
                            word.copy(
                                isGuessed = word.isSelected && (content.english.firstOrNull { it.id == word.id }?.isSelected
                                    ?: false)
                            )
                        }
                    },
                    english = content.english.map { word ->
                        if (word.isGuessed) {
                            word
                        } else {
                            word.copy(
                                isGuessed = word.isSelected && (content.danish.firstOrNull { it.id == word.id }?.isSelected
                                    ?: false)
                            )
                        }
                    },
                )
            }
        }
    }
}