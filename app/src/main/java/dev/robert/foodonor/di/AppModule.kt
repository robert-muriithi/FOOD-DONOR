package dev.robert.foodonor.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.foodonor.repository.MainRepository
import dev.robert.foodonor.repository.RepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(database: FirebaseDatabase) : MainRepository{
        return RepositoryImpl(database)
    }
    @Provides
    @Singleton
    fun provideDatabaseInstance(): FirebaseDatabase =
        FirebaseDatabase.getInstance()

}