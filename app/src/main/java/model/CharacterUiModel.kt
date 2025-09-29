package model

data class CharacterUiModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String = "",
    val gender: String,
    val origin: RmLocation,
    val location: RmLocation,
    val image: String,
    val episode: List<String> = emptyList(),
    val url: String,
    val created: String
)

data class RmLocation(
    val name: String,
    val url: String
)
