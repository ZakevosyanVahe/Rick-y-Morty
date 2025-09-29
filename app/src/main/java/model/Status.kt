package model

sealed class UiState
data class Success(val characters: List<CharacterUiModel>) : UiState()
data class UiError(val exception: String) : UiState()
object Loading : UiState()
object Idle : UiState()