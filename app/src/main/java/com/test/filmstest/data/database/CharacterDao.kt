package com.test.filmstest.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.filmstest.data.model.CharacterEntity
import com.test.filmstest.data.model.FilmCharacterCrossRef
import com.test.filmstest.data.model.FilmEntity
import com.test.filmstest.data.model.PlanetEntity

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Long): CharacterEntity?

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): LiveData<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id IN (SELECT characterId FROM film_character_cross_ref WHERE filmId = :filmId)")
    fun getCharactersByFilmId(filmId: Long): LiveData<List<CharacterEntity>>
}

@Database(entities = [FilmEntity::class, CharacterEntity::class, FilmCharacterCrossRef::class, PlanetEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun filmDao(): FilmDao
    abstract fun planetDao(): PlanetDao

    companion object {
        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        fun getDatabase(context: Context): CharacterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDatabase::class.java,
                    "star_wars_films.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
