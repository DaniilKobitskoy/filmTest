package com.test.filmstest.data.repository

import java.util.regex.Pattern
import com.test.filmstest.data.database.PlanetDao
import com.test.filmstest.data.model.PlanetEntity
import com.test.filmstest.data.network.ApiService

class PlanetRepository(private val apiService: ApiService, private val planetDao: PlanetDao) {

    suspend fun fetchAndSavePlanet(planetUrl: String): PlanetEntity? {
        val planetId = extractIdFromUrl(planetUrl)
        val planetEntity = planetDao.getPlanetById(planetId)
        return if (planetEntity == null) {
            val response = apiService.getPlanetByUrl(planetUrl)
            if (response.isSuccessful) {
                response.body()?.let {
                    val newPlanetEntity = PlanetEntity(
                        id = planetId,
                        name = it.name,
                        rotation_period = it.rotation_period,
                        orbital_period = it.orbital_period,
                        diameter = it.diameter,
                        climate = it.climate,
                        gravity = it.gravity,
                        terrain = it.terrain,
                        surface_water = it.surface_water,
                        population = it.population
                    )
                    planetDao.insertPlanet(newPlanetEntity)
                    newPlanetEntity
                }
            } else {
                null
            }
        } else {
            planetEntity
        }
    }

    private fun extractIdFromUrl(url: String): Long {
        val pattern = Pattern.compile(".*/(\\d+)/?")
        val matcher = pattern.matcher(url)
        return if (matcher.find()) {
            matcher.group(1)?.toLongOrNull() ?: 0L
        } else {
            0L
        }
    }
}
