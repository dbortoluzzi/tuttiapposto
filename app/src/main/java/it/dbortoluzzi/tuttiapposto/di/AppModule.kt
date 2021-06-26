package it.dbortoluzzi.tuttiapposto.di

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import it.dbortoluzzi.data.*
import it.dbortoluzzi.tuttiapposto.framework.FakeLocationSource
import it.dbortoluzzi.tuttiapposto.framework.FirebaseAuthenticationSource
import it.dbortoluzzi.tuttiapposto.framework.InMemoryLocationPersistenceSource
import it.dbortoluzzi.tuttiapposto.ui.LocationPresenter
import it.dbortoluzzi.tuttiapposto.ui.LoginPresenter
import it.dbortoluzzi.tuttiapposto.ui.MainPresenter
import it.dbortoluzzi.tuttiapposto.ui.RegisterPresenter
import it.dbortoluzzi.usecases.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /*Sources*/
    @Provides
    @Singleton
    fun locationPersistenceSource(): LocationPersistenceSource = InMemoryLocationPersistenceSource()


    @Provides
    @Singleton
    fun deviceLocationSource(): DeviceLocationSource = FakeLocationSource()

    @Provides
    @Singleton
    fun authenticationSource(firebaseAuth: FirebaseAuth): AuthenticationSource = FirebaseAuthenticationSource(firebaseAuth)

    /*Repositories*/
    @Provides
    @Singleton
    fun locationsRepository(persistence: LocationPersistenceSource, deviceLocationSource: DeviceLocationSource): LocationsRepository {
        return LocationsRepository(persistence, deviceLocationSource)
    }

    @Provides
    @Singleton
    fun usersRepository(authenticationSource: AuthenticationSource): UsersRepository {
        return UsersRepository(authenticationSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

@InstallIn(ActivityComponent::class)
@Module
object ActivityModule {

    @Provides
    fun bindLoginActivity(activity: Activity): LoginPresenter.View {
        return activity as LoginPresenter.View
    }

    @Provides
    fun bindRegisterActivity(activity: Activity): RegisterPresenter.View {
        return activity as RegisterPresenter.View
    }

    @Provides
    fun bindMainActivity(activity: Activity): MainPresenter.View {
        return activity as MainPresenter.View
    }
}

@InstallIn(FragmentComponent::class)
@Module
object FragmentModule {

    @Provides
    @FragmentScoped
    fun bindLocationFragment(fragment: Fragment): LocationPresenter.View {
        return fragment as LocationPresenter.View
    }
}

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    @Singleton
    fun getLocations(locationsRepository: LocationsRepository): GetLocations = GetLocations(locationsRepository)

    @Provides
    @Singleton
    fun requestNewLocation(locationsRepository: LocationsRepository): RequestNewLocation = RequestNewLocation(locationsRepository)

    @Provides
    @Singleton
    fun login(usersRepository: UsersRepository): Login = Login(usersRepository)

    @Provides
    @Singleton
    fun register(usersRepository: UsersRepository): Register = Register(usersRepository)

    @Provides
    @Singleton
    fun logout(usersRepository: UsersRepository): Logout = Logout(usersRepository)

    @Provides
    @Singleton
    fun getUser(usersRepository: UsersRepository): GetUser = GetUser(usersRepository)
}
