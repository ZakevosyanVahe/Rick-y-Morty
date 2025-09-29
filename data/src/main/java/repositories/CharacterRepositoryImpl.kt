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

    override suspend fun getCharacters(): CharactersResponse {
        try {
            val apiCharacters = remoteDataSource.getCharacters()
            return characterMapper.toDomain(apiCharacters.info.next, apiCharacters.results)
        } catch (e: Exception) {
            return ErrorResponse(e.message)
        }
    }

    override suspend fun loadNextPage(url: String): CharactersResponse {
        try {
            val apiCharacters = remoteDataSource.loadNextPage(url)
            return characterMapper.toDomain(apiCharacters.info.next, apiCharacters.results)
        } catch (e: Exception) {
            return ErrorResponse(e.message)
        }

    }

    override suspend fun getCharacterById(id: Int): CharactersResponse {
        try {
            val apiCharacter = remoteDataSource.getCharacterById(id)
            return SuccessResponse(null, listOf(characterMapper.toDomain(apiCharacter)))
        } catch (e: Exception) {
            return ErrorResponse(e.message)
        }

    }

    override suspend fun searchCharacterByName(name: String): CharactersResponse {
        try {
            val apiCharacters = remoteDataSource.searchCharacterByName(name)
            return characterMapper.toDomain(apiCharacters.info.next, apiCharacters.results)
        } catch (e: Exception) {
            return ErrorResponse(e.message)
        }
    }
}
