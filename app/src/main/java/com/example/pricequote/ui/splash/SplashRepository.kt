package com.example.pricequote.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pricequote.utilities.TAG
import com.example.pricequote.utilities.USERS
import com.example.pricequote.ui.auth.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class SplashRepository {

    private val NTAG = "$TAG - SplashRepo"
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val user: User =
        User()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(USERS)

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<User> {
        val isUserAuthenticateInFirebaseMutableLiveData: MutableLiveData<User> = MutableLiveData()
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            user.isAuthenticated = false
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        } else {
            user.uid = firebaseUser.uid
            user.isAuthenticated = true
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        }

        return isUserAuthenticateInFirebaseMutableLiveData
    }

    fun addUserToLiveData(uid: String?): MutableLiveData<User> {
        val userMutableLiveData: MutableLiveData<User> = MutableLiveData()
        val uidRef = usersRef.document(uid!!)

        Log.i(NTAG, "UID: $uid")
        uidRef.get().addOnCompleteListener { userTask: Task<DocumentSnapshot?> ->
            Log.i(NTAG, "isSuccessful: ${userTask.isSuccessful}")
            if (userTask.isSuccessful) {
                val document = userTask.result
                if (document!!.exists()) {
                    Log.i(NTAG, "DocumentSnapshot data: " + userTask.result!!.data)
                    val user: User? = document.toObject(
                        User::class.java)
                    userMutableLiveData.value = user
                } else {
                    Log.d(NTAG, "No such document")
                }

            } else {
                Log.d(NTAG, "userTask: ${userTask.exception?.message!!}")
            }
        }

        return userMutableLiveData
    }
}