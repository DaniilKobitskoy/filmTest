package com.test.filmstest.data.repository

import androidx.lifecycle.LiveData
import com.test.filmstest.data.database.FilmDao
import com.test.filmstest.data.model.FilmEntity
import com.test.filmstest.data.network.ApiService
import kotlinx.coroutines.flow.Flow

class FilmRepository(private val apiService: ApiService, private val filmDao: FilmDao) {

    suspend fun fetchAndInsertFilms() {
        val response = apiService.getFilms()
        if (response.isSuccessful) {
            val films = response.body()?.results ?: emptyList()
            filmDao.insertFilms(films)
        }
    }

    fun searchFilmsByTitle(title: String): Flow<List<FilmEntity>> {
        return filmDao.searchFilmsByTitleFlow("%$title%")
    }

    fun getAllFilms(): LiveData<List<FilmEntity>> {
        return filmDao.getAllFilms()
    }

}

