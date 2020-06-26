package com.example.pricequote.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pricequote.utilities.TAG
import com.example.pricequote.utilities.USERS
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(USERS)

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<User> {
        val authenticatedUserMutableLiveData: MutableLiveData<User> = MutableLiveData()

        firebaseAuth.signInWithCredential(googleAuthCredential!!).addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser = authTask.result!!.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser

                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val user =
                            User(uid, name, email)
                        user.isNew = isNewUser
                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    Log.d(TAG, "authTask: ${authTask.exception?.message!!}")
                }
            }

        return authenticatedUserMutableLiveData
    }

    fun createUserInFirebaseIfNotExists(authenticatedUser: User): MutableLiveData<User> {
        val newUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        val uidRef = usersRef.document(authenticatedUser.uid!!)

        uidRef.get().addOnCompleteListener { uidTask: Task<DocumentSnapshot?> ->
                if (uidTask.isSuccessful) {
                    val document = uidTask.result
                    if (!document!!.exists()) {
                        uidRef.set(authenticatedUser).addOnCompleteListener { userCreationTask: Task<Void?> ->
                                if (userCreationTask.isSuccessful) {
                                    authenticatedUser.isCreated = true
                                    newUserMutableLiveData.setValue(authenticatedUser)
                                } else {
                                    Log.d(TAG, "userCreationTask ${userCreationTask.exception?.message!!}")
                                }
                            }
                    } else {
                        newUserMutableLiveData.setValue(authenticatedUser)
                    }
                } else {
                    Log.d(TAG, "uidTask ${uidTask.exception?.message!!}")
                }
            }
        return newUserMutableLiveData
    }
}