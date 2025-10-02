package dataSource.remote

import NetworkException
import NotFoundException
import dataSource.models.ApiInfo
import dataSource.models.ApiResponse
import dataSource.models.CharacterApiModel
import dataSource.remote.api.CharacterApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import java.net.HttpURLConnection
import dataSource.models.RmLocation as ApiRmLocation

class CharacterRemoteDataSourceTest {
    private val mockApiService = mockk<CharacterApiService>()
    private lateinit var dataSource: CharacterRemoteDataSource

    @BeforeEach
    fun setUp() {
        dataSource = CharacterRemoteDataSource(mockApiService)
    }

    @Test
    fun `getCharacters should return ApiResponse when API call succeeds`() = runTest {
        val apiInfo = ApiInfo(count = 826, pages = 42, next = "next-url", prev = null)
        val apiResults = listOf(
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
        val expectedResponse = ApiResponse(results = apiResults, info = apiInfo)
        coEvery { mockApiService.getCharacters() } returns expectedResponse

        val result = dataSource.getCharacters()

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `getCharacters should throw NetworkException when API call fails`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { mockApiService.getCharacters() } throws exception

        val thrownException = assertThrows(NetworkException::class.java) {
            runTest { dataSource.getCharacters() }
        }
        assertEquals("Error fetching characters", thrownException.message)
        assertEquals(exception, thrownException.cause)
    }

    @Test
    fun `loadNextPage should return ApiResponse when API call succeeds`() = runTest {
        val url = "https://rickandmortyapi.com/api/character/?page=2"
        val apiInfo = ApiInfo(count = 826, pages = 42, next = "next-next-url", prev = "prev-url")
        val apiResults = listOf(
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
        val expectedResponse = ApiResponse(results = apiResults, info = apiInfo)
        coEvery { mockApiService.loadNextPage(url) } returns expectedResponse

        val result = dataSource.loadNextPage(url)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `loadNextPage should throw NetworkException when API call fails`() = runTest {
        val url = "https://rickandmortyapi.com/api/character/?page=999"
        val exception = RuntimeException("Network error")
        coEvery { mockApiService.loadNextPage(url) } throws exception

        val thrownException = assertThrows(NetworkException::class.java) {
            runTest { dataSource.loadNextPage(url) }
        }
        assertEquals("Error fetching characters", thrownException.message)
        assertEquals(exception, thrownException.cause)
    }

    @Test
    fun `getCharacterById should return CharacterApiModel when API call succeeds`() = runTest {
        val characterId = 1
        val expectedCharacter = CharacterApiModel(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = ApiRmLocation(
                name = "Earth",
                url = "url1"
            ),
            location = ApiRmLocation(
                name = "Earth",
                url = "url2"
            ),
            image = "image1",
            episode = listOf(),
            url = "url1",
            created = "2017-01-01"
        )
        coEvery { mockApiService.getCharacterById(characterId) } returns expectedCharacter

        val result = dataSource.getCharacterById(characterId)

        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `getCharacterById should throw NetworkException when API call fails`() = runTest {
        val characterId = 999
        val exception = RuntimeException("Character not found")
        coEvery { mockApiService.getCharacterById(characterId) } throws exception

        val thrownException = assertThrows(NetworkException::class.java) {
            runTest { dataSource.getCharacterById(characterId) }
        }
        assertEquals("Error fetching character", thrownException.message)
        assertEquals(exception, thrownException.cause)
    }

    @Test
    fun `searchCharacterByName should return ApiResponse when API call succeeds`() = runTest {
        val searchQuery = "Rick"
        val apiInfo = ApiInfo(count = 1, pages = 1, next = null, prev = null)
        val apiResults = listOf(
            CharacterApiModel(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = ApiRmLocation(
                    name = "Earth",
                    url = "url1"
                ),
                location = ApiRmLocation(
                    name = "Earth",
                    url = "url2"
                ),
                image = "image1",
                episode = listOf(),
                url = "url1",
                created = "2017-01-01"
            )
        )
        val expectedResponse = ApiResponse(results = apiResults, info = apiInfo)
        coEvery { mockApiService.getCharacterByName(searchQuery) } returns expectedResponse

        val result = dataSource.searchCharacterByName(searchQuery)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `searchCharacterByName should throw NotFoundException when API returns 404`() = runTest {
        val searchQuery = "NonExistent"
        val httpException = HttpException(
            retrofit2.Response.error<Any>(
                HttpURLConnection.HTTP_NOT_FOUND,
                okhttp3.ResponseBody.create(null, "Not Found")
            )
        )
        coEvery { mockApiService.getCharacterByName(searchQuery) } throws httpException

        val thrownException = assertThrows(NotFoundException::class.java) {
            runTest { dataSource.searchCharacterByName(searchQuery) }
        }
        assertEquals("There is nothing here", thrownException.message)
        assertEquals(httpException, thrownException.cause)
    }

    @Test
    fun `searchCharacterByName should throw NetworkException when API returns other error`() = runTest {
        val searchQuery = "Rick"
        val httpException = HttpException(
            retrofit2.Response.error<Any>(
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                okhttp3.ResponseBody.create(null, "Internal Server Error")
            )
        )
        coEvery { mockApiService.getCharacterByName(searchQuery) } throws httpException

        val thrownException = assertThrows(NetworkException::class.java) {
            runTest { dataSource.searchCharacterByName(searchQuery) }
        }
        assertEquals("Error fetching characters", thrownException.message)
        assertEquals(httpException, thrownException.cause)
    }

    @Test
    fun `searchCharacterByName should throw NetworkException when non-HTTP exception occurs`() = runTest {
        val searchQuery = "Rick"
        val exception = RuntimeException("Network error")
        coEvery { mockApiService.getCharacterByName(searchQuery) } throws exception

        val thrownException = assertThrows(NetworkException::class.java) {
            runTest { dataSource.searchCharacterByName(searchQuery) }
        }
        assertEquals("Error fetching characters", thrownException.message)
        assertEquals(exception, thrownException.cause)
    }
}
