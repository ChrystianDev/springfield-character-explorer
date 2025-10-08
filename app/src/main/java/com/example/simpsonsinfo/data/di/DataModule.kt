/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.data.di

import com.example.simpsonsinfo.data.network.SimpsonsClient
import com.example.simpsonsinfo.data.network.SimpsonsService
import com.example.simpsonsinfo.data.repository.SimpsonsRepositoryImpl
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import com.example.simpsonsinfo.domain.usecase.SimpsonsUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * [@Module] indica que esta clase es un modulo de Hilt, y provee las instancias creadas en esta clase
 * [@InstallIn] indica el alcance del modulo
 * [@SingletonComponent] indica que el modulo es un singleton
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {



    // ****** PLUGINS or DEPENDENCIES ******
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://thesimpsonsapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideSimpsonsClient(retrofit: Retrofit): SimpsonsClient {
        return retrofit.create(SimpsonsClient::class.java)
    }



    // ****** API-REST ******
    @Provides
    fun provideApiService(simpsonsClient: SimpsonsClient): SimpsonsService {
        return SimpsonsService(simpsonsClient)
    }

    /**
     * [@Provides] indica que esta funcion provee una instancia de [AuthRepository]
     * y funciona con objectos, no funciona con clases abstractas. Ksp genera el
     * código necesario para proveer la instancia
     */
    // ****** REPOSITORIES ******
//    @Provides
//    fun provideAuthRepository(): AuthRepository{
//        return AuthRepositoryImpl()
//    }
//
    @Provides
    fun provideSimpsonsRepository(simpsonsService: SimpsonsService): SimpsonsRepository{
        return SimpsonsRepositoryImpl(simpsonsService)
    }


    // ****** USECASES ******
    @Provides
    fun provideSimpsonsUseCase(simpsonsRepository: SimpsonsRepository): SimpsonsUsecase{
        return SimpsonsUsecase(simpsonsRepository)
    }
}