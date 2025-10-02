package app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import app.util.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appakk.R

@Composable
fun CharacterPhoto(modifier: Modifier = Modifier, imageUrl: String) {
    val context = LocalContext.current
    val imageLoader = remember { ImageLoader.imageLoader }
    
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .diskCacheKey(imageUrl)
            .memoryCacheKey(imageUrl)
            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
            .diskCachePolicy(coil.request.CachePolicy.ENABLED)
            .build(),
        contentDescription = stringResource(R.string.character_image),
        imageLoader = imageLoader,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}