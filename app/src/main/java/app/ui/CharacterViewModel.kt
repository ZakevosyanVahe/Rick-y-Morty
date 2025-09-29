package app.ui

import ErrorResponse
import SuccessResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mappers.CharacterUiMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Idle
import model.Loading
import model.Success
import model.UiError
import model.UiState
import usaCase.GetCharacterByIdUseCase
import usaCase.GetCharactersUseCase
import usaCase.LoadNextPageUseCase
import usaCase.SearchCharacterByNameUseCase

class CharacterViewModel(
    private val getCharacterUseCase: GetCharactersUseCase,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val loadNextPageUseCase: LoadNextPageUseCase,
    private val searchCharacterByNameUseCase: SearchCharacterByNameUseCase,
    private val uiMapper: CharacterUiMapper
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(Idle)
    val uiState: StateFlow<UiState> = _uiState

    private var nextPageUrl: String? = null


    fun loadCharacters() {
        _uiState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            val charactersResponse = getCharacterUseCase()
            when (charactersResponse) {
                is ErrorResponse -> {
                    _uiState.value = UiError(charactersResponse.errorMsg ?: "unknown error")
                }

                is SuccessResponse -> {
                    _uiState.value = Success(uiMapper.toUiModel(charactersResponse.charactersList))
                    nextPageUrl = charactersResponse.nextPageUrl
                }
            }

        }
    }

    fun loadNextPage() {
        val currentState = _uiState.value
        val currentCharacters = when (currentState) {
            is Success -> {
                currentState.characters
            }

            else -> emptyList()
        }
        if (currentState !is Success) return
        viewModelScope.launch(Dispatchers.IO) {
            nextPageUrl?.let {
                val charactersResponse = loadNextPageUseCase(it)
                when (charactersResponse) {
                    is ErrorResponse -> {
                        _uiState.value = UiError(charactersResponse.errorMsg ?: "unknown error")
                    }
                    is SuccessResponse -> {
                        _uiState.value =
                            Success(currentCharacters + uiMapper.toUiModel(charactersResponse.charactersList))
                        nextPageUrl = charactersResponse.nextPageUrl
                    }
                }
            }
        }
    }

    fun search(keyword: String) {
        _uiState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            val charactersResponse = searchCharacterByNameUseCase(keyword)
            when (charactersResponse) {
                is ErrorResponse -> {
                    _uiState.value = UiError(charactersResponse.errorMsg ?: "unknown error")
                }
                is SuccessResponse -> {
                    _uiState.value = Success(uiMapper.toUiModel(charactersResponse.charactersList))
                    nextPageUrl = charactersResponse.nextPageUrl
                }
            }
        }
    }

    fun loadCharacterById(id: Int) {
        _uiState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            val charactersResponse = getCharacterByIdUseCase(id)
            when (charactersResponse) {
                is ErrorResponse -> {
                    _uiState.value = UiError(charactersResponse.errorMsg ?: "unknown error")
                }
                is SuccessResponse -> {
                    _uiState.value =
                        Success(listOf(uiMapper.toUiModel(charactersResponse.charactersList.first())))
                }
            }
        }
    }
}