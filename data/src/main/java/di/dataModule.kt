package di

import dataSource.remote.CharacterRemoteDataSource
import mappers.CharacterMapper
import org.koin.dsl.module
import repositories.CharacterRepositoryImpl
import repository.CharacterRepository

val dataModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
}

val dataSourceModule = module {
    single { CharacterRemoteDataSource(get()) }
}

val mapperModule = module {
    single { CharacterMapper() }
}



