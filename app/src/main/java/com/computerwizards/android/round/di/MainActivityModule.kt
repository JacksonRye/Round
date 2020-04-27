package com.computerwizards.android.round.di

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.ui.MainActivity
import com.computerwizards.android.round.ui.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindViewModel(viewModel: MainActivityViewModel): ViewModel


}