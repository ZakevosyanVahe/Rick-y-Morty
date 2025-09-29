package app.ui.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.components.CharacterPhoto
import app.components.ErrorScreen
import app.components.LoadingScreen
import app.components.StatusState
import app.ui.CharacterViewModel
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
    val focusManager = LocalFocusManager.current

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
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier
                            .focusRequester(focusRequester),
                        placeholder = { Text(stringResource(R.string.search)) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        trailingIcon = {
                            IconButton(onClick = { query = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.clear)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = true,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                if (query.isNotEmpty()) {
                                    viewModel.search(query)
                                }
                            }
                        )
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
            StatusState(modifier = Modifier, model = model)
        }
    }
}