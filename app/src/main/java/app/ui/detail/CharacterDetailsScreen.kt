package app.ui.detail

import CharacterItem
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import app.components.ErrorScreen
import app.components.LoadingScreen
import app.ui.CharacterViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(id: Int, navController: NavHostController) {

    val viewModel: CharacterViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCharacterById(id)
    }

    when (uiState.value) {
        is CharacterViewModel.UiError -> {
            ErrorScreen(errorMsg = (uiState.value as CharacterViewModel.UiError).exception)
        }
        CharacterViewModel.Loading -> {
            LoadingScreen()
        }
        is CharacterViewModel.Success -> {
            val model = (uiState.value as CharacterViewModel.Success).characters.first()
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
                    }
                )
            }) { paddingValues ->
                CharacterItem(modifier = Modifier.padding(paddingValues), model)
            }
        }
        CharacterViewModel.Idle -> Unit
    }
}
