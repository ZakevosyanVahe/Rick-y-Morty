package app.ui.searchPage

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.ui.components.CharacterPhoto
import app.ui.components.StatusState
import model.CharacterUiModel

@Composable
fun SearchItem(model: CharacterUiModel, onIemClick: (id: Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onIemClick.invoke(model.id)
            }
            .padding(4.dp)
            .border(
                width = 0.5.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CharacterPhoto(
            modifier = Modifier
                .size(50.dp)
                .padding(4.dp)
                .clip(CircleShape),
            imageUrl = model.image
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(), text = model.name
            )
            StatusState(
                model = model
            )
        }
    }
}