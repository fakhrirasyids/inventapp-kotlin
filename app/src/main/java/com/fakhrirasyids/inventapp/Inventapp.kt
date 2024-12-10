package com.fakhrirasyids.inventapp

import android.app.Application
import com.fakhrirasyids.inventapp.di.databaseModules
import com.fakhrirasyids.inventapp.di.repositoryModules
import com.fakhrirasyids.inventapp.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Inventapp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Inventapp)
            modules(
                databaseModules,
                repositoryModules,
                viewModelModules
            )
        }
    }
}