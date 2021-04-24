package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import it.dbortoluzzi.data.LocationPersistenceSource
import it.dbortoluzzi.domain.Location
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryLocationPersistenceSource @Inject constructor(): LocationPersistenceSource {

    private var locations: List<Location> = emptyList()

    override fun getPersistedLocations(): List<Location> = locations

    override fun saveNewLocation(location: Location) {
        Log.i(TAG, "new location: + $location")
        locations = locations + location
    }

    companion object {
        private val TAG = "InMemoryLocationPersistenceSource"
    }
}