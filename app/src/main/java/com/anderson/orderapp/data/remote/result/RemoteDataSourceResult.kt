package com.anderson.orderapp.data.remote.result

sealed class RemoteDataSourceResult<T> {
    class Success<T>(val data: T): RemoteDataSourceResult<T>()
    class Error<T>(val error: RemoteDataSourceError): RemoteDataSourceResult<T>()
}

