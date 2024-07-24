package com.example.foodboxapp.di

import android.content.Context
import com.example.foodboxapp.backend.data_sources.AcceptedOrdersDataSource
import com.example.foodboxapp.backend.data_sources.AcceptedOrdersDataSourceImpl
import com.example.foodboxapp.backend.data_sources.AccountDataSource
import com.example.foodboxapp.backend.data_sources.AccountDataSourceImpl
import com.example.foodboxapp.backend.data_sources.AvailableOrdersDataSource
import com.example.foodboxapp.backend.data_sources.AvailableOrdersDataSourceImpl
import com.example.foodboxapp.backend.data_sources.CART_PREFS_NAME
import com.example.foodboxapp.backend.data_sources.CartDataSource
import com.example.foodboxapp.backend.data_sources.CartDataSourceImpl
import com.example.foodboxapp.backend.data_sources.OrderDataSource
import com.example.foodboxapp.backend.data_sources.OrderDataSourceImpl
import com.example.foodboxapp.backend.data_sources.ProductDataSource
import com.example.foodboxapp.backend.data_sources.ProductDataSourceImpl
import com.example.foodboxapp.backend.data_sources.SETTINGS_PREFS_NAME
import com.example.foodboxapp.backend.data_sources.SettingsDataSource
import com.example.foodboxapp.backend.data_sources.SettingsDataSourceImpl
import com.example.foodboxapp.backend.data_sources.StoreDataSource
import com.example.foodboxapp.backend.data_sources.StoreDataSourceImpl
import com.example.foodboxapp.backend.network.FoodBoxService
import com.example.foodboxapp.backend.network.FoodBoxServiceImpl
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepositoryImpl
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.AccountRepositoryImpl
import com.example.foodboxapp.backend.repositories.AvailableOrdersRepository
import com.example.foodboxapp.backend.repositories.AvailableOrdersRepositoryImpl
import com.example.foodboxapp.backend.repositories.CartRepository
import com.example.foodboxapp.backend.repositories.CartRepositoryImpl
import com.example.foodboxapp.backend.repositories.OrderRepository
import com.example.foodboxapp.backend.repositories.OrderRepositoryImpl
import com.example.foodboxapp.backend.repositories.ProductRepository
import com.example.foodboxapp.backend.repositories.ProductRepositoryImpl
import com.example.foodboxapp.backend.repositories.SettingsRepository
import com.example.foodboxapp.backend.repositories.SettingsRepositoryImpl
import com.example.foodboxapp.backend.repositories.StoreRepository
import com.example.foodboxapp.backend.repositories.StoreRepositoryImpl
import com.example.foodboxapp.viewmodels.AccountSettingsViewModel
import com.example.foodboxapp.viewmodels.CartViewModel
import com.example.foodboxapp.viewmodels.CheckoutViewModel
import com.example.foodboxapp.viewmodels.LoginViewModel
import com.example.foodboxapp.viewmodels.MainViewModel
import com.example.foodboxapp.viewmodels.ProductViewModel
import com.example.foodboxapp.viewmodels.RegistrationViewModel
import com.example.foodboxapp.viewmodels.SettingsViewModel
import com.example.foodboxapp.viewmodels.StoreViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import com.example.foodboxapp.viewmodels.worker.AcceptedOrdersViewModel
import com.example.foodboxapp.viewmodels.worker.AvailableOrdersViewModel
import com.example.foodboxapp.viewmodels.worker.OrderViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedPrefsModule = module {
    single(named(SETTINGS_PREFS_NAME)) {
        androidApplication().getSharedPreferences(SETTINGS_PREFS_NAME, Context.MODE_PRIVATE)
    }
    single(named(CART_PREFS_NAME)) {
        androidApplication().getSharedPreferences(CART_PREFS_NAME, Context.MODE_PRIVATE)
    }
}

val serviceModule = module {
    single<FoodBoxService>{ FoodBoxServiceImpl() }
}

val dataSourceModule = module {
    includes(sharedPrefsModule)
    includes(serviceModule)
    single<StoreDataSource> { StoreDataSourceImpl(get()) }
    single<ProductDataSource> { ProductDataSourceImpl(get()) }
    single<CartDataSource> { CartDataSourceImpl(get(named(CART_PREFS_NAME))) }
    single<SettingsDataSource> { SettingsDataSourceImpl(get(named(SETTINGS_PREFS_NAME))) }
    single<AccountDataSource> { AccountDataSourceImpl(get()) }
    single<AvailableOrdersDataSource> { AvailableOrdersDataSourceImpl(get()) }
    single<AcceptedOrdersDataSource> { AcceptedOrdersDataSourceImpl(get()) }
    single<OrderDataSource> { OrderDataSourceImpl(get()) }

}

val repositoryModule = module {
    includes(dataSourceModule)
    single<StoreRepository> { StoreRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<AccountRepository> { AccountRepositoryImpl(get()) }
    single<AvailableOrdersRepository> { AvailableOrdersRepositoryImpl(get()) }
    single<AcceptedOrdersRepository> { AcceptedOrdersRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
}

val appModule = module {
    includes(repositoryModule)
    viewModel { MainViewModel(get(), get()) }
    viewModel { ToolbarViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { StoreViewModel(get()) }
    viewModel { ProductViewModel(get(), get(), get(), get()) }
    viewModel { CartViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { CheckoutViewModel(get(), get(), get()) }
    viewModel { AccountSettingsViewModel(get()) }
    viewModel { AvailableOrdersViewModel(get(), get()) }
    viewModel { AcceptedOrdersViewModel(get(), get()) }
    viewModel { OrderViewModel(get()) }
}