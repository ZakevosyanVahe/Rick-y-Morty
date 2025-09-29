package dataSource.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ApiResponse<T>(
    @SerializedName("results")
    val results: T,
    @SerializedName("info")
    val info: ApiInfo,
)

@Serializable
data class ApiInfo(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String? = null,
    @SerializedName("prev") val prev: String? = null
)