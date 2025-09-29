
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.components.CharacterPhoto
import app.components.StatusState
import model.CharacterUiModel

const val location = "Location: %s"
const val gender = "Gender: %s"
const val specie = "Specie: %s"
const val types = "Type: %s"

@Composable
fun CharacterItem(modifier: Modifier, model: CharacterUiModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            CharacterPhoto(
                modifier = Modifier
                    .height(400.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                imageUrl = model.image
            )
            StatusState(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                model = model
            )
            CharacterItemText(text = String.format(location, model.location.name))
            CharacterItemText(text = String.format(gender, model.gender))
            CharacterItemText(text = String.format(specie, model.species))
            model.type
                .takeIf { it.isNotEmpty() }
                ?.let { CharacterItemText(text = String.format(types, it)) }
        }
    }
}

@Composable
fun CharacterItemText(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center
    )
}