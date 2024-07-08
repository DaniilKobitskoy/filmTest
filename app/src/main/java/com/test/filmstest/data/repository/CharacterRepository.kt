package com.test.filmstest.data.repository

import androidx.lifecycle.LiveData
import com.test.filmstest.data.database.CharacterDao
import com.test.filmstest.data.database.FilmDao
import com.test.filmstest.data.model.CharacterEntity
import com.test.filmstest.data.model.FilmCharacterCrossRef
import com.test.filmstest.data.network.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import java.util.regex.Pattern

class CharacterRepository(
    private val apiService: ApiService,
    private val characterDao: CharacterDao,
    private val filmDao: FilmDao
) {

    suspend fun fetchAndSaveCharactersForFilm(filmId: Long, characterUrls: List<String>) =
        coroutineScope {
            val characterDeferred = characterUrls.map { url ->
                async(Dispatchers.IO) {
                    val characterId = extractId(url)
                    val characterEntity = characterDao.getCharacterById(characterId)
                    if (characterEntity == null) {
                        val response = apiService.getCharacterByUrl(url)
                        if (response.isSuccessful) {
                            val character = response.body()
                            character?.let {
                                val characterEntity = CharacterEntity(
                                    id = characterId,
                                    name = it.name,
                                    height = it.height,
                                    mass = it.mass,
                                    hair_color = it.hair_color,
                                    skin_color = it.skin_color,
                                    eye_color = it.eye_color,
                                    birth_year = it.birth_year,
                                    gender = it.gender,
                                    homeworld = it.homeworld,
                                    filmIds = it.filmUrls.map { filmUrl -> extractId(filmUrl).toInt() }
                                )
                                characterDao.insertCharacter(characterEntity)
                                filmDao.insertFilmCharacterCrossRef(
                                    FilmCharacterCrossRef(
                                        filmId,
                                        characterId
                                    )
                                )
                            }
                        }
                    }
                }
            }
            characterDeferred.awaitAll()
        }

    fun getCharactersByFilmId(filmId: Long): LiveData<List<CharacterEntity>> {
        return characterDao.getCharactersByFilmId(filmId)
    }

    fun getAllCharacters(): LiveData<List<CharacterEntity>> {
        return characterDao.getAllCharacters()
    }

    private fun extractId(url: String): Long {
        val pattern = Pattern.compile(".*/(\\d+)/?")
        val matcher = pattern.matcher(url)
        return if (matcher.find()) {
            matcher.group(1)?.toLongOrNull() ?: 0L
        } else {
            0L
        }
    }
}
