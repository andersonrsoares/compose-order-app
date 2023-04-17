package com.anderson.orderapp.domain.repository

import com.anderson.orderapp.DispatcherProvider
import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSource
import com.anderson.orderapp.data.remote.result.RemoteDataSourceError
import com.anderson.orderapp.data.remote.result.RemoteDataSourceResult
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.FailureReason
import com.anderson.orderapp.domain.mapper.toPizza
import com.anderson.orderapp.domain.model.Pizza
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PizzaRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val pizzasRemoteDataSource: PizzasRemoteDataSource
): PizzasRepository {
    override fun fetchPizzas(): Flow<DataState<List<Pizza>>> {
        return flow {
            emit(DataState.Loading())
            when(val result = pizzasRemoteDataSource.fetchPizzas()) {
                is RemoteDataSourceResult.Success -> {
                   emit(DataState.Success(result.data.map { it.toPizza() }))
                }

                is RemoteDataSourceResult.Error -> {
                    result.error.exception?.printStackTrace()
                    val error = when(result.error) {
                        is RemoteDataSourceError.ServerError -> FailureReason.ServerError(result.error)
                        is RemoteDataSourceError.NetworkError,
                        is RemoteDataSourceError.ParseError -> FailureReason.NetworkIssue
                    }
                    emit(DataState.Failure(error))
                }
            }
        }.flowOn(dispatcherProvider.io)
    }
}