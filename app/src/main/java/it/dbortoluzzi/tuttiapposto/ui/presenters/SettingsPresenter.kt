package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetCompanies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
        mView: View?,
        private val getCompanies: GetCompanies
) : BaseMvpPresenterImpl<SettingsPresenter.View>(mView){

    interface View : BaseMvpView {
        fun getCompanySelected(): String
        fun onSuccessSave()
        fun renderCompanies(companies: List<Company>)
    }

    override fun onAttachView() {
        GlobalScope.launch(Dispatchers.Main) {
            val companies = withContext(Dispatchers.IO) { getCompanies() }
            view?.renderCompanies(companies)
        }
    }

    fun doSaveOptions() {
        prefs.companyUId = view?.getCompanySelected()
        view?.onSuccessSave()
    }

    fun getPrefCompany(): String? {
        return prefs.companyUId
    }

    companion object {
        private val TAG = "OptionsPresenter"
    }
}
