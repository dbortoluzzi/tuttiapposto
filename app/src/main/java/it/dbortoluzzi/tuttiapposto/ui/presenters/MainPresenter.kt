package it.dbortoluzzi.tuttiapposto.ui.presenters

import android.view.MenuItem
import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetAvailableTables
import it.dbortoluzzi.usecases.GetUser
import it.dbortoluzzi.usecases.Logout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MainPresenter @Inject constructor(
        mView: View?,
        private val logout: Logout,
        private val getUser: GetUser,
        private val getAvailableTables: GetAvailableTables
) : BaseMvpPresenterImpl<MainPresenter.View>(mView){

    interface View : BaseMvpView {
        fun logoutSuccess()
        fun logoutError(errorMessage: String)
        fun initializeWhenUserIsLogged()
        fun initializeWhenUserIsNotLogged()
        fun closeNavigationDrawer()
        fun onLogoutClicked(item: MenuItem)
        fun onSettingsClicked(item: MenuItem)
        fun onFilterClicked(item: MenuItem)
    }

    override fun onAttachView() {

        val buildingId = "VTdqvUGCKLWKq0SFkTHx"
        val roomId = "B29tSJlDqC6J6OG9Jcug"

        GlobalScope.launch(Dispatchers.IO) {
            // TODO: only for tests!
            if (prefs.companyUId != null) {
                val serviceResult = getAvailableTables(prefs.companyUId!!, buildingId, roomId, Date(), Date())
                val a = 2
//            GlobalScope.launch(Dispatchers.Main) {
//
//            }
            }
        }
    }

    fun doLogout() {
        GlobalScope.launch(Dispatchers.IO) {
            val serviceResult = logout()
            GlobalScope.launch(Dispatchers.Main) {
                when (serviceResult) {
                    is ServiceResult.Success<*> -> view?.logoutSuccess()
                    is ServiceResult.Error -> view?.logoutError(serviceResult.exception.message?:"error")
                }
            }
        }
    }

    fun getCurrentUserLogged() : User? {
        val user = getUser()
        return when (user) {
            is ServiceResult.Success -> user.data
            else -> null
        }
    }

    fun checkIfUserIsLogged() {
        getCurrentUserLogged() != null
    }

    companion object {
        private val TAG = "MainPresenter"
    }
}
