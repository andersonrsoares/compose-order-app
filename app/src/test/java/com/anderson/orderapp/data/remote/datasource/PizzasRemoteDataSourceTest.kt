package com.anderson.orderapp.data.remote.datasource

import br.com.anderson.composefirstlook.ApiUtil
import br.com.anderson.composefirstlook.ApiUtil.mockResponseError
import br.com.anderson.composefirstlook.ApiUtil.mockResponseSuccess
import com.anderson.orderapp.data.remote.dto.PizzaDto
import com.anderson.orderapp.data.remote.network.OrderService
import com.anderson.orderapp.data.remote.result.RemoteDataSourceError
import com.anderson.orderapp.data.remote.result.RemoteDataSourceResult
import com.anderson.orderapp.domain.model.PizzaList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response



class PizzasRemoteDataSourceTest {
    private val mockWebServer = MockWebServer()

    private lateinit var service: OrderService
    @Before
    fun setUp() {
        service = ApiUtil.getApi(mockWebServer, OrderService::class.java)
    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `test success datasource response`() = runBlocking {
        mockWebServer.mockResponseSuccess("pizzas_success.json")

        val datasource = PizzasRemoteDataSourceImpl(service)

        val dataSourceResponse = datasource.fetchPizzas()
        assert(
            dataSourceResponse is RemoteDataSourceResult.Success &&
                    dataSourceResponse.data.firstOrNull { it.name == "Mozzarella" } != null
        )
    }


    @Test
    fun `test error datasource response`() = runBlocking {
        mockWebServer.mockResponseError("", statusCode = 500)

        val datasource = PizzasRemoteDataSourceImpl(service)

        val dataSourceResponse = datasource.fetchPizzas()
        assert(
            dataSourceResponse is RemoteDataSourceResult.Error &&
                    dataSourceResponse.error is RemoteDataSourceError.ServerError
        )
    }

}