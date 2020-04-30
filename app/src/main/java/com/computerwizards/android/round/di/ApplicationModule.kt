package com.computerwizards.android.round.di

import com.computerwizards.android.round.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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
    fun provideUser(): User {
        val fireUser = FirebaseAuth.getInstance().currentUser ?: return User()

        return User(fireUser)
    }

    @Singleton
    @Provides
    fun provideStorage(): FirebaseStorage = Firebase.storage


    companion object {
        private const val TAG = "ApplicationModule"
    }
}