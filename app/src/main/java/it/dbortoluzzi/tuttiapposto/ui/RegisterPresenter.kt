package it.dbortoluzzi.tuttiapposto.ui

import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.usecases.Register
import kotlinx.coroutines.*
import javax.inject.Inject

class RegisterPresenter @Inject constructor(
        mView: RegisterPresenter.View?,
        private val register: Register,
) : BaseMvpPresenterImpl<RegisterPresenter.View>(mView){

    interface View : BaseMvpView {
        fun onUserRegistered(user: User)
        fun onUserNotRegistered(errorMessage: String)
        fun onNavigateToLogin()
    }

    fun doRegister(email: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val serviceResult: ServiceResult<User> = register(email, password)
            GlobalScope.launch(Dispatchers.Main) {
                when (serviceResult) {
                    is ServiceResult.Success -> view?.onUserRegistered(serviceResult.data)
                    is ServiceResult.Error -> view?.onUserNotRegistered(serviceResult.exception.message?:"error")
                }
            }
        }

    }

    companion object {
        private val TAG = "RegisterPresenter"
    }
}
