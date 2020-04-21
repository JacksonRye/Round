package com.computerwizards.android.round.di

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.ui.*
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

@Module
abstract class ServiceOptionsModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun serviceOptionsDialogFragment(): ServiceOptionsDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(ServiceOptionsViewModel::class)
    abstract fun bindViewModel(viewModel: ServiceOptionsViewModel): ViewModel
}

@Module
abstract class UserProfileModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun userProfileFragment(): UserProfileFragment


    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    abstract fun bindViewModel(viewModel: UserProfileViewModel): ViewModel
}