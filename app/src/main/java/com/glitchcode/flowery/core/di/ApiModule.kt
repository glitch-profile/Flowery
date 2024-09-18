package com.glitchcode.flowery.core.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import javax.inject.Named

//private const val BASE_URL = "146.120.105.211:8081" //computer
private const val BASE_URL = "192.168.116.121:8081" //notebook with mobile data

val apiModule = module {
    single<HttpClient>(qualifier = named("HttpServer")) {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                url("http://$BASE_URL")
            }
            expectSuccess = true
        }
    }
}