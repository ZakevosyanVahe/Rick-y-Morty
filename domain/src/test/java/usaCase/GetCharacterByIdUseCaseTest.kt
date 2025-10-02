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

class GetCharacterByIdUseCaseTest {
    private val mockRepository = mockk<CharacterRepository>()
    private lateinit var useCase: GetCharacterByIdUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetCharacterByIdUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success response with single character when repository returns success`() = runTest {
        val characterId = 1
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
            nextPageUrl = null,
            charactersList = listOf(characterModel)
        )
        coEvery { mockRepository.getCharacterById(characterId) } returns expectedResponse

        val result = useCase(characterId)

        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
        assertEquals(characterId, result.charactersList.first().id)
        assertEquals("Rick Sanchez", result.charactersList.first().name)
        coVerify { mockRepository.getCharacterById(characterId) }
    }

    @Test
    fun `invoke should return error response when repository returns error`() = runTest {
        val characterId = 999
        val errorMessage = "Character not found"
        val expectedResponse = ErrorResponse(errorMessage)
        coEvery { mockRepository.getCharacterById(characterId) } returns expectedResponse

        val result = useCase(characterId)

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals(errorMessage, result.errorMsg)
        coVerify { mockRepository.getCharacterById(characterId) }
    }
}
