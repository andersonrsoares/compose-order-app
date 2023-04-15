package com.anderson.orderapp.domain

import com.anderson.orderapp.R
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

    fun defaultResourceMessage(): Int {
        return when (this) {
            is NetworkIssue -> R.string.error_network_issue
            is GenericError -> R.string.error_unknown
            is ServerError -> {
                when (this.message) {
                    is RemoteDataSourceError.NetworkError -> R.string.error_network_issue
                    is RemoteDataSourceError.ServerError -> R.string.error_server
                    is RemoteDataSourceError.ParseError -> R.string.error_unknown
                }
            }
        }
    }
}
