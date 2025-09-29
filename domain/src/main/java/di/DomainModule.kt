package di

import org.koin.dsl.module
import usaCase.GetCharacterByIdUseCase
import usaCase.GetCharactersUseCase
import usaCase.LoadNextPageUseCase
import usaCase.SearchCharacterByNameUseCase

val domainModule = module {
    factory { GetCharactersUseCase(get()) }
    factory { GetCharacterByIdUseCase(get()) }
    factory { SearchCharacterByNameUseCase(get()) }
    factory { LoadNextPageUseCase(get()) }

}