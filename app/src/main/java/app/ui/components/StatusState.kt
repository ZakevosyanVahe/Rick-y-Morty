package app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.appakk.R
import model.CharacterStatus
import model.CharacterUiModel

@Composable
fun StatusState(modifier: Modifier = Modifier, model: CharacterUiModel) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (model.status) {
                CharacterStatus.ALIVE.value,
                CharacterStatus.DEAD.value,
                CharacterStatus.UNKNOWN.value -> Icons.Filled.CheckCircle

                else -> Icons.Default.CheckCircle
            },
            contentDescription = when (model.status) {
                CharacterStatus.ALIVE.value -> stringResource(R.string.alive)
                CharacterStatus.DEAD.value -> stringResource(R.string.dead)
                CharacterStatus.UNKNOWN.value -> stringResource(R.string.unknown)
                else -> ""
            },
            tint = when (model.status) {
                CharacterStatus.ALIVE.value -> Color.Green
                CharacterStatus.DEAD.value -> Color.Red
                CharacterStatus.UNKNOWN.value -> Color.Gray
                else -> Color.Unspecified
            },
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = model.status,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
    }
}