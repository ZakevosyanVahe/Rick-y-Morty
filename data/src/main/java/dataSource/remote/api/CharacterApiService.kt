package dataSource.remote.api

import dataSource.models.ApiResponse
import dataSource.models.CharacterApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CharacterApiService {
    @GET("character")
    suspend fun getCharacters(): ApiResponse<List<CharacterApiModel>>

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterApiModel

    @GET
    suspend fun loadNextPage(@Url url: String): ApiResponse<List<CharacterApiModel>>

    @GET("character/")
    suspend fun getCharacterByName(@Query("name") name: String? = null): ApiResponse<List<CharacterApiModel>>


}