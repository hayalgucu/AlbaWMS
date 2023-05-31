package com.hayalgucu.albawms.di

import android.content.Context
import com.hayalgucu.albawms.prefstore.PrefsStore
import com.hayalgucu.albawms.services.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePrefStore(@ApplicationContext context: Context): PrefsStore =
        PrefsStore.create(context)

    @Provides
    @Singleton
    fun provideApiService(prefsStore: PrefsStore): ApiService {
        var url = ""
        runBlocking {
            url = prefsStore.getServerAddress().first()
        }
        return ApiService.create(url)
    }

}