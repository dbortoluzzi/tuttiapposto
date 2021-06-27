package it.dbortoluzzi.tuttiapposto.ui.presenters

import android.util.Log
import it.dbortoluzzi.tuttiapposto.model.Location
import it.dbortoluzzi.tuttiapposto.model.toPresentationModel
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetLocations
import it.dbortoluzzi.usecases.RequestNewLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import it.dbortoluzzi.domain.Location as DomainLocation

class LocationPresenter @Inject constructor(
        mView: View?,
        private val getLocations: GetLocations,
        private val requestNewLocation: RequestNewLocation
) : BaseMvpPresenterImpl<LocationPresenter.View>(mView){

    init {
        Log.i(TAG, "init object")
    }

    interface View : BaseMvpView {
        fun renderLocations(locations: List<Location>)
    }

    override fun onAttachView() {
        GlobalScope.launch(Dispatchers.Main) {
            val locations = withContext(Dispatchers.IO) { getLocations() }
            view?.renderLocations(locations.map(DomainLocation::toPresentationModel))
        }
    }

    fun newLocationClicked() = GlobalScope.launch(Dispatchers.Main) {
        val locations = withContext(Dispatchers.IO) { requestNewLocation() }
        view?.renderLocations(locations.map(DomainLocation::toPresentationModel))
    }

    companion object {
        private val TAG = "LocationPresenter"
    }
}
