package com.example.data

import CharacterModel
import ErrorResponse
import RmLocation
import SuccessResponse
import dataSource.models.ApiInfo
import dataSource.models.ApiResponse
import dataSource.models.CharacterApiModel
import dataSource.remote.CharacterRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mappers.CharacterMapper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import repositories.CharacterRepositoryImpl
import repository.CharacterRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class CharacterRepositoryImplTest {
    private val remoteDataSource = mockk<CharacterRemoteDataSource>()
    private val characterMapper = mockk<CharacterMapper>()
    private var repository: CharacterRepository = CharacterRepositoryImpl(remoteDataSource, characterMapper)

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CharacterRepositoryImpl(remoteDataSource, characterMapper)
    }

    @Test
    fun `getCharacters returns SuccessResponse on success`() = runTest {
        val apiInfo = ApiInfo(count = 826, pages = 42, next = "next-url", prev = null)
        val apiResults = listOf(
            CharacterApiModel(
                id = 1, name = "Rick", status = "Alive", species = "Human", type = "",
                gender = "Male",
                origin = dataSource.models.RmLocation(name = "Earth", url = "o-url"),
                location = dataSource.models.RmLocation(name = "Citadel", url = "l-url"),
                image = "img", episode = listOf(), url = "u", created = "c"
            )
        )


        val apiResponse = ApiResponse(results = apiResults, info = apiInfo)

        val domainList = listOf(
            CharacterModel(
                id = 1, name = "Rick", status = "Alive", species = "Human", type = "",
                gender = "Male",
                origin = RmLocation(name = "Earth", url = "o-url"),
                location = RmLocation(name = "Citadel", url = "l-url"),
                image = "img", episode = listOf(), url = "u", created = "c"
            )
        )
        val mapped = SuccessResponse(nextPageUrl = "next-url", charactersList = domainList)

        coEvery { remoteDataSource.getCharacters() } returns apiResponse
        coEvery { characterMapper.toDomain(apiInfo.next, apiResults) } returns mapped

        val result = repository.getCharacters()

        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("next-url", result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
        assertEquals(1, result.charactersList.first().id)
    }

    @Test
    fun `getCharacters returns ErrorResponse on exception`() = runTest {
        coEvery { remoteDataSource.getCharacters() } throws RuntimeException("network error")

        val result = repository.getCharacters()

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("network error", result.errorMsg)
    }

    @Test
    fun `loadNextPage returns SuccessResponse on success`() = runTest {
        val url = "next-url"
        val apiInfo = ApiInfo(count = 826, pages = 42, next = "next-next-url", prev = null)
        val apiResults = listOf(
            CharacterApiModel(
                id = 2, name = "Morty", status = "Alive", species = "Human", type = "",
                gender = "Male",
                origin = dataSource.models.RmLocation(name = "Earth", url = "o-url"),
                location = dataSource.models.RmLocation(name = "Citadel", url = "l-url"),
                image = "img", episode = listOf(), url = "u", created = "c"
            )
        )
        val apiResponse = ApiResponse(results = apiResults, info = apiInfo)

        val domainList = listOf(
            CharacterModel(
                id = 2, name = "Morty", status = "Alive", species = "Human", type = "",
                gender = "Male",
                origin = RmLocation(name = "Earth", url = "o-url"),
                location = RmLocation(name = "Citadel", url = "l-url"),
                image = "img", episode = listOf(), url = "u", created = "c"
            )
        )
        val mapped = SuccessResponse(nextPageUrl = "next-next-url", charactersList = domainList)

        coEvery { remoteDataSource.loadNextPage(url) } returns apiResponse
        coEvery { characterMapper.toDomain(apiInfo.next, apiResults) } returns mapped

        val result = repository.loadNextPage(url)

        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("next-next-url", result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
        assertEquals(2, result.charactersList.first().id)
    }

    @Test
    fun `loadNextPage returns ErrorResponse on exception`() = runTest {
        coEvery { remoteDataSource.loadNextPage("bad") } throws IllegalStateException("bad url")

        val result = repository.loadNextPage("bad")

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("bad url", result.errorMsg)
    }

    @Test
    fun `getCharacterById returns SuccessResponse with 1 character mapped`() = runTest {
        val api = CharacterApiModel(
            id = 3, name = "Summer", status = "Alive", species = "Human", type = "",
            gender = "Female",
            origin = dataSource.models.RmLocation(name = "Earth", url = "o-url"),
            location = dataSource.models.RmLocation(name = "Citadel", url = "l-url"),
            image = "img", episode = listOf(), url = "u", created = "c"
        )
        val domain = CharacterModel(
            id = 3, name = "Summer", status = "Alive", species = "Human", type = "",
            gender = "Female",
            origin = RmLocation(name = "Earth", url = "o-url"),
            location = RmLocation(name = "Citadel", url = "l-url"),
            image = "img", episode = listOf(), url = "u", created = "c"
        )

        coEvery { remoteDataSource.getCharacterById(3) } returns api
        coEvery { characterMapper.toDomain(api) } returns domain

        val result = repository.getCharacterById(3)

        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
        assertEquals(3, result.charactersList.first().id)
    }

    @Test
    fun `getCharacterById returns ErrorResponse on exception`() = runTest {
        coEvery { remoteDataSource.getCharacterById(99) } throws NoSuchElementException("not found")

        val result = repository.getCharacterById(99)

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("not found", result.errorMsg)
    }

    @Test
    fun `searchCharacterByName returns SuccessResponse on success`() = runTest {
        val apiInfo = ApiInfo(count = 826, pages = 42, next = "next-search-url", prev = null)
        val apiResults = listOf(
            CharacterApiModel(
                id = 1, name = "Rick Sanchez", status = "Alive", species = "Human", type = "",
                gender = "Male",
                origin = dataSource.models.RmLocation(name = "Earth", url = "o-url"),
                location = dataSource.models.RmLocation(name = "Citadel", url = "l-url"),
                image = "img", episode = listOf(), url = "u", created = "c"
            )
        )
        val apiResponse = ApiResponse(results = apiResults, info = apiInfo)

        val domainList = listOf(
            CharacterModel(
                id = 1, name = "Rick Sanchez", status = "Alive", species = "Human", type = "",
                gender = "Male",
                origin = RmLocation(name = "Earth", url = "o-url"),
                location = RmLocation(name = "Citadel", url = "l-url"),
                image = "img", episode = listOf(), url = "u", created = "c"
            )
        )
        val mapped = SuccessResponse(nextPageUrl = "next-search-url", charactersList = domainList)

        coEvery { remoteDataSource.searchCharacterByName("rick") } returns apiResponse
        coEvery { characterMapper.toDomain(apiInfo.next, apiResults) } returns mapped

        val result = repository.searchCharacterByName("rick")

        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("next-search-url", result.nextPageUrl)
        assertEquals(1, result.charactersList.size)
        assertEquals("Rick Sanchez", result.charactersList.first().name)
    }

    @Test
    fun `searchCharacterByName returns ErrorResponse on exception`() = runTest {
        coEvery { remoteDataSource.searchCharacterByName("evil") } throws RuntimeException("500")

        val result = repository.searchCharacterByName("evil")

        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("500", result.errorMsg)
    }
}