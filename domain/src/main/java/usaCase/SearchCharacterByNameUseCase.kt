package usaCase

import CharactersResponse
import repository.CharacterRepository

class SearchCharacterByNameUseCase(private val repo: CharacterRepository) {
    suspend operator fun invoke(name: String): CharactersResponse =
        repo.searchCharacterByName(name)
}