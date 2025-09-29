package app.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import app.components.ErrorScreen
import app.components.LoadingScreen
import app.ui.CharacterViewModel
import com.example.appakk.R
import model.Idle
import model.Loading
import model.Success
import model.UiError
import model.UiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    navController: NavHostController,
    viewModel: CharacterViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadCharacters()
    }
    val listState = rememberLazyListState()
    var characterListSize by remember { mutableIntStateOf(10) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null) {
                    if (lastVisibleIndex >= characterListSize - 3) {
                        viewModel.loadNextPage()
                    }
                }
            }
    }
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                }
            )
        }) {
        Content(navController, listState, uiState.value, paddingValues = it) {
            characterListSize = it
        }
    }
}

@Composable
private fun Content(
    navController: NavController,
    listState: LazyListState,
    uiState: UiState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    onSuccess: (itemSize: Int) -> Unit
) {
    when (uiState) {
        is UiError -> {
            ErrorScreen(errorMsg = uiState.exception)
        }
        Loading -> {
            LoadingScreen()
        }

        is Success -> {
            val characterList = uiState.characters
            onSuccess.invoke(characterList.size)

            LazyColumn(
                state = listState,
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = characterList.size,
                    key = { index -> characterList[index].id }
                ) {
                    val item = characterList[it]
                    CharacterItem(
                        modifier,
                        item,
                    ) {
                        navController.navigate("detail/${item.id}")
                    }
                }
            }
        }

        Idle -> Unit
    }
}