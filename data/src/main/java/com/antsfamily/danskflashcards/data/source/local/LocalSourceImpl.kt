package com.antsfamily.danskflashcards.data.source.local

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class LocalSourceImpl @Inject constructor(
    private val gson: Gson,
    private val jsonDataSource: JsonDataSource
) : LocalSource {

    companion object {
        const val FLASHCARDS_ASSET_NAME = "dansk_flashcards.json"
    }

    private val listType = object : TypeToken<List<WordApiModel>>() {}.type

    override fun getCards(): List<WordApiModel> {
        return getDataFromJson()
    }

    override fun getCardsAmount(): Int {
        val users = getDataFromJson()
        return users.size
    }

    private fun getDataFromJson(): List<WordApiModel> {
        val data = jsonDataSource.readJsonFile(FLASHCARDS_ASSET_NAME)
        return gson.fromJson(data, listType)
    }
}