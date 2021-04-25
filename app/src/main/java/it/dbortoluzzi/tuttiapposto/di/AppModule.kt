package it.dbortoluzzi.tuttiapposto.framework

import android.app.Activity
import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import it.dbortoluzzi.data.DeviceLocationSource
import it.dbortoluzzi.data.LocationPersistenceSource
import it.dbortoluzzi.data.LocationsRepository
import it.dbortoluzzi.tuttiapposto.ui.MainActivity
import it.dbortoluzzi.tuttiapposto.ui.MainPresenter
import it.dbortoluzzi.tuttiapposto.ui.fragments.LocationsFragment
import it.dbortoluzzi.usecases.GetLocations
import it.dbortoluzzi.usecases.RequestNewLocation
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun locationPersistenceSource(): LocationPersistenceSource = InMemoryLocationPersistenceSource()


    @Provides
    @Singleton
    fun deviceLocationSource(): DeviceLocationSource = FakeLocationSource()

    @Provides
    @Singleton
    fun locationsRepository(persistence: LocationPersistenceSource, deviceLocationSource: DeviceLocationSource): LocationsRepository {
        return LocationsRepository(persistence, deviceLocationSource)
    }

}

@InstallIn(ActivityComponent::class)
@Module
object ActivityModule {

//    @Provides
//    fun bindActivity(activity: Activity): MainActivity {
//        return activity as MainActivity
//    }

}

@InstallIn(FragmentComponent::class)
@Module
object FragmentModule {

    @Provides
    @FragmentScoped
    fun bindFragment(fragment: Fragment): LocationsFragment {
        return fragment as LocationsFragment
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

}
