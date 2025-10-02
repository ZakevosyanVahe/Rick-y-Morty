import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.ui.components.CharacterPhoto
import app.ui.components.StatusState
import model.CharacterUiModel

@Composable
fun CharacterItem(modifier: Modifier, model: CharacterUiModel) {

    Column(
        modifier = modifier
    ) {
        CharacterPhoto(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageUrl = model.image
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(4.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            StatusState(model = model)
        }
    }
}