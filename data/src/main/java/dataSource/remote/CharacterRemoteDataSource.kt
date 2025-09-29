package dataSource.remote

import NetworkException
import NotFoundException
import dataSource.models.ApiResponse
import dataSource.models.CharacterApiModel
import dataSource.remote.api.CharacterApiService
import retrofit2.HttpException

class CharacterRemoteDataSource(
    private val characterApiService: CharacterApiService
) {
    suspend fun getCharacters(): ApiResponse<List<CharacterApiModel>> {
        return try {
            characterApiService.getCharacters()
        } catch (e: Exception) {
            throw NetworkException("Error fetching characters", e)
        }
    }

    suspend fun loadNextPage(url: String): ApiResponse<List<CharacterApiModel>> {
        return try {
            characterApiService.loadNextPage(url)
        } catch (e: Exception) {
            throw NetworkException("Error fetching characters", e)
        }
    }

    suspend fun getCharacterById(id: Int): CharacterApiModel {
        return try {
            characterApiService.getCharacterById(id)
        } catch (e: Exception) {
            throw NetworkException("Error fetching character", e)
        }
    }

    suspend fun searchCharacterByName(name: String): ApiResponse<List<CharacterApiModel>> {
        return try {
            characterApiService.getCharacterByName(name)
        } catch (e: Exception) {
            if ((e as HttpException).code() == 404) {
                throw NotFoundException("There is nothing here", e)
            } else throw NetworkException("Error fetching characters", e)
        }
    }
}