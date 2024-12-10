package com.fakhrirasyids.inventapp.di

import com.fakhrirasyids.inventapp.data.db.InventappDatabase
import com.fakhrirasyids.inventapp.data.repo.InventoryRepository
import com.fakhrirasyids.inventapp.ui.addstocks.AddStocksViewModel
import com.fakhrirasyids.inventapp.ui.detail.DetailViewModel
import com.fakhrirasyids.inventapp.ui.editstocks.EditStocksViewModel
import com.fakhrirasyids.inventapp.ui.main.ui.analytics.AnalyticsViewModel
import com.fakhrirasyids.inventapp.ui.main.ui.home.HomeViewModel
import com.fakhrirasyids.inventapp.ui.main.ui.profile.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val databaseModules = module {
    single { InventappDatabase.getInstance(androidContext()) }
}

val repositoryModules = module {
    factory { InventoryRepository(get()) }
}

val viewModelModules = module {
    viewModel { HomeViewModel(get()) }
    viewModel { AnalyticsViewModel(get()) }
    viewModel { ProfileViewModel() }
    viewModel { AddStocksViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { EditStocksViewModel(get()) }
}