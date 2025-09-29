package dataSource.models

import com.google.gson.annotations.SerializedName

data class CharacterApiModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val origin: RmLocation,
    @SerializedName("location")
    val location: RmLocation,
    @SerializedName("image")
    val image: String,
    @SerializedName("episode")
    val episode: List<String> = emptyList(),
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val created: String
)
data class RmLocation(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)