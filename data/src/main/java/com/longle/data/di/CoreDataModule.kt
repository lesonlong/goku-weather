package com.longle.data.di

import android.app.Application
import com.google.gson.Gson
import com.longle.data.BuildConfig
import com.longle.data.api.ApiService
import com.longle.data.db.AppDatabase
import com.longle.data.util.RateLimiter
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger module to provide core data functionality.
 */
@Module
class CoreDataModule {

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()

    @Singleton
    @Provides
    fun provideRateLimiter() = RateLimiter(5, TimeUnit.SECONDS)

    @Singleton
    @Provides
    fun provideApiService(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okHttpClient, converterFactory, ApiService::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val certificatePinner = CertificatePinner.Builder()
            .add(ApiService.DOMAIN, ApiService.PIN1)
            .add(ApiService.DOMAIN, ApiService.PIN2)
            .add(ApiService.DOMAIN, ApiService.PIN3)
            .build()

        return OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) BODY else NONE }

    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        clazz: Class<T>
    ): T {
        return createRetrofit(okHttpClient, converterFactory).create(clazz)
    }

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiService.ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}
