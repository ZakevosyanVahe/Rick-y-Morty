package usaCase

import CharactersResponse
import repository.CharacterRepository

class LoadNextPageUseCase(private val repo: CharacterRepository) {
    suspend operator fun invoke(url: String): CharactersResponse = repo.loadNextPage(url)
}