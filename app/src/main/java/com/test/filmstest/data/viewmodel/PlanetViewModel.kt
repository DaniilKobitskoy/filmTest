package com.test.filmstest.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.filmstest.data.database.CharacterDatabase
import com.test.filmstest.data.model.PlanetEntity
import com.test.filmstest.data.network.ApiClient
import com.test.filmstest.data.repository.PlanetRepository
import kotlinx.coroutines.launch

class PlanetViewModel(application: Application) : AndroidViewModel(application) {

    private val planetRepository: PlanetRepository

    init {
        val planetDao = CharacterDatabase.getDatabase(application).planetDao()
        val apiService = ApiClient.apiService
        planetRepository = PlanetRepository(apiService, planetDao)
    }

    private val _planet = MutableLiveData<PlanetEntity>()
    val planet: LiveData<PlanetEntity> get() = _planet

    fun fetchPlanet(planetUrl: String) {
        viewModelScope.launch {
            val planetEntity = planetRepository.fetchAndSavePlanet(planetUrl)
            planetEntity?.let {
                _planet.postValue(it)
            }
        }
    }
}
