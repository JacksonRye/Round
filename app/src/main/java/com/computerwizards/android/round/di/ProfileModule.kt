package com.computerwizards.android.round.di

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.ui.AddServiceDialogFragment
import com.computerwizards.android.round.ui.AddServiceViewModel
import com.computerwizards.android.round.ui.ProfileFragment
import com.computerwizards.android.round.ui.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [ViewModelBuilder::class]
)
abstract class ProfileModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun profileFragment(): ProfileFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindViewModel(viewModel: ProfileViewModel): ViewModel


}

@Module
abstract class AddServiceModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun AddServiceDialogFragment(): AddServiceDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(AddServiceViewModel::class)
    abstract fun bindViewModel(viewModel: AddServiceViewModel): ViewModel




}