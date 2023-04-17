package br.com.anderson.composefirstlook

import okio.buffer
import okio.source
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiUtil {

    fun loadfile(fileName: String): String =
        javaClass.classLoader?.getResourceAsStream("api-response/$fileName")?.source()?.buffer()
            ?.readString(Charsets.UTF_8) ?: ""

    fun <T> loadfile(resourceName: String, classOfT: Class<T>): T {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(classOfT).fromJson(loadfile(resourceName))!!
    }

    fun <T> getApi(server: MockWebServer, cclassOfT: Class<T>): T {
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .baseUrl(server.url("/"))
            .client(
                OkHttpClient
                    .Builder()
                    .build()
            )
            .build().create(cclassOfT)
    }


    fun MockWebServer.mockResponseSuccess(
        fileName: String,
        headers: Map<String, String> = emptyMap()
    ) {
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        this.enqueue(
            mockResponse
                .setBody(loadfile(fileName))
                .setResponseCode(200)
        )
    }

    fun MockWebServer.mockResponseError(body: String, statusCode: Int) {
        val mockResponse = MockResponse()
        this.enqueue(
            mockResponse
                .setBody(body)
                .setResponseCode(statusCode)
        )
    }
}