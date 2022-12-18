package com.elliottsoftware.calftracker.dI

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(ActivityRetainedComponent::class) //THIS IS THE COMPONENT
object ViewModelModule {


    //NOT SCOPED BECAUSE WE HAVE NOT DECLARED A SCOPE ANNOTATION
    @Provides
    fun providesAuthRepository(): AuthRepository {
        return  AuthRepositoryImpl()
    }

    @Provides
    fun providesDatabaseRepository(): DatabaseRepository{
        return DatabaseRepositoryImpl()
    }




}