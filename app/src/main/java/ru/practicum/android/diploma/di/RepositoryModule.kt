package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.FavoriteRepositoryImpl
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository

val repositoryModule = module {

    single<VacancyRepository> {
        VacanciesRepositoryImpl(get())
    }
    factory<FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }
}
