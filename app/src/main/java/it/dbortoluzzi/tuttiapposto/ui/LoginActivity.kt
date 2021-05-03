package it.dbortoluzzi.tuttiapposto.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: get from service
        mAuth = FirebaseAuth.getInstance()

        binding.LoginBtn.setOnClickListener {
            val email = binding.LoginEmail.text.toString().trim()
            val password = binding.LoginPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                // TODO: add message
                binding.LoginEmail.error = "Enter Email"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                // TODO: add message
                binding.LoginPassword.error = "Enter Password"
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.LoginRegisterBtn.setOnClickListener {
            val registerActivity = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(registerActivity)
            finish()
        }

    }

    private fun loginUser(email: String, password: String) {
        // TODO: add LOADING

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val startIntent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(startIntent)
                        finish()
                    } else {
                        Log.e(TAG,"Error in login", task.exception)
                        // TODO: add error message
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    companion object {
        private val TAG = "LoginActivity"
    }
}
