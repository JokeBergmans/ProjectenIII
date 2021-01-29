package com.example.damiaanapp.DI


import com.example.damiaanapp.BuildConfig
import com.example.damiaanapp.data.local.*
import com.example.damiaanapp.data.remote.*
import com.example.damiaanapp.repos.*
import com.example.damiaanapp.util.DateAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

// Authors: Joke Bergmans, Thibaud Steenhaut, Tom Van der Weeën
val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), BuildConfig.BASE_URL) }
    single {
        provideApiService(get())
    }

    single { AppDatabase.getDatabase(androidApplication()).registrationDao() }
    single { RegistrationRemoteDataSource(get()) }
    single { RegistrationLocalDataSource(get(), get(), get()) }
    single { RegistrationRepository(get(), get()) }

    single { AppDatabase.getDatabase(androidApplication()).participantDao() }
    single { ParticipantRemoteDataSource(get()) }
    single { ParticipantLocalDataSource(get()) }
    single { ParticipantRepository(get(), get()) }

    single { AppDatabase.getDatabase(androidApplication()).routeDao() }
    single { RouteLocalDataSource(get()) }
    single { RouteRepository(get()) }

    single { AppDatabase.getDatabase(androidApplication()).nodeDao() }
    single { NodeLocalDataSource(get()) }
    single { NodeScanRemoteDataSource(get()) }
    single { NodeRepository(get(), get()) }

    single { AccountRemoteDataSource(get()) }
    single { AccountRepository(get()) }
}


private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).add(DateAdapter()).build()
            )
        )
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

/**
 * Provide the API service
 */
private fun provideApiService(retrofit: Retrofit): DamiaanService =
    retrofit.create(DamiaanService::class.java)

