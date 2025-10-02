package usaCase

import CharacterModel
import ErrorResponse
import RmLocation
import SuccessResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.CharacterRepository

class GetCharactersUseCaseTest {
    private val mockRepository = mockk<CharacterRepository>()
    private lateinit var useCase: GetCharactersUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetCharactersUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success response when repository returns success`() = runTest {
        val characterModel = CharacterModel(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = RmLocation(
                name = "Earth",
                url = "url1"
            ),
            location = RmLocation(
                name = "Earth",
                url = "url2"
            ),
            image = "image_url",
            url = "character_url",
            created = "2017-01-01"
        )
        val expectedResponse = SuccessResponse(
            nextPageUrl = "next_page_url",
            charactersList = listOf(characterModel)
        )
        coEvery { mockRepository.getCharacters() } returns expectedResponse

        val result = useCase()

        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("next_page_url", result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
        assertEquals("Rick Sanchez", result.charactersList.first().name)
        coVerify { mockRepository.getCharacters() }
    }

    @Test
    fun `invoke should return error response when repository returns error`() = runTest {
        val errorMessage = "Network error"
        val expectedResponse = ErrorResponse(errorMessage)
        coEvery { mockRepository.getCharacters() } returns expectedResponse

        val result = useCase()

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals(errorMessage, result.errorMsg)
        coVerify { mockRepository.getCharacters() }
    }
}
