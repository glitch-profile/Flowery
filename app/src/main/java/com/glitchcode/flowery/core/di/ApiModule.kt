package com.glitchcode.flowery.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "146.120.105.211:8081" //computer

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    @Named("RestClient")
    fun provideRestApiKtorClient(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                encodeDefaults = true
                ignoreUnknownKeys = true
            })
        }
        expectSuccess = true
        install(DefaultRequest) {
            url ("http://$BASE_URL")
        }
    }

}