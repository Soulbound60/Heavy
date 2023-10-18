package com.imsiu.heavy.Helper

import ClientModel
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsiu.heavy.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import toClientModel

class FireBaseViewxModel(context1: Context) {

    // Firebase authentication instance to manage authentication processes
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to observe login state, true for success and false for failure
    var loginState = MutableLiveData<Boolean>()

    // LiveData to observe signup state, true for success and false for failure
    var signUpstatues = MutableLiveData<Boolean>()

    /**
     * Create a new user in Firebase using given clientModel.
     */
    fun createUser(clientModel: ClientModel) {
        val db = Firebase.firestore

        // Attempt to create a user using email and password from clientModel
        mAuth.createUserWithEmailAndPassword(clientModel.email, clientModel.password)
            .addOnCompleteListener(
                MainActivity()
            ) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = mAuth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenResult ->
                        if (tokenResult.isSuccessful) {
                           // val token = tokenResult.result?.token

                            Log.d("Test123","")
                            // Save user data to Firestore under 'Users' collection
                            db.collection("Users").document(user!!.uid).set(clientModel)
                                .addOnSuccessListener {

                                    signUpstatues.value = true
                                }
                                .addOnFailureListener { e ->

                                }
                        } else {

                        }
                    }
                } else {

                }
            }
    }

    /**
     * Login user using provided email and password.
     */
    fun loginUser(email: String, password: String) {
        Log.d("TAG123"," Login function $email , $password")
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener {
                Log.d("TAG123"," Failed ")
                Log.d("TagFailed"," ${it.message} ")
            }

            .addOnCompleteListener(MainActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG123"," Susccess")
                    val user: FirebaseUser? = mAuth.currentUser

                    user?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
                        val token = tokenResult.token

                        loginState.value = true  // Notify observers of successful login

                        CoroutineScope(Dispatchers.IO).launch {
                            ConstantClient = fetchUserInfo(user.uid)!!
                        }
                        ConstantUID = user.uid
                    }
                        ?.addOnFailureListener {
                            Toast.makeText(MainActivity(), "${task.exception?.message}", Toast.LENGTH_SHORT)
                                .show()

                        }
                } else {

                    loginState.value = false  // Notify observers of login failure
                }
            }
    }

    /**
     * Send password reset email to provided email.
     */
    fun forgetPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    /**
     * Fetch user information from Firestore using the given UID.
     */
    suspend fun fetchUserInfo(uid: String): ClientModel? = withContext(Dispatchers.IO) {
        val db = Firebase.firestore
        return@withContext try {
            val document = Tasks.await(db.collection("Users").document(uid).get())
            if (document.exists()) {
                val clientModel = document.toClientModel()

                clientModel
            } else {

                null
            }
        } catch (exception: Exception) {

            null
        }
    }
}
