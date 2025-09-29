package usaCase

import CharactersResponse
import repository.CharacterRepository

class GetCharactersUseCase(private val repo: CharacterRepository) {
    suspend operator fun invoke(): CharactersResponse = repo.getCharacters()
}