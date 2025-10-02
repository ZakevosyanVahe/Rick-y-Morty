package app.util

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImageLoader {
    lateinit var appContext: Context
    val imageLoader: ImageLoader by lazy {
        buildImageLoader(appContext)
    }

    private fun buildImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.4)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("coil_cache"))
                    .maxSizePercent(0.05) // 5% disk cache
                    .build()
            }
            .respectCacheHeaders(false)
            .crossfade(true)
            .crossfade(200)
            .allowHardware(true)
            .build()
    }

    fun preloadImages(context: Context, imageUrls: List<String>, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            imageUrls.forEach { imageUrl ->
                try {
                    val request = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .diskCacheKey(imageUrl)
                        .memoryCacheKey(imageUrl)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build()

                    imageLoader.enqueue(request)
                } catch (e: Exception) {
                }
            }
        }
    }
}