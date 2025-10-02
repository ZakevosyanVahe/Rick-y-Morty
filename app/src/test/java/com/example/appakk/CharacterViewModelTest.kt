
import app.mappers.CharacterUiMapper
import app.ui.CharacterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import model.CharacterUiModel
import model.Idle
import model.Success
import model.UiError
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import usaCase.GetCharacterByIdUseCase
import usaCase.GetCharactersUseCase
import usaCase.LoadNextPageUseCase
import usaCase.SearchCharacterByNameUseCase

@ExperimentalCoroutinesApi
class CharacterViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val getCharacterUseCase: GetCharactersUseCase = mock()
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase = mock()
    private val loadNextPageUseCase: LoadNextPageUseCase = mock()
    private val searchCharacterByNameUseCase: SearchCharacterByNameUseCase = mock()
    private val uiMapper: CharacterUiMapper = mock()
    private lateinit var viewModel: CharacterViewModel
    private val testCharacterModel = CharacterModel(
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

    private val testCharacterUiModel = CharacterUiModel(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = model.RmLocation(
            name = "Earth",
            url = "url1"
        ),
        location = model.RmLocation(
            name = "Earth",
            url = "url2"
        ),
        image = "image_url",
        url = "character_url",
        created = "2017-01-01"
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CharacterViewModel(
            getCharacterUseCase = getCharacterUseCase,
            getCharacterByIdUseCase = getCharacterByIdUseCase,
            loadNextPageUseCase = loadNextPageUseCase,
            searchCharacterByNameUseCase = searchCharacterByNameUseCase,
            uiMapper = uiMapper
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Idle`() = testScope.runTest {
        val initialState = viewModel.uiState.first()
        assertTrue(initialState is Idle)
    }

    @Test
    fun `loadCharacters should update state to Loading then Success when use case returns success`() = testScope.runTest {
        val successResponse = SuccessResponse(
            nextPageUrl = "next_page_url",
            charactersList = listOf(testCharacterModel)
        )
        whenever(getCharacterUseCase()).thenReturn(successResponse)
        whenever(uiMapper.toUiModel(listOf(testCharacterModel))).thenReturn(listOf(testCharacterUiModel))
        viewModel.loadCharacters()
        advanceUntilIdle()
        val successState = viewModel.uiState.first() as Success
        assertEquals(listOf(testCharacterUiModel), successState.characters)
        verify(getCharacterUseCase).invoke()
    }

    @Test
    fun `loadCharacters should update state to Loading then UiError when use case returns error`() = testScope.runTest {

        val errorMessage = "Network error"
        val errorResponse = ErrorResponse(errorMessage)
        whenever(getCharacterUseCase()).thenReturn(errorResponse)
        viewModel.loadCharacters()
        advanceUntilIdle()
        val errorState = viewModel.uiState.first() as UiError
        assertEquals(errorMessage, errorState.exception)
        verify(getCharacterUseCase).invoke()
    }

    @Test
    fun `loadNextPage should not proceed when current state is not Success`() = testScope.runTest {

        viewModel.loadNextPage()
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertTrue(state is Idle)
    }

    @Test
    fun `loadNextPage should load next page and append characters when successful`() = testScope.runTest {
        val newCharacterModel = CharacterModel(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = RmLocation(
                name = "Earth",
                url = "url3"
            ),
            location = RmLocation(
                name = "Earth",
                url = "url4"
            ),
            image = "image_url_2",
            url = "character_url_2",
            created = "2017-01-02"
        )
        val newCharacterUiModel = CharacterUiModel(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = model.RmLocation(
                name = "Earth",
                url = "url3"
            ),
            location = model.RmLocation("Earth", "url4"),
            image = "image_url_2",
            url = "character_url_2",
            created = "2017-01-02"
        )
        val nextPageUrl = "next_page_url"
        val successResponse = SuccessResponse(
            nextPageUrl = "next_next_page_url",
            charactersList = listOf(newCharacterModel)
        )

        viewModel.loadCharacters()
        whenever(loadNextPageUseCase(nextPageUrl)).thenReturn(successResponse)
        whenever(uiMapper.toUiModel(listOf(newCharacterModel))).thenReturn(listOf(newCharacterUiModel))
        viewModel.loadNextPage()
        advanceUntilIdle()

        val successState = viewModel.uiState.first() as Success
        assertEquals(listOf(testCharacterUiModel, newCharacterUiModel), successState.characters)
        verify(loadNextPageUseCase).invoke(nextPageUrl)
    }

    @Test
    fun `search should update state to Loading then Success when search is successful`() = testScope.runTest {
        val searchQuery = "Rick"
        val successResponse = SuccessResponse(
            nextPageUrl = "next_page_url",
            charactersList = listOf(testCharacterModel)
        )
        whenever(searchCharacterByNameUseCase(searchQuery)).thenReturn(successResponse)
        whenever(uiMapper.toUiModel(listOf(testCharacterModel))).thenReturn(listOf(testCharacterUiModel))

        viewModel.search(searchQuery)
        advanceUntilIdle()

        val successState = viewModel.uiState.first() as Success
        assertEquals(listOf(testCharacterUiModel), successState.characters)
        verify(searchCharacterByNameUseCase).invoke(searchQuery)
    }

    @Test
    fun `search should update state to Loading then UiError when search fails`() = testScope.runTest {

        val searchQuery = "Rick"
        val errorMessage = "Search failed"
        val errorResponse = ErrorResponse(errorMessage)
        whenever(searchCharacterByNameUseCase(searchQuery)).thenReturn(errorResponse)

        viewModel.search(searchQuery)
        advanceUntilIdle()

        val errorState = viewModel.uiState.first() as UiError
        assertEquals(errorMessage, errorState.exception)
        verify(searchCharacterByNameUseCase).invoke(searchQuery)
    }

    @Test
    fun `loadCharacterById should update state to Loading then Success with single character`() = testScope.runTest {

        val characterId = 1
        val successResponse = SuccessResponse(
            nextPageUrl = null,
            charactersList = listOf(testCharacterModel)
        )
        whenever(getCharacterByIdUseCase(characterId)).thenReturn(successResponse)
        whenever(uiMapper.toUiModel(testCharacterModel)).thenReturn(testCharacterUiModel)

        viewModel.loadCharacterById(characterId)
        advanceUntilIdle()

        val successState = viewModel.uiState.first() as Success
        assertEquals(listOf(testCharacterUiModel), successState.characters)
        verify(getCharacterByIdUseCase).invoke(characterId)
    }

    @Test
    fun `loadCharacterById should update state to Loading then UiError when character not found`() = testScope.runTest {

        val characterId = 999
        val errorMessage = "Character not found"
        val errorResponse = ErrorResponse(errorMessage)
        whenever(getCharacterByIdUseCase(characterId)).thenReturn(errorResponse)

        viewModel.loadCharacterById(characterId)
        advanceUntilIdle()

        val errorState = viewModel.uiState.first() as UiError
        assertEquals(errorMessage, errorState.exception)
        verify(getCharacterByIdUseCase).invoke(characterId)
    }
}