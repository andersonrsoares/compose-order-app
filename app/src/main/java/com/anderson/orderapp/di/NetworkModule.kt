package com.anderson.orderapp.di

import com.anderson.orderapp.data.remote.network.OrderService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val  BASE_URL = "https://static.mozio.com/mobile/"

val NetworkModule = module {
    single { provideHTTPLoggingInterceptor() }

    single { provideOkHttpClient(get()) }

    single { provideMoshi() }

    single { provideRetrofit(get(), get()) }

    single { provideOrderService(get()) }
}

private fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return interceptor;
}

private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun provideMoshi(): Moshi {
    return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()
}


private fun provideOrderService(retrofit: Retrofit): OrderService {
    return retrofit.create(OrderService::class.java);
}
