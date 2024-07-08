package com.test.filmstest.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.test.filmstest.data.database.Converters


data class FilmResponse(
    var results: List<FilmEntity>
)


@Entity(tableName = "film_character_cross_ref", primaryKeys = ["filmId", "characterId"])
data class FilmCharacterCrossRef(
    val filmId: Long,
    val characterId: Long
)



@Entity(tableName = "films")
@TypeConverters(Converters::class)
data class FilmEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val episode_id: Int,
    val opening_crawl: String,
    val director: String,
    val producer: String,
    val release_date: String,
    @TypeConverters(Converters::class) val characters: List<String>,
    @TypeConverters(Converters::class) val planets: List<String>,
    @TypeConverters(Converters::class) val starships: List<String>,
    @TypeConverters(Converters::class) val vehicles: List<String>,
    @TypeConverters(Converters::class) val species: List<String>
)

data class Character(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val height: String,
    val mass: String,
    val hair_color: String,
    val skin_color: String,
    val eye_color: String,
    val birth_year: String,
    val gender: String,
    val homeworld: String,
    @SerializedName("films") val filmUrls: List<String>
)


@Entity(tableName = "characters")
@TypeConverters(Converters::class)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val height: String,
    val mass: String,
    val hair_color: String,
    val skin_color: String,
    val eye_color: String,
    val birth_year: String,
    val gender: String,
    val homeworld: String,
    @TypeConverters(Converters::class) val filmIds: List<Int>  // Store film IDs instead of URLs
)


data class FilmWithCharacters(
    @Embedded val film: FilmEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(FilmCharacterCrossRef::class, parentColumn = "filmId", entityColumn = "characterId")
    )
    val characters: List<CharacterEntity>
)

@Entity(tableName = "planets")
data class PlanetEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val rotation_period: String,
    val orbital_period: String,
    val diameter: String,
    val climate: String,
    val gravity: String,
    val terrain: String,
    val surface_water: String,
    val population: String
)
