package com.bitcoin.ticker.di

import com.bitcoin.ticker.BuildConfig
import com.bitcoin.ticker.data.rest.CryptoApisService
import com.bitcoin.ticker.data.rest.CryptoRemoteDataSource
import com.bitcoin.ticker.data.rest.ServiceUrls.BASE_URL
import com.bitcoin.ticker.data.rest.ServiceUrls.APIKEY
import com.bitcoin.ticker.util.CustomHttpLogger
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModel {

    @Provides
    @Singleton
    fun provideHttpClient() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor(CustomHttpLogger()).apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder().apply {
            networkInterceptors().add(Interceptor { chain ->

                val jwt = APIKEY
                val original = chain.request()
                val request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("X-API-Key", jwt)
                        .method(original.method(), original.body())
                        .build()
                chain.proceed(request)
            })

            addInterceptor(interceptor)
        }

        return client.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson,httpClient : OkHttpClient) : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideCryptoApisService(retrofit: Retrofit): CryptoApisService = retrofit.create(CryptoApisService::class.java)

    @Singleton
    @Provides
    fun provideCryptoApisRemoteDataSource(characterService: CryptoApisService) = CryptoRemoteDataSource(characterService)


}