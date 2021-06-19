package it.dbortoluzzi.tuttiapposto.ui

import android.util.Log
import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.usecases.Login
import kotlinx.coroutines.*
import javax.inject.Inject

class LoginPresenter @Inject constructor(
        mView: LoginPresenter.View?,
        private val login: Login,
) : BaseMvpPresenterImpl<LoginPresenter.View>(mView){

    init {
        Log.i(TAG, "init object")
    }

    interface View : BaseMvpView {
        fun userLogged(user: User)
        fun userNotLogged(errorMessage: String)
        fun navigateToRegisterUser()
    }

    override fun onAttachView() {
    }

    fun loginBtnClicked(email: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val serviceResult: ServiceResult<User> = login(email, password)
            GlobalScope.launch(Dispatchers.Main) {
                when (serviceResult) {
                    is ServiceResult.Success -> view?.userLogged(serviceResult.data)
                    is ServiceResult.Error -> view?.userNotLogged(serviceResult.exception.message?:"error")
                }
            }
        }

    }

    companion object {
        private val TAG = "LoginPresenter"
    }
}
