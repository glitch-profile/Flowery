package com.glitchcode.flowery.core

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.glitchcode.flowery.core.di.apiModule
import com.glitchcode.flowery.core.di.repositoryModule
import com.glitchcode.flowery.core.di.useCasesModule
import com.glitchcode.flowery.core.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

private const val MAX_DISK_CACHE_SIZE_BYTES = 52428800L // 50MB in bytes

class App: Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                apiModule,
                repositoryModule,
                useCasesModule,
                viewModelsModule
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .diskCachePolicy(CachePolicy.DISABLED)
//            .diskCachePolicy(CachePolicy.ENABLED)
//            .diskCache {
//                DiskCache.Builder()
//                    .maxSizeBytes(MAX_DISK_CACHE_SIZE_BYTES)
//                    .build()
//            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .build()
            }
            .build()
    }
}