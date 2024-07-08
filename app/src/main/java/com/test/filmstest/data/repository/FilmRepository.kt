package com.test.filmstest.data.repository

import androidx.lifecycle.LiveData
import com.test.filmstest.data.database.FilmDao
import com.test.filmstest.data.model.FilmEntity
import com.test.filmstest.data.model.FilmWithCharacters
import com.test.filmstest.data.network.ApiService

class FilmRepository(private val apiService: ApiService, private val filmDao: FilmDao) {

    suspend fun fetchAndInsertFilms() {
        val response = apiService.getFilms()
        if (response.isSuccessful) {
            val films = response.body()?.results ?: emptyList()
            filmDao.insertFilms(films)
        }
    }

    fun getAllFilms(): LiveData<List<FilmEntity>> {
        return filmDao.getAllFilms()
    }

    fun getFilmWithCharacters(filmId: Long): LiveData<FilmWithCharacters> {
        return filmDao.getFilmWithCharacters(filmId)
    }
}
