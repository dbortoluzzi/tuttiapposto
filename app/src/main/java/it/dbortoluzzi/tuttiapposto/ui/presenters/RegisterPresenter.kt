package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.Register
import kotlinx.coroutines.*
import javax.inject.Inject

class RegisterPresenter @Inject constructor(
        mView: View?,
        private val register: Register,
) : BaseMvpPresenterImpl<RegisterPresenter.View>(mView){

    interface View : BaseMvpView {
        fun userRegistered(user: User)
        fun userNotRegistered(errorMessage: String)
        fun navigateToLogin()
    }

    fun doRegister(email: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val serviceResult: ServiceResult<User> = register(email, password)
            GlobalScope.launch(Dispatchers.Main) {
                when (serviceResult) {
                    is ServiceResult.Success -> view?.userRegistered(serviceResult.data)
                    is ServiceResult.Error -> view?.userNotRegistered(serviceResult.exception.message?:"error")
                }
            }
        }

    }

    companion object {
        private val TAG = "RegisterPresenter"
    }
}
