package dataSource.remote

import dataSource.models.ApiResponse
import dataSource.models.CharacterApiModel
import dataSource.remote.api.CharacterApiService

class CharacterRemoteDataSource(
    private val characterApiService: CharacterApiService
) {
    suspend fun getCharacters(): ApiResponse<List<CharacterApiModel>> {
        return safeApiCall {
            characterApiService.getCharacters()
        }.getOrElse { exception ->
            throw exception
        }
    }

    suspend fun loadNextPage(url: String): ApiResponse<List<CharacterApiModel>> {
        return safeApiCall {
            characterApiService.loadNextPage(url)
        }.getOrElse { exception ->
            throw exception
        }
    }

    suspend fun getCharacterById(id: Int): CharacterApiModel {
        return safeApiCall {
            characterApiService.getCharacterById(id)
        }.getOrElse { exception ->
            throw exception
        }
    }

    suspend fun searchCharacterByName(name: String): ApiResponse<List<CharacterApiModel>> {
        return safeApiCall {
            characterApiService.getCharacterByName(name)
        }.getOrElse { exception ->
            throw exception
        }
    }
}