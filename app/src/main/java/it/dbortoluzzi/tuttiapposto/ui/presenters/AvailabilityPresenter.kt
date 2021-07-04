package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.Availability
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.model.toPresentationModel
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetAvailableTables
import it.dbortoluzzi.usecases.RequestNewLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class AvailabilityPresenter @Inject constructor(
        mView: View?,
        private val getAvailableTables: GetAvailableTables,
        private val requestNewLocation: RequestNewLocation//TODO: change
) : BaseMvpPresenterImpl<AvailabilityPresenter.View>(mView){

    interface View : BaseMvpView {
        fun renderAvailableTables(availabilities: List<Availability>)
    }

    override fun onAttachView() {
        if (PrefsValidator.isConfigured(prefs)) {
            GlobalScope.launch(Dispatchers.Main) {
                val companyId = prefs.companyUId!!
                // TODO: change
                val buildingId = "VTdqvUGCKLWKq0SFkTHx"
                val roomId = "B29tSJlDqC6J6OG9Jcug"

                val startDate = Date()
                val endDate = Date(startDate.time + 3600)

                val availabilities = withContext(Dispatchers.IO) { getAvailableTables(companyId, buildingId, roomId, startDate, endDate) }
                view?.renderAvailableTables(availabilities.map { it.toPresentationModel("ROOM NAME")})
            }
        }
    }

    fun newBookingClicked() = GlobalScope.launch(Dispatchers.Main) {
        val locations = withContext(Dispatchers.IO) { requestNewLocation() }
        // TODO: go to reservation fragement
    }

    companion object {
        private val TAG = "AvailabilityPresenter"
    }
}
