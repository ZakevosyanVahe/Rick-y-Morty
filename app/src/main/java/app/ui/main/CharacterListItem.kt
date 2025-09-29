package app.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.components.CharacterPhoto
import app.components.StatusState
import model.CharacterUiModel
import model.RmLocation

const val location = "Last known location: %s"

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    model: CharacterUiModel,
    onClicked: ((id: Int) -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                width = 0.5.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
            .clickable {
                onClicked?.invoke(model.id)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title (centered)
            Text(
                text = model.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            StatusState(modifier, model)

            CharacterPhoto(modifier.clip(RoundedCornerShape(8.dp)), model.image)

            Text(
                modifier = Modifier.padding(8.dp),
                text = String.format(location, model.location.name),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun CharacterItemPreview() {
    MaterialTheme {
        val model = CharacterUiModel(
            name = "Name",
            id = 1,
            status = "Alive",
            species = "",
            type = "",
            gender = "",
            origin = RmLocation(
                name = "LocationName",
                url = "https://rickandmortyapi.com/api/ubicación/1"
            ),
            location = RmLocation(
                name = "LocationName",
                url = "https://rickandmortyapi.com/api/ubicación/1"
            ),
            image = "https://rickandmortyapi.com/api/character/avatar/82.jpeg",
            episode = emptyList(),
            url = "",
            created = "",
        )
        CharacterItem(model = model) {}
    }
}