package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import it.dbortoluzzi.data.AuthenticationSource
import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.*
import javax.inject.Inject
import javax.inject.Singleton

class FirebaseAuthenticationSource constructor(
        var mAuth: FirebaseAuth,
        var userSource: UserPersistenceSource
) : AuthenticationSource {

    override suspend fun login(mail: String, password: String): ServiceResult<String> {
        val firebaseUser = logInUserFromAuthWithEmailAndPassword(mail, password)
        return when(firebaseUser) {
            is Success -> Success(firebaseUser.data.uid)
            is Error -> Error(firebaseUser.exception)
            else -> throw NotImplementedError()
        }
    }

    override suspend fun register(mail: String, password: String): ServiceResult<String> {
        val firebaseUser = registerUserFromAuthWithEmailAndPassword(mail, password)
        return when(firebaseUser) {
            is Success -> {
                val userId = firebaseUser.data.uid
                return when(val saveUserResult = userSource.saveUser(userId, mail)) {
                    is Success -> Success(userId)
                    is Error -> Error(saveUserResult.exception)
                    else -> throw NotImplementedError()
                }
            }
            is Error -> Error(firebaseUser.exception)
            else -> throw NotImplementedError()
        }
    }

    override suspend fun logout(): ServiceResult<Boolean> {
        return try {
            mAuth.signOut()
            ServiceResult.Success(true)
        }catch (e: Exception) {
            ServiceResult.Success(false)
        }
    }

    suspend fun registerUserFromAuthWithEmailAndPassword(email: String, password: String): ServiceResult<FirebaseUser> {
        try {
            return when(val resultDocumentSnapshot = mAuth.createUserWithEmailAndPassword(email, password).await()) {
                is Success -> {
                    Log.i(TAG, "register Result.Success")
                    val firebaseUser: FirebaseUser = resultDocumentSnapshot.data!!.user!!
                    Success(firebaseUser)
                }
                is Error -> {
                    Log.e(TAG, "register ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        }
        catch (exception: Exception) {
            return Error(exception)
        }
    }

    suspend fun logInUserFromAuthWithEmailAndPassword(email: String, password: String): ServiceResult<FirebaseUser> {
        try {
            return when(val resultDocumentSnapshot = mAuth.signInWithEmailAndPassword(email, password).await()) {
                is Success -> {
                    Log.i(TAG, "login Result.Success")
                    val firebaseUser = resultDocumentSnapshot.data!!.user!!
                    Success(firebaseUser)
                }
                is Error -> {
                    Log.e(TAG, "login ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        }
        catch (exception: Exception) {
            return Error(exception)
        }
    }

    override fun getCurrentUser(): ServiceResult<User> {
        val currentUser: FirebaseUser? = mAuth.currentUser
        return if (currentUser != null) Success(convertToUser(currentUser)) else Error("no user logged")
    }

    private fun convertToUser(firebaseUser: FirebaseUser): User {
        return User(firebaseUser.uid, firebaseUser.email?:"none", firebaseUser.displayName)
    }

    companion object {
        private val TAG = "FirebaseAuthenticationSource"
    }

}