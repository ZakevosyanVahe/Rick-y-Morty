package app.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest

@Composable
fun CharacterPhoto(modifier: Modifier = Modifier, imageUrl: String) {
    val imageLoader = rememberImageLoader()
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .diskCacheKey(imageUrl)
            .memoryCacheKey(imageUrl)
            .build(),
        contentDescription = "Character image",
        imageLoader = imageLoader,

        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current

    return remember {
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("coil_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .respectCacheHeaders(false)
            .crossfade(true)
            .build()
    }
}