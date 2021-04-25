package it.dbortoluzzi.tuttiapposto.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import it.dbortoluzzi.data.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: add to DI
        mAuth = FirebaseAuth.getInstance();

        binding.RegisterBtn.setOnClickListener {
            val loginIntent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        binding.RegisterBtn.setOnClickListener {
            val email = binding.RegisterEmail.text.toString().trim()
            val password = binding.RegisterPassword.text.toString().trim()


            if (TextUtils.isEmpty(email)) {
                binding.RegisterEmail.error = "Enter Email"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                binding.RegisterPassword.error = "Enter Password"
                return@setOnClickListener
            }
            createUser(email, password)
        }
    }

    fun createUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val Intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(Intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Authentication failed.${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}



