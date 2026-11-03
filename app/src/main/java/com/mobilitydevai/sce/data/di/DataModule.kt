/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.sce.data.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.gson.Gson
import com.mobilitydevai.sce.BuildConfig
import com.mobilitydevai.sce.data.network.EpisodesOkHttpClient
import com.mobilitydevai.sce.data.network.SimpsonsClient
import com.mobilitydevai.sce.data.network.SimpsonsService
import com.mobilitydevai.sce.data.repository.EpisodesRepositoryImpl
import com.mobilitydevai.sce.data.repository.SimpsonsRepositoryImpl
import com.mobilitydevai.sce.data.repository.local.DataBaseRoom
import com.mobilitydevai.sce.data.repository.local.FavouriteCharacterDao
import com.mobilitydevai.sce.domain.repository.EpisodesRepository
import com.mobilitydevai.sce.domain.repository.SimpsonsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val HTTP_LOG_TAG = "okhttp.OkHttpClient"
private const val HTTP_LOG_CHUNK_SIZE = 3000

private val Context.likesDataStore: DataStore<Preferences> by preferencesDataStore(name = "likes")

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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val buildConfig = BuildConfig.BASE_URL
        return Retrofit.Builder()
            .baseUrl(buildConfig)
            // Shared client, so Retrofit calls also go through the logging interceptor
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideSimpsonsClient(retrofit: Retrofit): SimpsonsClient {
        return retrofit.create(SimpsonsClient::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        // HTTP logs (method, URL, status, headers and bodies) only in debug builds.
        // Logcat filter: tag:okhttp.OkHttpClient
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            // Logcat truncates entries at ~4 KB, so long bodies are split into chunks
            message.chunked(HTTP_LOG_CHUNK_SIZE).forEach { Log.d(HTTP_LOG_TAG, it) }
        }.apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideEpisodesOkHttpClient(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): EpisodesOkHttpClient {
        return EpisodesOkHttpClient(okHttpClient, gson)
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
    fun provideEpisodesRepository(episodesOkHttpClient: EpisodesOkHttpClient): EpisodesRepository {
        return EpisodesRepositoryImpl(episodesOkHttpClient)
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
    @Singleton
    fun provideLikesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.likesDataStore

    @Provides
    fun provideLocationsDispatcher(): CoroutineDispatcher = Dispatchers.IO

}