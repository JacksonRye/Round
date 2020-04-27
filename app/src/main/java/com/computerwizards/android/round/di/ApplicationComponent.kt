package com.computerwizards.android.round.di

import android.content.Context
import com.computerwizards.android.round.RoundApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        HomeModule::class,
        AddModule::class,
        ListFragmentModule::class,
        ProvidersModule::class,
        ProfileModule::class,
        AddServiceModule::class,
        ServiceOptionsModule::class,
        UserProfileModule::class,
        MainActivityModule::class,
        UploadServiceModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<RoundApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}