package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.framework.SelectedAvailabilityFiltersRepository
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
        private val selectedAvailabilityFiltersRepository: SelectedAvailabilityFiltersRepository,
        private val requestNewLocation: RequestNewLocation//TODO: change
) : BaseMvpPresenterImpl<AvailabilityPresenter.View>(mView){

    interface View : BaseMvpView {
        fun renderAvailableTables(availabilities: List<Availability>)
        fun showProgressBar()
        fun hideProgressBar()
        fun showNetworkError()
    }

    override fun onAttachView() {
    }

    override fun onStartView() {
        super.onStartView()
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar();

                    val companyId = prefs.companyUId!!
                    val buildingId = selectedAvailabilityFiltersRepository.getBuilding()?.uID
                    val roomId = selectedAvailabilityFiltersRepository.getRoom()?.uID

                    val startDate = selectedAvailabilityFiltersRepository.getStartDate()?:Date()
                    val endDate =  selectedAvailabilityFiltersRepository.getEndDate()?:Date(startDate.time + 3600)

                    val availabilities = withContext(Dispatchers.IO) { getAvailableTables(companyId, buildingId, roomId, startDate, endDate) }
                    view?.renderAvailableTables(availabilities.map { it.toPresentationModel("ROOM NAME")/*TODO*/ })
                    view?.hideProgressBar();
                }
            } else {
                view?.showNetworkError()
            }
        }
    }

    fun newBookingClicked() = GlobalScope.launch(Dispatchers.Main) {
        val locations = withContext(Dispatchers.IO) { requestNewLocation() }
        // TODO: go to reservation fragment
    }

    companion object {
        private val TAG = "AvailabilityPresenter"
    }
}
