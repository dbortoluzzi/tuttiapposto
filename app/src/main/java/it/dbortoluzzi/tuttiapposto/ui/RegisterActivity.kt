package it.dbortoluzzi.tuttiapposto.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.ActivityRegisterBinding
import it.dbortoluzzi.domain.User
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseMvpActivity<RegisterActivity, RegisterPresenter>(), RegisterPresenter.View {

    @Inject
    override lateinit var mPresenter: RegisterPresenter

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RegisterBtn.setOnClickListener {
            val loginIntent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        binding.RegisterBtn.setOnClickListener {
            val email = binding.RegisterEmail.text.toString().trim()
            val password = binding.RegisterPassword.text.toString().trim()


            if (TextUtils.isEmpty(email)) {
                binding.RegisterEmail.error = getString(R.string.email_error)
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                binding.RegisterPassword.error = getString(R.string.password_error)
                return@setOnClickListener
            }

            mPresenter.registerBtnClicked(email, password)
        }

        binding.RegisterLoginBtn.setOnClickListener {
            navigateToLogin()
        }
    }

    override fun userRegistered(user: User) {
        val Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(Intent)
        finish()
    }

    override fun userNotRegistered(errorMessage: String) {
        Log.e(TAG,"Error in register: $errorMessage")
        Toast.makeText(this, getString(R.string.registration_error), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLogin() {
        val Intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(Intent)
        finish()
    }

    companion object {
        private val TAG = "RegisterActivity"
    }
}



