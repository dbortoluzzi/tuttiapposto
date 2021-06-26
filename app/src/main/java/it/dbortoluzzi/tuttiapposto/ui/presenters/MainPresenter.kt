package it.dbortoluzzi.tuttiapposto.ui.presenters

import android.view.MenuItem
import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.model.Prefs
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetUser
import it.dbortoluzzi.usecases.Logout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainPresenter @Inject constructor(
        mView: View?,
        private val logout: Logout,
        private val getUser: GetUser
) : BaseMvpPresenterImpl<MainPresenter.View>(mView){

    interface View : BaseMvpView {
        fun logoutSuccess()
        fun logoutError(errorMessage: String)
        fun initializeWhenUserIsLogged()
        fun initializeWhenUserIsNotLogged()
        fun onLogout(item: MenuItem)
    }

    override fun onAttachView() {
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
