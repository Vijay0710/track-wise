package com.eyeshield.sharewise.di

import com.eyeshield.sharewise.core.data.ClientSocketRepositoryImpl
import com.eyeshield.sharewise.core.data.ServerSocketRepositoryImpl
import com.eyeshield.sharewise.core.domain.ClientSocketRepository
import com.eyeshield.sharewise.core.domain.ServerSocketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocketModule {

    @Binds
    @Singleton
    abstract fun providesSocketRepository(
        serverSocketRepositoryImpl: ServerSocketRepositoryImpl
    ): ServerSocketRepository

    @Binds
    abstract fun providesClientRepository(
        clientSocketRepositoryImpl: ClientSocketRepositoryImpl
    ): ClientSocketRepository
}