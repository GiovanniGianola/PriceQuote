package com.example.pricequote.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pricequote.R
import com.example.pricequote.utilities.RC_SIGN_IN
import com.example.pricequote.utilities.USER
import com.example.pricequote.ui.list.ListActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider


class AuthActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initSignInButton()
        initAuthViewModel()
        initGoogleSignInClient()
    }

    private fun initSignInButton() {
        val googleSignInButton = findViewById<SignInButton>(R.id.google_sign_in_button)
        googleSignInButton.setOnClickListener { signIn() }
    }

    private fun initAuthViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(com.example.pricequote.utilities.TAG, "requestCode: $requestCode")
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(ApiException::class.java)
                googleSignInAccount?.let { getGoogleAuthCredential(it) }

            } catch (e: ApiException) {
                Log.d(com.example.pricequote.utilities.TAG, "onActivityResult CATCH: $e")
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        authViewModel.signInWithGoogle(googleAuthCredential)
        authViewModel.authenticatedUserLiveData!!.observe(this,
            Observer { authenticatedUser: User ->
                if (authenticatedUser.isNew) {
                    Log.i(com.example.pricequote.utilities.TAG, "signInWithGoogleAuthCredential")
                    createNewUser(authenticatedUser)
                } else {
                    Toast.makeText(this, "Welcome back ${authenticatedUser.name}!", Toast.LENGTH_LONG).show()
                    goToMainActivity(authenticatedUser)
                }
            }
        )
    }

    private fun createNewUser(authenticatedUser: User) {
        authViewModel.createUser(authenticatedUser)
        authViewModel.createdUserLiveData!!.observe(this,
            Observer { user: User ->
                if (user.isCreated) {
                    Toast.makeText(this,
                        "Hi ${user.name}!\nYour account was successfully created.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Log.i(com.example.pricequote.utilities.TAG, "createNewUser")
                goToMainActivity(user)
            }
        )
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this@AuthActivity, ListActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}