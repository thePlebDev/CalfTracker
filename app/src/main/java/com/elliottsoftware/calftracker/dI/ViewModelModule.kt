package com.elliottsoftware.calftracker.dI

import com.elliottsoftware.calftracker.data.remote.WeatherApi
import com.elliottsoftware.calftracker.data.remote.WeatherRetrofitInstance
import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.data.repositories.WeatherRepositoryImpl
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    fun providesWeatherRepository(api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    @Provides
    fun providesWeatherApi(): WeatherApi{
        return WeatherRetrofitInstance.api
    }

/***********THIS IS WHAT WE WANT TO DO****************/
//@Provides
//fun provideFetchApi():FetchApi{
//    return Retrofit.Builder()
//        .baseUrl("https://fetch-hiring.s3.amazonaws.com")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(FetchApi::class.java)
//}
//
//
//
//    @Provides
//    fun provideRemoteRepository(
//        fetchApi: FetchApi
//    ):RemoteRepository{
//        return RemoteRepositoryImpl(
//            fetchApi
//        )
//    }


}