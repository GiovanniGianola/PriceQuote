package com.example.pricequote.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pricequote.ui.auth.User


class SplashViewModel(app: Application) : AndroidViewModel(app) {
    private val splashRepository: SplashRepository = SplashRepository()
    var isUserAuthenticatedLiveData: LiveData<User>? = null
    var userLiveData: LiveData<User>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase()
    }

    fun setUid(uid: String?) {
        userLiveData = splashRepository.addUserToLiveData(uid)
    }

}