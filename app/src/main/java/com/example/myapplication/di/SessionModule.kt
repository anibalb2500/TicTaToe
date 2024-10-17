package com.example.myapplication.di

import com.example.myapplication.data.TicTacToeSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SessionModule {
    @Provides
    @Singleton
    fun providePrePassSession(): TicTacToeSession = TicTacToeSession
}