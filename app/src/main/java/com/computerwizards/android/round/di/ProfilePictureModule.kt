package com.computerwizards.android.round.di

import androidx.lifecycle.ViewModel
import com.computerwizards.android.round.ui.ProfilePictureBottomSheet
import com.computerwizards.android.round.ui.ProfilePictureFragment
import com.computerwizards.android.round.ui.ProfilePictureViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ProfilePictureModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun profilePictureFragment(): ProfilePictureFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfilePictureViewModel::class)
    abstract fun bindViewModel(viewModel: ProfilePictureViewModel): ViewModel

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun profilePictureBottomSheet(): ProfilePictureBottomSheet
}

