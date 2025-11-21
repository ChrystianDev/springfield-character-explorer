/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobilitydevai.sce.data.repository.remote.UserRepositoryImpl
import com.mobilitydevai.sce.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): UserRepository {
        return UserRepositoryImpl(firestore, auth)
    }
}
