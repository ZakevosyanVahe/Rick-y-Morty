package app.ui.searchPage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.ui.CharacterViewModel
import app.ui.components.ErrorScreen
import app.ui.components.LoadingScreen
import com.example.appakk.R
import model.CharacterUiModel
import model.Idle
import model.Loading
import model.Success
import model.UiError
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterSearchScreen(navController: NavHostController) {

    val viewModel: CharacterViewModel = koinViewModel()
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = {
                    SearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = viewModel::search,
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        val uiState = viewModel.uiState.collectAsState()
        when (uiState.value) {
            is UiError -> {
                ErrorScreen(errorMsg = (uiState.value as UiError).exception)
            }

            Loading -> {
                LoadingScreen()
            }

            is Success -> {
                val characters = (uiState.value as Success).characters
                SearchResultContent(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    itemList = characters
                ) {
                    navController.navigate("detail/${it}")
                }
            }

            Idle -> Unit
        }
    }
}

@Composable
fun SearchResultContent(
    modifier: Modifier,
    itemList: List<CharacterUiModel>,
    onIemClick: (Int) -> Unit
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        items(itemList.size, { index -> itemList[index].id }) {
            SearchItem(itemList[it], onIemClick = onIemClick)
        }
    }
}