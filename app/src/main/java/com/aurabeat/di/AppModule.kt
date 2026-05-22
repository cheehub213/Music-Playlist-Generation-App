package com.aurabeat.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.aurabeat.data.local.AuraBeatDatabase
import com.aurabeat.data.remote.AuraBeatApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAppContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("aurabeat_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuraBeatDatabase(@ApplicationContext context: Context): AuraBeatDatabase {
        return Room.databaseBuilder(
            context,
            AuraBeatDatabase::class.java,
            "aurabeat.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(sharedPreferences: SharedPreferences): Interceptor {
        return Interceptor { chain ->
            val targetBaseUrl = sharedPreferences.getString("api_base_url", null)?.trim().orEmpty()
            var request = chain.request()

            if (targetBaseUrl.isNotBlank()) {
                val parsedBaseUrl = try {
                    val normalized = if (targetBaseUrl.endsWith("/")) targetBaseUrl else "$targetBaseUrl/"
                    normalized.toHttpUrl()
                } catch (_: IllegalArgumentException) {
                    null
                }

                if (parsedBaseUrl != null) {
                    val originalUrl = request.url
                    val rewrittenUrl = originalUrl.newBuilder()
                        .scheme(parsedBaseUrl.scheme)
                        .host(parsedBaseUrl.host)
                        .port(parsedBaseUrl.port)
                        .encodedPath(parsedBaseUrl.encodedPath.trimEnd('/') + originalUrl.encodedPath)
                        .build()

                    request = request.newBuilder().url(rewrittenUrl).build()
                }
            }

            val token = sharedPreferences.getString("auth_token", null)
            val requestBuilder = request.newBuilder()
            requestBuilder.header("ngrok-skip-browser-warning", "true")
            if (token != null) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(networkInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext context: Context, okHttpClient: OkHttpClient): AuraBeatApiService {
        fun isEmulator(): Boolean {
            return (android.os.Build.FINGERPRINT.startsWith("generic")
                    || android.os.Build.FINGERPRINT.startsWith("unknown")
                    || android.os.Build.MODEL.contains("google_sdk")
                    || android.os.Build.MODEL.contains("Emulator")
                    || android.os.Build.MODEL.contains("Android SDK built for x86"))
        }

        val baseUrl = when {
            isEmulator() -> "http://10.0.2.2:3000/"
            else -> "https://directory-penniless-unthawed.ngrok-free.dev/"
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuraBeatApiService::class.java)
    }
}

