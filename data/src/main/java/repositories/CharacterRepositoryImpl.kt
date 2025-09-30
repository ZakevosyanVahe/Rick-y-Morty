package repositories

import CharactersResponse
import ErrorResponse
import SuccessResponse
import dataSource.remote.CharacterRemoteDataSource
import mappers.CharacterMapper
import repository.CharacterRepository

class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val characterMapper: CharacterMapper
) : CharacterRepository {

    private var cachedCharacters: CharactersResponse? = null

    override suspend fun getCharacters(): CharactersResponse {
        cachedCharacters?.let { return it }
        return try {
            val apiCharacters = remoteDataSource.getCharacters()
            val response = characterMapper.toDomain(apiCharacters.info.next, apiCharacters.results)
            cachedCharacters = response
            response
        } catch (e: Exception) {
            ErrorResponse(e.message)
        }
    }

    override suspend fun loadNextPage(url: String): CharactersResponse {
        return try {
            val apiCharacters = remoteDataSource.loadNextPage(url)
            characterMapper.toDomain(apiCharacters.info.next, apiCharacters.results)
        } catch (e: Exception) {
            ErrorResponse(e.message)
        }
    }

    override suspend fun getCharacterById(id: Int): CharactersResponse {
        return try {
            val apiCharacter = remoteDataSource.getCharacterById(id)
            SuccessResponse(null, listOf(characterMapper.toDomain(apiCharacter)))
        } catch (e: Exception) {
            ErrorResponse(e.message)
        }
    }

    override suspend fun searchCharacterByName(name: String): CharactersResponse {
        return try {
            val apiCharacters = remoteDataSource.searchCharacterByName(name)
            characterMapper.toDomain(apiCharacters.info.next, apiCharacters.results)
        } catch (e: Exception) {
            ErrorResponse(e.message)
        }
    }
}
