package com.anderson.orderapp.data.remote.datasource

sealed class RemoteDataSourceResult<T> {
    class Success<T>(val data: T): RemoteDataSourceResult<T>()
    class Error<T>(val error: RemoteDataSourceError): RemoteDataSourceResult<T>()
}

sealed class RemoteDataSourceError {
    object NetworkError : RemoteDataSourceError()
    class Unauthorized(var message: String): RemoteDataSourceError()
    class NotFound(var message: String): RemoteDataSourceError()
    object UnknownError: RemoteDataSourceError()
}