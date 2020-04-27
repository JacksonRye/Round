package com.computerwizards.android.round.di

import com.computerwizards.android.round.services.UploadService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UploadServiceModule {


    @ContributesAndroidInjector
    internal abstract fun uploadService(): UploadService


}