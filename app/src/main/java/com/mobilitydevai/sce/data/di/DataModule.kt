/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.sce.data.di

import android.content.Context
import androidx.room.Room
import com.mobilitydevai.sce.BuildConfig
import com.mobilitydevai.sce.data.network.SimpsonsClient
import com.mobilitydevai.sce.data.network.SimpsonsService
import com.mobilitydevai.sce.data.repository.SimpsonsRepositoryImpl
import com.mobilitydevai.sce.data.repository.local.DataBaseRoom
import com.mobilitydevai.sce.data.repository.local.FavouriteCharacterDao
import com.mobilitydevai.sce.domain.repository.SimpsonsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
        val buildConfig = BuildConfig.BASE_URL
        return Retrofit.Builder()
            .baseUrl(buildConfig)
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
     * [@Provides] indica que esta función provee una instancia de [SimpsonsRepository]
     * y se declara con objectos, no con clases abstractas. Ksp genera el
     * código necesario para proveer la instancia
     */

    // ****** REPOSITORIES ******
    @Provides
    fun provideSimpsonsRepository(simpsonsService: SimpsonsService): SimpsonsRepository {
        return SimpsonsRepositoryImpl(simpsonsService)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DataBaseRoom {
        return Room.databaseBuilder(
            context,
            DataBaseRoom::class.java,
            "FavouriteCharacter.db"
        ).build()
    }

    @Provides
    fun provideFavouriteCharacterDao(db: DataBaseRoom): FavouriteCharacterDao =
        db.favouriteCharacterDao()

    @Provides
    fun provideLocationsDispatcher(): CoroutineDispatcher = Dispatchers.IO

}