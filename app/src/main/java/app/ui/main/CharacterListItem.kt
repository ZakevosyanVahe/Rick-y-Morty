package app.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(0.5.dp, color = Color.LightGray)
            .clickable {
                onClicked?.invoke(model.id)
            }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            CharacterPhoto(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clip(RoundedCornerShape(8.dp)),
                model.image
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
                    .weight(2f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 0.1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        //.align(Alignment.CenterHorizontally),
                        text = model.name,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        //.align(Alignment.CenterHorizontally),
                        text = "Location: ${model.location.name}",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    StatusState(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        model = model
                    )
                }
            }
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