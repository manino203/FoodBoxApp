package com.example.foodboxapp.di

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
import com.example.foodboxapp.backend.StoreDataSource
import com.example.foodboxapp.backend.StoreDataSourceImpl
import com.example.foodboxapp.backend.StoreRepository
import com.example.foodboxapp.backend.StoreRepositoryImpl
import com.example.foodboxapp.viewmodels.CartViewModel
import com.example.foodboxapp.viewmodels.LoginViewModel
import com.example.foodboxapp.viewmodels.MainViewModel
import com.example.foodboxapp.viewmodels.ProductViewModel
import com.example.foodboxapp.viewmodels.StoreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<SessionDataSource> { SessionDataSourceImpl() }
    single<SessionRepository> { SessionRepositoryImpl(get()) }

    single<StoreDataSource> { StoreDataSourceImpl() }
    single<StoreRepository> { StoreRepositoryImpl(get()) }

    single<ProductDataSource> { ProductDataSourceImpl() }
    single<ProductRepository> { ProductRepositoryImpl(get()) }

    single<CartRepository> {CartRepositoryImpl()}

    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { StoreViewModel(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { CartViewModel(get()) }
}