package com.computerwizards.android.round.di

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.ui.ProvidersFragment
import com.computerwizards.android.round.ui.ProvidersViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ProvidersModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun providersFragment(): ProvidersFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProvidersViewModel::class)
    abstract fun bindViewModel(viewModel: ProvidersViewModel): ViewModel

}