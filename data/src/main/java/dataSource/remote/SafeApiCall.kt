package dataSource.remote

import NetworkException
import NotFoundException
import retrofit2.HttpException

object SafeApiCall {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: HttpException) {
            Result.failure(handleHttpException(e))
        } catch (e: Exception) {
            Result.failure(handleGenericException(e))
        }
    }

    private fun handleHttpException(e: HttpException): Exception {
        return when (e.code()) {
            404 -> NotFoundException("Resource not found", e)
            else -> NetworkException("HTTP error ${e.code()}: ${e.message()}", e)
        }
    }

    private fun handleGenericException(e: Exception): Exception {
        return NetworkException("Unexpected error occurred: ${e.message}", e)
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return SafeApiCall.safeApiCall(apiCall)
}
