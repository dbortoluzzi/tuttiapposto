package it.dbortoluzzi.tuttiapposto.ui

import android.os.Bundle
import android.view.MenuItem
import it.dbortoluzzi.data.databinding.ActivityMainBinding
import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.usecases.GetUser
import it.dbortoluzzi.usecases.Logout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainPresenter @Inject constructor(
        mView: MainPresenter.View?,
        private val logout: Logout,
        private val getUser: GetUser,
) : BaseMvpPresenterImpl<MainPresenter.View>(mView){

    interface View : BaseMvpView {
        fun initializeView()
        fun logoutSuccess()
        fun logoutError(errorMessage: String)
        fun initializeWhenUserIsLogged(currentUser: User)
        fun initializeWhenUserIsNotLogged()
        fun logout(item: MenuItem)
    }

    override fun onAttachView(savedInstanceState: Bundle?) {
        view?.initializeView()

        val currentUser = getCurrentUserLogged()
        if(currentUser == null){
            view?.initializeWhenUserIsNotLogged()
        }else{
            // TODO: put the user to savedInstance to remember the user logged
            view?.initializeWhenUserIsLogged(currentUser)
        }
    }

    fun logoutBtnClicked() {
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
