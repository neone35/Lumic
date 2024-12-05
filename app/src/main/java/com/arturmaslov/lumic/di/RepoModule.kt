package com.arturmaslov.lumic.di

import android.app.Application
import androidx.room.Room
import com.arturmaslov.lumic.data.LocalDataSource
import com.arturmaslov.lumic.data.LocalDatabase
import com.arturmaslov.lumic.data.MainRepository
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repoModule = module {
    single { provideLocalDatabase(androidApplication()) }
    single { provideLocalDataSource(get()) }
    single { provideMainRepository(get()) }
}

fun provideLocalDatabase(app: Application): LocalDatabase {
    val dbName = LocalDatabase.DATABASE_NAME
    return Room.databaseBuilder(
        app,
        LocalDatabase::class.java, dbName
    )
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideLocalDataSource(
    localDB: LocalDatabase
): LocalDataSource {
    return LocalDataSource(localDB, Dispatchers.IO)
}

private fun provideMainRepository(
    localDataSource: LocalDataSource
): MainRepository {
    return MainRepository(localDataSource)
}