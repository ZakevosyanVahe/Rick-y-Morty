package repository

import CharactersResponse

interface CharacterRepository {
    suspend fun getCharacters(): CharactersResponse

    suspend fun loadNextPage(url: String): CharactersResponse

    suspend fun getCharacterById(id: Int): CharactersResponse

    suspend fun searchCharacterByName(name: String): CharactersResponse
}