package com.edify.challenge

import android.app.Application
import com.edify.challenge.di.AppComponent
import com.edify.challenge.di.AppModule
import com.edify.challenge.di.DaggerAppComponent
import com.edify.challenge.di.StorageModule
import timber.log.Timber

class App : Application() {

    companion object {
        private lateinit var appComponent: AppComponent
        fun getAppComponent(): AppComponent {
            return appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
        initDaggerAppComponent()
        Timber.plant(Timber.DebugTree())
    }

    private fun initDaggerAppComponent(): AppComponent {
        appComponent =
            DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .storageModule(StorageModule(this))
                .build()
        return appComponent
    }
}