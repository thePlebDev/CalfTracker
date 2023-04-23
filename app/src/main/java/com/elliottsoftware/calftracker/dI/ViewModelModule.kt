package com.elliottsoftware.calftracker.dI

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliottsoftware.calftracker.data.remote.WeatherApi
import com.elliottsoftware.calftracker.data.remote.WeatherRetrofitInstance
import com.elliottsoftware.calftracker.data.repositories.*
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.domain.repositories.SubscriptionRepository
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(ActivityRetainedComponent::class) // The associated Hilt component ViewModelModule, is scoped to ActivityRetainedComponent
object ViewModelModule {
//    Warning: A common misconception is that all bindings declared in
//    a module will be scoped to the component the module is installed in.
//    However, this isn’t true. Only bindings declarations annotated with a scope
//    annotation will be scoped.

//    When to scope?
//    Scoping a binding has a cost on both the generated code size and
//    its runtime performance so use scoping sparingly. The general rule for determining
//    if a binding should be scoped is to only scope the binding if it’s required for the
//    correctness of the code. If you think a binding should be scoped for purely performance reasons,
//    first verify that the performance is an issue, and if it is consider using @Reusable instead
//    of a component scope.


    // Everything with @Provides  is "unscoped".
    @Provides
    fun providesAuthRepository(auth: FirebaseAuth): AuthRepository {
        return  AuthRepositoryImpl(auth)
    }

    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return  Firebase.auth
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


    @Provides
    fun provideSubscriptionDataRepository(
        billingClient: BillingClientWrapper
    ): SubscriptionRepository {
    // billingClientWrapper = billingClient
        return SubscriptionDataRepository(billingClient)
    }

    @Provides
    fun provideBillingClientWrapper(
        @ApplicationContext context:Context
    ):BillingClientWrapper{
        val _billingConnectionState = MutableLiveData(false)
        Timber.tag("BillingClientWrapper").d("Created dependency injection")

        var billingClient: BillingClientWrapper = BillingClientWrapper(context.applicationContext)
        billingClient.startBillingConnection(billingConnectionState = _billingConnectionState)
        return billingClient
    }
}

