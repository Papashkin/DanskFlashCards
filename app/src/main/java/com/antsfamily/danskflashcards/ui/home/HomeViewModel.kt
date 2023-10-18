package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.Word
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private var databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val gson: Gson by lazy {
        Gson()
    }

    private var words = emptyList<Word>()

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    init {
        getData()
    }

    private fun getData() = viewModelScope.launch {
        try {
            words = async(Dispatchers.IO) { fetchWords() }.await()
            _state.update { HomeUiState.Content(words) }
        } catch (e: Exception) {
            _state.update { HomeUiState.Error(errorMessage = e.message.orEmpty()) }
        }
    }

    private suspend fun fetchWords(): List<Word> = suspendCancellableCoroutine {
        databaseRef.child("words").get().addOnSuccessListener { snapshot ->
            val words = if (snapshot.exists()) {
                val snapshotValue = snapshot.getValue<ArrayList<HashMap<String, Object>>>().orEmpty()
                val jsonData = gson.toJson(snapshotValue)
                val listType = object : TypeToken<List<Word>>() {}.type
                Gson().fromJson<List<Word>>(jsonData, listType)
            } else {
                emptyList()
            }
            it.resume(words)
        }.addOnFailureListener { exception ->
            it.resumeWithException(exception)
        }
    }
}