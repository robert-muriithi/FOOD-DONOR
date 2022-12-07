package edu.puo.foodonus.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.puo.foodonus.repository.AuthRepository
import edu.puo.foodonus.repository.AuthRepositoryImpl
import edu.puo.foodonus.repository.MainRepository
import edu.puo.foodonus.repository.RepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ) : AuthRepository {
        return AuthRepositoryImpl(db, auth)
    }

    @Provides
    @Singleton
    fun provideRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth

    ) : MainRepository{
        return RepositoryImpl(database, auth)
    }

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(@ApplicationContext app: Application): Application = app

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

}