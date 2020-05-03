package com.computerwizards.android.round.di

import com.computerwizards.android.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore


    @Provides
    fun provideUser(): com.computerwizards.android.model.User {
        val fireUser = FirebaseAuth.getInstance().currentUser ?: return com.computerwizards.android.model.User()

        return com.computerwizards.android.model.User(fireUser)
    }

    @Singleton
    @Provides
    fun provideFunctions(): FirebaseFunctions = Firebase.functions

    @Singleton
    @Provides
    fun provideStorage(): FirebaseStorage = Firebase.storage


    companion object {
        private const val TAG = "ApplicationModule"
    }
}