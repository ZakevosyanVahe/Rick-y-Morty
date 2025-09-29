package usaCase

import CharactersResponse
import repository.CharacterRepository

class GetCharacterByIdUseCase(private val repo: CharacterRepository) {
    suspend operator fun invoke(id: Int): CharactersResponse = repo.getCharacterById(id)
}