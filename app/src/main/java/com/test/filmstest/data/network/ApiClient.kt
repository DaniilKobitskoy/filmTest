package com.test.filmstest.data.network

import com.test.filmstest.data.model.Character
import com.test.filmstest.data.model.FilmResponse
import com.test.filmstest.data.model.PlanetEntity
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

object ApiClient {
    private const val BASE_URL = "https://swapi.dev/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

interface ApiService {
    @GET("films/")
    suspend fun getFilms(): Response<FilmResponse>

    @GET
    suspend fun getCharacterByUrl(@Url url: String): Response<Character>

    @GET
    suspend fun getPlanetByUrl(@Url url: String): Response<PlanetEntity>
}
