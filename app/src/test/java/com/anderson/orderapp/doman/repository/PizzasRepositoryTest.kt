package com.anderson.orderapp.doman.repository

import br.com.anderson.composefirstlook.ApiUtil
import com.anderson.orderapp.DispatcherProvider
import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSource
import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSourceImpl
import com.anderson.orderapp.data.remote.result.RemoteDataSourceResult
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.PizzaList
import com.anderson.orderapp.domain.repository.PizzaRepositoryImpl
import com.anderson.orderapp.domain.repository.PizzasRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PizzasRepositoryTest {

    private val dispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = testDispatcher
        override val io: CoroutineDispatcher
            get() = testDispatcher
        override val default: CoroutineDispatcher
            get() = testDispatcher
        override val unconfined: CoroutineDispatcher
            get() = testDispatcher
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private val testScope = TestScope(testDispatcher)

    private val remoteDataSource = mockk<PizzasRemoteDataSource>()

    @Test
    fun `test success fetch pizzas`() = testScope.runTest {

        val pizzasContent = ApiUtil.loadfile(
            "pizzas_success.json",
            PizzaList::class.java
        )

        coEvery { remoteDataSource.fetchPizzas() } returns RemoteDataSourceResult.Success(
            pizzasContent
        )

        val pizzasRepository = PizzaRepositoryImpl(
            dispatcherProvider = dispatcherProvider,
            pizzasRemoteDataSource = remoteDataSource
        )

        val flow =  pizzasRepository.fetchPizzas()


        assert(flow.first() is DataState.Loading)

        val successItem = flow.last()
        assert(successItem is DataState.Success && successItem.data.find { it.name == "Mozzarella"  } != null)


        coVerify (
            exactly = 1
        ) {
            remoteDataSource.fetchPizzas()
        }
    }
}