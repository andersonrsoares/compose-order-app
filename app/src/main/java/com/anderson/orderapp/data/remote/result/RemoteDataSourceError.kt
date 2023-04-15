package com.anderson.orderapp.data.remote.result

sealed class RemoteDataSourceError(error: Throwable?) {
    class NetworkError(error: Throwable?) : RemoteDataSourceError(error)
    class Unauthorized(error: Throwable?) : RemoteDataSourceError(error)
    class NotFound(error: Throwable?)  : RemoteDataSourceError(error)
    class ParseError(error: Throwable?) : RemoteDataSourceError(error)
    class UnknownError(error: Throwable?) : RemoteDataSourceError(error)
}