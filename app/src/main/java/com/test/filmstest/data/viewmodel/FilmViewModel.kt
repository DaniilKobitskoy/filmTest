package com.test.filmstest.data.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.test.filmstest.data.database.CharacterDatabase
import com.test.filmstest.data.model.FilmEntity
import com.test.filmstest.data.model.FilmWithCharacters
import com.test.filmstest.data.network.ApiClient
import com.test.filmstest.data.repository.FilmRepository
import kotlinx.coroutines.launch

class FilmViewModel(application: Application) : AndroidViewModel(application) {

    private val filmRepository: FilmRepository
    val allFilms: LiveData<List<FilmEntity>>

    init {
        val filmDao = CharacterDatabase.getDatabase(application).filmDao()
        val apiService = ApiClient.apiService
        filmRepository = FilmRepository(apiService, filmDao)
        allFilms = filmRepository.getAllFilms()
    }

    fun fetchAndInsertFilms() {
        viewModelScope.launch {
            filmRepository.fetchAndInsertFilms()
        }
    }

    fun getFilmWithCharacters(filmId: Long): LiveData<FilmWithCharacters> {
        return filmRepository.getFilmWithCharacters(filmId)
    }
}
