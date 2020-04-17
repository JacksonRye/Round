package com.computerwizards.android.round.di

import com.computerwizards.android.round.ui.ProvidersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProvidersModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun providersFragment(): ProvidersFragment

}