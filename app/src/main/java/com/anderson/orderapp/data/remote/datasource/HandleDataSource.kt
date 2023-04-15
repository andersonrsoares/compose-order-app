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
        else -> RemoteDataSourceError.UnknownError(this)
    }
}

fun <T> Response<T>.handleServerError() : RemoteDataSourceError {
    return when(this.code()){
        401 -> RemoteDataSourceError.Unauthorized(Throwable("Unauthorized code error ${this.code()}"))
        404 -> RemoteDataSourceError.NotFound(Throwable("RemoteDataSourceError code error ${this.code()}"))
        else -> RemoteDataSourceError.UnknownError(Throwable("UnknownError code error ${this.code()}"))
    }
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
