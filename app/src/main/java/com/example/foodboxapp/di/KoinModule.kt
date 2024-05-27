package com.example.foodboxapp.di

import android.content.Context
import com.example.foodboxapp.backend.CartRepository
import com.example.foodboxapp.backend.CartRepositoryImpl
import com.example.foodboxapp.backend.ProductDataSource
import com.example.foodboxapp.backend.ProductDataSourceImpl
import com.example.foodboxapp.backend.ProductRepository
import com.example.foodboxapp.backend.ProductRepositoryImpl
import com.example.foodboxapp.backend.SessionDataSource
import com.example.foodboxapp.backend.SessionDataSourceImpl
import com.example.foodboxapp.backend.SessionRepository
import com.example.foodboxapp.backend.SessionRepositoryImpl
import com.example.foodboxapp.backend.SettingsDataSource
import com.example.foodboxapp.backend.SettingsDataSourceImpl
import com.example.foodboxapp.backend.SettingsRepository
import com.example.foodboxapp.backend.SettingsRepositoryImpl
import com.example.foodboxapp.backend.StoreDataSource
import com.example.foodboxapp.backend.StoreDataSourceImpl
import com.example.foodboxapp.backend.StoreRepository
import com.example.foodboxapp.backend.StoreRepositoryImpl
import com.example.foodboxapp.backend.settingsPrefsName
import com.example.foodboxapp.viewmodels.CartViewModel
import com.example.foodboxapp.viewmodels.LoginViewModel
import com.example.foodboxapp.viewmodels.MainViewModel
import com.example.foodboxapp.viewmodels.ProductViewModel
import com.example.foodboxapp.viewmodels.SettingsViewModel
import com.example.foodboxapp.viewmodels.StoreViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedPrefsModule = module {
    single {
        androidApplication().getSharedPreferences(settingsPrefsName, Context.MODE_PRIVATE)
    }
}

val dataSourceModule = module {
    includes(sharedPrefsModule)
    single<SessionDataSource> { SessionDataSourceImpl() }
    single<StoreDataSource> { StoreDataSourceImpl() }
    single<ProductDataSource> { ProductDataSourceImpl() }
    single<SettingsDataSource> { SettingsDataSourceImpl(get()) }
}

val repositoryModule = module {
    includes(dataSourceModule)
    single<SessionRepository> { SessionRepositoryImpl(get()) }
    single<StoreRepository> { StoreRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl() }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}

val appModule = module {
    includes(repositoryModule)
    viewModel { MainViewModel(get()) }
    viewModel { ToolbarViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { StoreViewModel(get()) }
    viewModel { ProductViewModel(get(), get()) }
    viewModel { CartViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}