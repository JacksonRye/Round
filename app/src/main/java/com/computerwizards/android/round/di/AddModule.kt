package com.computerwizards.android.round.di

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.ui.AddFragment
import com.computerwizards.android.round.ui.AddViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AddModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun addFragment(): AddFragment


    @Binds
    @IntoMap
    @ViewModelKey(AddViewModel::class)
    abstract fun bindViewModel(viewModel: AddViewModel): ViewModel
}