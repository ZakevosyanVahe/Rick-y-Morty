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

class LoadNextPageUseCaseTest {
    private val mockRepository = mockk<CharacterRepository>()
    private lateinit var useCase: LoadNextPageUseCase

    @BeforeEach
    fun setUp() {
        useCase = LoadNextPageUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success response when repository returns success`() = runTest {
        val nextPageUrl = "https://rickandmortyapi.com/api/character/?page=2"
        val characterModel = CharacterModel(
            id = 2,
            name = "Morty Smith",
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
            image = "image_url_2",
            url = "character_url_2",
            created = "2017-01-02"
        )
        val expectedResponse = SuccessResponse(
            nextPageUrl = "https://rickandmortyapi.com/api/character/?page=3",
            charactersList = listOf(characterModel)
        )
        coEvery { mockRepository.loadNextPage(nextPageUrl) } returns expectedResponse

        val result = useCase(nextPageUrl)

        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("https://rickandmortyapi.com/api/character/?page=3", result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
        assertEquals("Morty Smith", result.charactersList.first().name)
        coVerify { mockRepository.loadNextPage(nextPageUrl) }
    }

    @Test
    fun `invoke should return error response when repository returns error`() = runTest {
        val nextPageUrl = "https://rickandmortyapi.com/api/character/?page=999"
        val errorMessage = "Page not found"
        val expectedResponse = ErrorResponse(errorMessage)
        coEvery { mockRepository.loadNextPage(nextPageUrl) } returns expectedResponse

        val result = useCase(nextPageUrl)

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals(errorMessage, result.errorMsg)
        coVerify { mockRepository.loadNextPage(nextPageUrl) }
    }

    @Test
    fun `invoke should handle invalid URL`() = runTest {
        val invalidUrl = "invalid-url"
        val errorMessage = "Invalid URL format"
        val expectedResponse = ErrorResponse(errorMessage)
        coEvery { mockRepository.loadNextPage(invalidUrl) } returns expectedResponse

        val result = useCase(invalidUrl)

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals(errorMessage, result.errorMsg)
        coVerify { mockRepository.loadNextPage(invalidUrl) }
    }
}
