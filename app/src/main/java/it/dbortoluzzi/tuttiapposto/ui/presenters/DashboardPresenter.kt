package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.tuttiapposto.model.Score
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardPresenter @Inject constructor(
        mView: View?,
) : BaseMvpPresenterImpl<DashboardPresenter.View>(mView) {

    interface View : BaseMvpView {
        fun renderHourOccupationChart(scores: List<Score>)
        fun renderRoomOccupationChart(scores: List<Score>)
    }

    override fun onAttachView() {
        GlobalScope.launch(Dispatchers.Main) {
            val hourScores = getHourOccupationList()
            view?.renderHourOccupationChart(hourScores)

            val roomScores = getRoomOccupationList()
            view?.renderRoomOccupationChart(roomScores)
        }
    }

    // TODO:
    // simulate api call
    // we are initialising it directly
    private fun getHourOccupationList(): List<Score> {
        return listOf(
                Score("09", 5),
                Score("10", 5),
                Score("11", 5),
                Score("12", 11),
                Score("13", 15),
                Score("14", 15),
                Score("15", 13),
                Score("16", 16),
                Score("17", 5, true),
                Score("18", 3),
                Score("19", 5),
        )
    }

    // TODO:
    // simulate api call
    // we are initialising it directly
    private fun getRoomOccupationList(): List<Score> {
        return listOf(
                Score("Stanza Studio", 70),
                Score("Open Space", 25)
        )
    }

    companion object {
        private val TAG = "DashboardPresenter"
    }
}
