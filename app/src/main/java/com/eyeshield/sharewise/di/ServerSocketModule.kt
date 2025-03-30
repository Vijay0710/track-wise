package com.eyeshield.sharewise.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServerSocketModule {

    @Provides
    fun providesPort(): Int = 8080
}