package com.anderson.orderapp.domain

import com.anderson.orderapp.data.remote.result.RemoteDataSourceError


sealed class DataState<T> {
    class Success<T>(val data: T): DataState<T>()
    class Loading<T>: DataState<T>()
    class Failure<T>(val reason: FailureReason): DataState<T>()
}


sealed class FailureReason {
    object NetworkIssue : FailureReason()
    class ServerError(val message: RemoteDataSourceError): FailureReason()
    object GenericError: FailureReason()
}
