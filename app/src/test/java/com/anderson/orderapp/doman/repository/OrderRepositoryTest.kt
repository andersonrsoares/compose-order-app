@file:OptIn(ExperimentalCoroutinesApi::class)
package com.anderson.orderapp.doman.repository


import com.anderson.orderapp.data.local.datasource.OrderLocalDataSource
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.OrderRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.UUID

class OrderRepositoryTest {



    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private val testScope = TestScope(testDispatcher)

    private val datasource = mockk<OrderLocalDataSource>()

    @Test
    fun `test save`() = testScope.runTest {


        val id = UUID.randomUUID()
        coEvery { datasource.save(any()) } returns id

        val pizzasRepository = OrderRepositoryImpl(
            datasource
        )

        val flow =  pizzasRepository.save(arrayListOf(Pizza("test", 1.10)))


        assert(flow.first() == id)

        coVerify (
            exactly = 1
        ) {
            datasource.save(arrayListOf(Pizza("test", 1.10)))
        }
    }

    @Test
    fun `test clean`() = testScope.runTest {


        val id = UUID.randomUUID()
        coEvery { datasource.clean(id) } returns true

        val pizzasRepository = OrderRepositoryImpl(
            datasource
        )

        val flow =  pizzasRepository.clean(id)


        assert(flow.first())

        coVerify (
            exactly = 1
        ) {
            datasource.clean(id)
        }
    }

    @Test
    fun `test fetch`() = testScope.runTest {

        val list = arrayListOf(Pizza("test", 1.10))
        val id = UUID.randomUUID()
        coEvery { datasource.fetch(id) } returns list

        val pizzasRepository = OrderRepositoryImpl(
            datasource
        )

        val flow =  pizzasRepository.fetch(id)


        assert(flow.first() == list)

        coVerify (
            exactly = 1
        ) {
            datasource.fetch(id)
        }
    }
}