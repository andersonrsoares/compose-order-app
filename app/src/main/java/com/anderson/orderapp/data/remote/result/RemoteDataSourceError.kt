package com.anderson.orderapp.data.remote.result

sealed class RemoteDataSourceError(val exception: Throwable?) {
    class ServerError(exception: Throwable?) : RemoteDataSourceError(exception)
    class NetworkError(exception: Throwable?) : RemoteDataSourceError(exception)
    class ParseError(exception: Throwable?) : RemoteDataSourceError(exception)
}