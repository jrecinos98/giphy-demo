package com.edify.challenge.di

import com.edify.challenge.App
import com.edify.challenge.features.preview.PreviewGifFragment
import com.edify.challenge.features.search.GifFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, StorageModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun appModule(module: AppModule): Builder
        fun storageModule(module: StorageModule) : Builder
        fun build(): AppComponent
    }

    fun inject(app: App)

    fun inject(gifFragment: GifFragment)

    fun inject(previewGifFragment: PreviewGifFragment)
}