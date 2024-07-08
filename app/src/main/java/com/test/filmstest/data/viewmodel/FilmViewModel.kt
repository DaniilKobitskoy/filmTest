package com.test.filmstest.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.test.filmstest.data.database.CharacterDatabase
import com.test.filmstest.data.model.FilmEntity
import com.test.filmstest.data.network.ApiClient
import com.test.filmstest.data.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class FilmViewModel(application: Application) : AndroidViewModel(application) {

    private val filmRepository: FilmRepository
    private val _currentQuery = MutableStateFlow("")


    val allFilms: LiveData<List<FilmEntity>>

    init {
        val filmDao = CharacterDatabase.getDatabase(application).filmDao()
        val apiService = ApiClient.apiService
        filmRepository = FilmRepository(apiService, filmDao)

        allFilms = filmRepository.getAllFilms()
    }

    fun fetchAndInsertFilmsIfNotSearched() {
        viewModelScope.launch {
            val query = _currentQuery.value
            if (query.isBlank()) {
                filmRepository.fetchAndInsertFilms()
            }
        }
    }

    fun searchFilmsByTitle(query: String) {
        _currentQuery.value = query
    }

    val filteredFilms = _currentQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            filmRepository.getAllFilms().asFlow()
        } else {
            filmRepository.searchFilmsByTitle(query)
        }
    }.asLiveData()

}