package app.mappers

import CharacterModel
import RmLocation
import app.mappers.CharacterUiMapper
import model.CharacterUiModel
import model.RmLocation as UiRmLocation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CharacterUiMapperTest {
    private lateinit var mapper: CharacterUiMapper

    @BeforeEach
    fun setUp() {
        mapper = CharacterUiMapper()
    }

    @Test
    fun `toUiModel should map single CharacterModel to CharacterUiModel correctly`() {
        // Given
        val domainModel = CharacterModel(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "Genius",
            gender = "Male",
            origin = RmLocation("Earth (C-137)", "https://rickandmortyapi.com/api/location/1"),
            location = RmLocation("Citadel of Ricks", "https://rickandmortyapi.com/api/location/3"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z"
        )

        // When
        val result = mapper.toUiModel(domainModel)

        // Then
        assertEquals(1, result.id)
        assertEquals("Rick Sanchez", result.name)
        assertEquals("Alive", result.status)
        assertEquals("Human", result.species)
        assertEquals("Male", result.gender)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", result.image)
        assertEquals("https://rickandmortyapi.com/api/character/1", result.url)
        assertEquals("2017-11-04T18:48:46.250Z", result.created)
        
        // Test origin mapping
        assertEquals("Earth (C-137)", result.origin.name)
        assertEquals("https://rickandmortyapi.com/api/location/1", result.origin.url)
        
        // Test location mapping
        assertEquals("Citadel of Ricks", result.location.name)
        assertEquals("https://rickandmortyapi.com/api/location/3", result.location.url)
    }

    @Test
    fun `toUiModel should map list of CharacterModel to list of CharacterUiModel correctly`() {
        // Given
        val domainModels = listOf(
            CharacterModel(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = RmLocation("Earth", "url1"),
                location = RmLocation("Earth", "url2"),
                image = "image1",
                episode = listOf(),
                url = "url1",
                created = "2017-01-01"
            ),
            CharacterModel(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = RmLocation("Earth", "url3"),
                location = RmLocation("Earth", "url4"),
                image = "image2",
                episode = listOf(),
                url = "url2",
                created = "2017-01-02"
            )
        )

        // When
        val result = mapper.toUiModel(domainModels)

        // Then
        assertEquals(2, result.size)
        assertEquals("Rick Sanchez", result[0].name)
        assertEquals("Morty Smith", result[1].name)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
    }

    @Test
    fun `toUiModel should handle empty list correctly`() {
        // Given
        val domainModels = emptyList<CharacterModel>()

        // When
        val result = mapper.toUiModel(domainModels)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `toUiModel should handle null values in RmLocation correctly`() {
        // Given
        val domainModel = CharacterModel(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = RmLocation("Unknown", ""),
            location = RmLocation("Unknown", ""),
            image = "image1",
            episode = listOf(),
            url = "url1",
            created = "2017-01-01"
        )

        // When
        val result = mapper.toUiModel(domainModel)

        // Then
        assertEquals("Unknown", result.origin.name)
        assertEquals("", result.origin.url)
        assertEquals("Unknown", result.location.name)
        assertEquals("", result.location.url)
    }

    @Test
    fun `toUiModel should preserve all required fields`() {
        // Given
        val domainModel = CharacterModel(
            id = 42,
            name = "Test Character",
            status = "Dead",
            species = "Alien",
            type = "Test Type",
            gender = "Female",
            origin = RmLocation("Test Origin", "test-origin-url"),
            location = RmLocation("Test Location", "test-location-url"),
            image = "test-image-url",
            episode = listOf("episode1", "episode2"),
            url = "test-character-url",
            created = "2023-01-01T00:00:00.000Z"
        )

        // When
        val result = mapper.toUiModel(domainModel)

        // Then
        assertEquals(42, result.id)
        assertEquals("Test Character", result.name)
        assertEquals("Dead", result.status)
        assertEquals("Alien", result.species)
        assertEquals("Female", result.gender)
        assertEquals("test-image-url", result.image)
        assertEquals("test-character-url", result.url)
        assertEquals("2023-01-01T00:00:00.000Z", result.created)
        assertEquals("Test Origin", result.origin.name)
        assertEquals("test-origin-url", result.origin.url)
        assertEquals("Test Location", result.location.name)
        assertEquals("test-location-url", result.location.url)
    }
}
