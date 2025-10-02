package mappers

import SuccessResponse
import dataSource.models.CharacterApiModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import dataSource.models.RmLocation as ApiRmLocation

class CharacterMapperTest {
    private lateinit var mapper: CharacterMapper

    @BeforeEach
    fun setUp() {
        mapper = CharacterMapper()
    }

    @Test
    fun `toDomain should map single CharacterApiModel to CharacterModel correctly`() {
        val apiModel = CharacterApiModel(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "Genius",
            gender = "Male",
            origin = ApiRmLocation("Earth (C-137)", "https://rickandmortyapi.com/api/location/1"),
            location = ApiRmLocation("Citadel of Ricks", "https://rickandmortyapi.com/api/location/3"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z"
        )

        val result = mapper.toDomain(apiModel)

        assertEquals(1, result.id)
        assertEquals("Rick Sanchez", result.name)
    }

    @Test
    fun `toDomain should map list of CharacterApiModel to CharactersResponse correctly`() {
        // Given
        val apiModels = listOf(
            CharacterApiModel(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = ApiRmLocation("Earth", "url1"),
                location = ApiRmLocation("Earth", "url2"),
                image = "image1",
                episode = listOf(),
                url = "url1",
                created = "2017-01-01"
            ),
            CharacterApiModel(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = ApiRmLocation("Earth", "url3"),
                location = ApiRmLocation("Earth", "url4"),
                image = "image2",
                episode = listOf(),
                url = "url2",
                created = "2017-01-02"
            )
        )
        val nextPageUrl = "https://rickandmortyapi.com/api/character/?page=2"

        // When
        val result = mapper.toDomain(nextPageUrl, apiModels)

        // Then
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(nextPageUrl, result.nextPageUrl)
    }

    @Test
    fun `toDomain should handle null nextPageUrl correctly`() {
        // Given
        val apiModels = listOf(
            CharacterApiModel(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = ApiRmLocation("Earth", "url1"),
                location = ApiRmLocation("Earth", "url2"),
                image = "image1",
                episode = listOf(),
                url = "url1",
                created = "2017-01-01"
            )
        )
        val nextPageUrl = null

        // When
        val result = mapper.toDomain(nextPageUrl, apiModels)

        // Then
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
    }

    @Test
    fun `toDomain should handle empty list correctly`() {
        // Given
        val apiModels = emptyList<CharacterApiModel>()
        val nextPageUrl = "https://rickandmortyapi.com/api/character/?page=2"

        // When
        val result = mapper.toDomain(nextPageUrl, apiModels)

        // Then
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(nextPageUrl, result.nextPageUrl)
        assertEquals(0, result.charactersList.size)
    }
}
