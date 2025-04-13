package org.example.project.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.example.project.data.VerbRepositoryImpl
import org.example.project.data.dao.DatabaseFactory
import org.example.project.data.dao.VerbDataBase
import org.example.project.data.local.LocalIrregularVerbsDataSourceImpl
import org.example.project.domain.LocalIrregularVerbsDataSource
import org.example.project.domain.VerbRepository
import org.example.project.domain.usecase.GetVerbListUseCase
import org.example.project.domain.usecase.GetVerbListUseCaseImpl
import org.example.project.domain.usecase.SaveIsDoneVerbUseCase
import org.example.project.domain.usecase.SaveIsDoneVerbUseCaseImpl
import org.example.project.domain.usecase.UpdateLocalVerbsUseCase
import org.example.project.domain.usecase.UpdateLocalVerbsUseCaseImpl
import org.example.project.feature.irregularverbs.GameVerbViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver()).build()
    }
    single { get<VerbDataBase>().verbDao }


    singleOf(::LocalIrregularVerbsDataSourceImpl).bind<LocalIrregularVerbsDataSource>()
    singleOf(::VerbRepositoryImpl).bind<VerbRepository>()

    singleOf(::GetVerbListUseCaseImpl).bind<GetVerbListUseCase>()
    singleOf(::SaveIsDoneVerbUseCaseImpl).bind<SaveIsDoneVerbUseCase>()
    singleOf(::UpdateLocalVerbsUseCaseImpl).bind<UpdateLocalVerbsUseCase>()


    viewModelOf(::GameVerbViewModel)
}