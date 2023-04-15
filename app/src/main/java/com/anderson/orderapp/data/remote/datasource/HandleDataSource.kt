package com.anderson.orderapp.data.remote.datasource


import com.anderson.orderapp.data.remote.result.RemoteDataSourceError
import com.anderson.orderapp.data.remote.result.RemoteDataSourceResult
import kotlinx.coroutines.TimeoutCancellationException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


fun <T> Response<T>.handleResponse() : RemoteDataSourceResult<T> {
    return if (this.isSuccessful) {
        RemoteDataSourceResult.Success(this.body()!!)
    } else {
        RemoteDataSourceResult.Error(this.handleServerError())
    }
}

fun Throwable.handleException(): RemoteDataSourceError {
    return when(this){
        is UnknownHostException,
        is TimeoutException,
        is TimeoutCancellationException,
        is IOException -> RemoteDataSourceError.NetworkError(this)
        is JSONException -> RemoteDataSourceError.ParseError(this)
        else -> RemoteDataSourceError.ServerError(this)
    }
}

fun <T> Response<T>.handleServerError() : RemoteDataSourceError {
    return RemoteDataSourceError.ServerError(Throwable("UnknownError error code: ${this.code()} error body: ${this.errorBody()?.string()}"))
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
) : RemoteDataSourceResult<T> {
    return runCatching {
        apiCall.invoke().handleResponse()
    }.getOrElse {
        RemoteDataSourceResult.Error(it.handleException())
    }
}
