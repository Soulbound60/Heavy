package com.imsiu.heavy.Helper

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsiu.heavy.JSON.ClientModel
import com.imsiu.heavy.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FireBaseViewxModel(context: Context) {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    fun createUser(clientModel:ClientModel) {
        val db = Firebase.firestore
        mAuth.createUserWithEmailAndPassword(clientModel.email, clientModel.password).addOnCompleteListener(
            MainActivity()
        ) { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = mAuth.currentUser
                user?.getIdToken(true)?.addOnCompleteListener { tokenResult ->
                    if (tokenResult.isSuccessful) {
                        val token = tokenResult.result?.token
                        Log.d("Read@", "Token: $token")
                        // Create a new user record in Firestore

                        Log.d("Read@!", "Users: $token")
                        // "users" is the collection name. You can change this if needed.
                        db.collection("Users").document(user!!.uid).set(clientModel)
                            .addOnSuccessListener {
                                Log.d("Firestore", "User successfully written!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error writing user", e)
                            }

                    } else {
                        Log.d("Read@", "Failed to get token")
                    }
                }
            } else {
                Log.d("Read@", "Sign up failed: ${task.exception?.message}")
            }
        }
    }
    // Add this at the top of your ViewModel
    val loginState = MutableLiveData<Boolean>()  // true for success, false for failure

    fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity()) { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = mAuth.currentUser
                Log.d("Auth", "User logged in: ${user?.email}")
                user?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
                    val token = tokenResult.token
                    Log.d("Auth", "User token: $token")
                    loginState.value = true  // Notify observers of successful login

                    GlobalScope.launch(Dispatchers.Main) {
                        val clientModel = fetchUserInfo(ConstantUID)
                        // Do something with clientModel on the main thread
                    }
                    ConstantUID = user.uid
                }
            } else {
                Log.d("Auth", "Login failed: ${task.exception?.message}")
                loginState.value = false  // Notify observers of login failure
            }
        }
    }


    fun forgetPassword(email: String){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d("TAG!@#","Susscess")
            }
            .addOnFailureListener {
                Log.d("TAG!@#","Rung")
            }
    }
    fun getCurrentUser(): FirebaseUser? {
        return mAuth.currentUser
    }
    suspend fun fetchUserInfo(uid: String): ClientModel? = withContext(Dispatchers.IO) {
        val db = Firebase.firestore
        return@withContext try {
            val document = Tasks.await(db.collection("Users").document(uid).get())
            if (document.exists()) {
                val clientModel = document.toObject(ClientModel::class.java)
                Log.d("UserInfo", "User details: $clientModel")
                clientModel
            } else {
                Log.d("UserInfo", "No such user!")
                null
            }
        } catch (exception: Exception) {
            Log.d("UserInfo", "Failed to fetch user details", exception)
            null
        }
    }



}