package com.test.filmstest.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.test.filmstest.data.model.FilmCharacterCrossRef
import com.test.filmstest.data.model.FilmEntity
import com.test.filmstest.data.model.FilmWithCharacters
import kotlinx.coroutines.flow.Flow


@Dao
interface FilmDao {

    @Query("SELECT * FROM films WHERE title LIKE :title")
    fun searchFilmsByTitle(title: String): LiveData<List<FilmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(films: List<FilmEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmCharacterCrossRef(crossRef: FilmCharacterCrossRef)

    @Query("SELECT * FROM films")
    fun getAllFilms(): LiveData<List<FilmEntity>>

    @Transaction
    @Query("SELECT * FROM films WHERE id = :filmId")
    fun getFilmWithCharacters(filmId: Long): LiveData<FilmWithCharacters>

    @Query("SELECT * FROM films WHERE title LIKE :title")
    fun searchFilmsByTitleFlow(title: String): Flow<List<FilmEntity>>

}

