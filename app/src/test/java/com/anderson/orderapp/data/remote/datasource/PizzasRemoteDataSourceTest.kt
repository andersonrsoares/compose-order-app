package com.anderson.orderapp.data.remote.datasource

import br.com.anderson.composefirstlook.ApiUtil
import com.anderson.orderapp.data.remote.dto.PizzaDto
import com.anderson.orderapp.data.remote.network.OrderService
import com.anderson.orderapp.data.remote.result.RemoteDataSourceError
import com.anderson.orderapp.data.remote.result.RemoteDataSourceResult
import com.anderson.orderapp.domain.model.PizzaList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response



class PizzasRemoteDataSourceTest {
    private val service = mockk<OrderService>()

    @Test
    fun `test success datasource response`() = runBlocking {
        val response = Response.success<List<PizzaDto>>(
            ApiUtil.loadfile<List<PizzaDto>>(
                "pizzas_success.json",
                PizzaList::class.java
            )
        )

        coEvery { service.pizzas() } returns response

        val datasource = PizzasRemoteDataSourceImpl(service)

        val dataSourceResponse = datasource.fetchPizzas()
        assert(
            dataSourceResponse is RemoteDataSourceResult.Success &&
                    dataSourceResponse.data.firstOrNull { it.name == "Mozzarella" } != null
        )
    }


    @Test
    fun `test error datasource response`() = runBlocking {
        val response = Response.error<List<PizzaDto>>(500, "<>".toResponseBody())
        coEvery { service.pizzas() } throws  HttpException(response)
        val datasource = PizzasRemoteDataSourceImpl(service)

        val dataSourceResponse = datasource.fetchPizzas()
        assert(
            dataSourceResponse is RemoteDataSourceResult.Error &&
                    dataSourceResponse.error is RemoteDataSourceError.ServerError
        )
    }

}