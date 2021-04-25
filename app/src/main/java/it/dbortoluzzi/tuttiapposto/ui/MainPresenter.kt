package it.dbortoluzzi.tuttiapposto.ui

import android.util.Log
import it.dbortoluzzi.tuttiapposto.ui.fragments.LocationsFragment
import it.dbortoluzzi.usecases.GetLocations
import it.dbortoluzzi.usecases.RequestNewLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import it.dbortoluzzi.domain.Location as DomainLocation

class MainPresenter @Inject constructor(
        private var view: LocationsFragment?,
        private val getLocations: GetLocations,
        private val requestNewLocation: RequestNewLocation
) {

    init {
        Log.i(TAG, "init object")
    }

    interface View {
        fun renderLocations(locations: List<Location>)
    }

    fun onCreate() = GlobalScope.launch(Dispatchers.Main) {
        val locations = withContext(Dispatchers.IO) { getLocations() }
        view?.renderLocations(locations.map(DomainLocation::toPresentationModel))
    }

    fun newLocationClicked() = GlobalScope.launch(Dispatchers.Main) {
        val locations = withContext(Dispatchers.IO) { requestNewLocation() }
        view?.renderLocations(locations.map(DomainLocation::toPresentationModel))
    }

    fun onDestroy() {
        view = null
    }

    companion object {
        private val TAG = "MainPresenter"
    }
}
