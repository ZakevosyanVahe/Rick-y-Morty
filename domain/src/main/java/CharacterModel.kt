data class CharacterModel(
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

sealed class CharactersResponse
data class ErrorResponse(val errorMsg: String?) : CharactersResponse()
data class SuccessResponse(
    val nextPageUrl: String?,
    val charactersList: List<CharacterModel>,
) : CharactersResponse()
