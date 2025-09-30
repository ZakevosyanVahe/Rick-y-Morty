package app.ui.detail

import CharacterItem
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import app.components.ErrorScreen
import app.components.LoadingScreen
import app.ui.CharacterViewModel
import model.Idle
import model.Loading
import model.Success
import model.UiError
import org.koin.compose.viewmodel.koinViewModel

const val location = "Location: %s"
const val gender = "Gender: %s"
const val specie = "Specie: %s"
const val types = "Type: %s"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(id: Int, navController: NavHostController) {

    var menuExpanded by remember { mutableStateOf(false) }
    val viewModel: CharacterViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCharacterById(id)
    }

    when (uiState.value) {
        is UiError -> {
            ErrorScreen(errorMsg = (uiState.value as UiError).exception)
        }

        Loading -> {
            LoadingScreen()
        }

        is Success -> {
            val model = (uiState.value as Success).characters.first()
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text(model.name) },
                    navigationIcon = {
                        IconButton({ navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.Info, contentDescription = "Character info")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    ItemText(text = String.format(location, model.location.name))
                                },
                                onClick = { menuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = {
                                    ItemText(text = String.format(gender, model.gender))
                                },
                                onClick = { menuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = {
                                    ItemText(text = String.format(specie, model.species))
                                },
                                onClick = { menuExpanded = false }
                            )
                            model.type
                                .takeIf { it.isNotEmpty() }
                                ?.let {
                                    DropdownMenuItem(
                                        text = {
                                            ItemText(text = String.format(types, it))
                                        },
                                        onClick = { menuExpanded = false }
                                    )
                                }
                        }
                    }
                )
            }) { paddingValues ->
                CharacterItem(modifier = Modifier.padding(paddingValues), model)
            }
        }

        Idle -> Unit
    }
}

@Composable
fun ItemText(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}
