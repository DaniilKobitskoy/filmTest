package com.test.filmstest.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.test.filmstest.data.database.CharacterDatabase
import com.test.filmstest.data.model.CharacterEntity
import com.test.filmstest.data.network.ApiClient
import com.test.filmstest.data.repository.CharacterRepository
import kotlinx.coroutines.launch

class CharacterViewModel(application: Application) : AndroidViewModel(application) {

    private val characterRepository: CharacterRepository
    val allCharacters: LiveData<List<CharacterEntity>>

    init {
        val characterDao = CharacterDatabase.getDatabase(application).characterDao()
        val filmDao = CharacterDatabase.getDatabase(application).filmDao()
        val apiService = ApiClient.apiService
        characterRepository = CharacterRepository(apiService, characterDao, filmDao)
        allCharacters = characterRepository.getAllCharacters()
    }

    fun fetchAndSaveCharactersForFilm(filmId: Long, characterUrls: List<String>) {
        viewModelScope.launch {
            characterRepository.fetchAndSaveCharactersForFilm(filmId, characterUrls)
        }
    }

    fun getCharactersByFilmId(filmId: Long): LiveData<List<CharacterEntity>> {
        return characterRepository.getCharactersByFilmId(filmId)
    }
}
