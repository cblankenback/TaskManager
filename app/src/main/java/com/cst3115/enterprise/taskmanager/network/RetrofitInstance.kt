package com.cst3115.enterprise.taskmanager.network

import android.content.Context
import com.cst3115.enterprise.taskmanager.util.TokenProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object for Retrofit instance.
 */
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080" // Emulator's localhost

    /**
     * Creates an OkHttpClient with logging and authentication interceptors.
     *
     * @param context The application context.
     * @return Configured OkHttpClient.
     */
    private fun createOkHttpClient(context: Context): OkHttpClient {
        // Logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Interceptor to add the Authorization header
        val authInterceptor = Interceptor { chain ->
            val token = TokenProvider.getToken(context)
            val request = if (token != null) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    /**
     * Provides the ApiService instance.
     *
     * @param context The application context.
     * @return ApiService instance.
     */
    fun getApiService(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
