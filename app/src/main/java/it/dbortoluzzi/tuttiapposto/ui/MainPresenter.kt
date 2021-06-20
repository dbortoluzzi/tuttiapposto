package it.dbortoluzzi.tuttiapposto.ui

import android.os.Bundle
import android.view.MenuItem
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
        fun onInitializeView()
        fun onLogoutSuccess()
        fun onLogoutError(errorMessage: String)
        fun onInitializeWhenUserIsLogged(currentUser: User, savedInstanceState: Bundle?)
        fun onInitializeWhenUserIsNotLogged()
        fun onLogout(item: MenuItem)
        fun getUserFromIntent() : User?
    }

    override fun onAttachView(savedInstanceState: Bundle?) {
        view?.onInitializeView()

        val currentUser = view?.getUserFromIntent()
                ?: savedInstanceState?.getSerializable(LoginActivity.USER_DATA) as User?
                ?: getCurrentUserLogged()
        if(currentUser == null){
            view?.onInitializeWhenUserIsNotLogged()
        }else{
            view?.onInitializeWhenUserIsLogged(currentUser, savedInstanceState)
        }
    }

    fun doLogout() {
        GlobalScope.launch(Dispatchers.IO) {
            val serviceResult = logout()
            GlobalScope.launch(Dispatchers.Main) {
                when (serviceResult) {
                    is ServiceResult.Success<*> -> view?.onLogoutSuccess()
                    is ServiceResult.Error -> view?.onLogoutError(serviceResult.exception.message?:"error")
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
