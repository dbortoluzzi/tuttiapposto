package it.dbortoluzzi.tuttiapposto.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.ActivityLoginBinding
import it.dbortoluzzi.domain.User
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseMvpActivity<LoginActivity, LoginPresenter>(), LoginPresenter.View {

    @Inject
    override lateinit var mPresenter: LoginPresenter

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginBtn.setOnClickListener {
            val email = binding.LoginEmail.text.toString().trim()
            val password = binding.LoginPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                binding.LoginEmail.error = getString(R.string.email_error)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.LoginPassword.error = getString(R.string.password_error)
                return@setOnClickListener
            }

            mPresenter.doLogin(email, password)
        }

        binding.LoginRegisterBtn.setOnClickListener { navigateToRegisterUser() }

    }

    override fun userLogged(user: User) {
        val startIntent = Intent(applicationContext, MainActivity::class.java)
        startIntent.putExtra(USER_DATA, user)
        startActivity(startIntent)
        Toast.makeText(applicationContext , getString(R.string.login_success) , Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun userNotLogged(errorMessage: String) {
        Log.e(TAG,"Error in login: $errorMessage")
        Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToRegisterUser() {
        val registerActivity = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(registerActivity)
        finish()
    }

    companion object {
        private val TAG = "LoginActivity"
        val USER_DATA = "${TAG}_USER_DATA"
    }
}
