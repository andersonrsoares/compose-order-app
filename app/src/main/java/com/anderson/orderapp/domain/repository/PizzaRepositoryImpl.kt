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

            emit(when(val result = pizzasRemoteDataSource.fetchPizzas()) {
                is RemoteDataSourceResult.Success -> {
                    DataState.Success(result.data.map { it.toPizza() })
                }

                is RemoteDataSourceResult.Error -> DataState.Failure(when(result.error) {
                    is RemoteDataSourceError.NotFound,
                    is RemoteDataSourceError.Unauthorized -> FailureReason.ServerError(result.error)
                    is RemoteDataSourceError.NetworkError,
                    is RemoteDataSourceError.ParseError -> FailureReason.NetworkIssue
                    else -> FailureReason.GenericError
                })
            })
        }.flowOn(dispatcherProvider.io)
    }
}