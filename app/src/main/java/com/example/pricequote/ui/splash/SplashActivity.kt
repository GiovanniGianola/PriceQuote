package com.example.pricequote.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.pricequote.utilities.USER
import com.example.pricequote.ui.auth.User
import com.example.pricequote.ui.list.ListActivity
import com.example.pricequote.ui.auth.AuthActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initSplashViewModel()
        checkIfUserIsAuthenticated()
    }

    private fun initSplashViewModel() {
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    private fun checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated()
        splashViewModel.isUserAuthenticatedLiveData?.observe(this, Observer { user ->
            Log.i(com.example.pricequote.utilities.TAG,"User auth: ${user.isAuthenticated}")
            if (!user.isAuthenticated) {
                goToAuthInActivity()
                finish()
            } else {
                getUserFromDatabase(user.uid)
            }
        })
    }

    private fun goToAuthInActivity() {
        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
        startActivity(intent)
    }

    private fun getUserFromDatabase(uid: String?) {
        splashViewModel.setUid(uid)
        splashViewModel.userLiveData?.observe(this, Observer { user ->
            Toast.makeText(this, "Welcome back ${user.name}!", Toast.LENGTH_LONG).show()
            goToMainActivity(user)
            finish()
        })
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this@SplashActivity, ListActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
    }
}