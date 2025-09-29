package di

import app.mappers.CharacterUiMapper
import app.ui.CharacterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        CharacterViewModel(
            getCharacterUseCase = get(),
            getCharacterByIdUseCase = get(),
            loadNextPageUseCase = get(),
            searchCharacterByNameUseCase = get(),
            uiMapper = get()
        )
    }

    single { CharacterUiMapper() }
}