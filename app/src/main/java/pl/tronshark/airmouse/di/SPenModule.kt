package pl.tronshark.airmouse.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.tronshark.airmouse.core.common.di.ActivityProvider
import pl.tronshark.airmouse.core.common.spen.SPenManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SPenModule {
    @Provides
    @Singleton
    fun provideActivityProvider(): ActivityProvider {
        return ActivityProvider()
    }

    @Provides
    @Singleton
    fun provideSPenManager(activityProvider: ActivityProvider): SPenManager {
        return SPenManager(activityProvider)
    }
}