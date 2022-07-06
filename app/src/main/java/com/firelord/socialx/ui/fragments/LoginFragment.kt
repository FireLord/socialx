package com.firelord.socialx.ui.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.firelord.socialx.R
import com.firelord.socialx.databinding.FragmentLoginBinding
import com.firelord.socialx.util.Constants.Companion.RC_SIGN_IN
import com.firelord.socialx.util.Constants.Companion.TAG
import com.firelord.socialx.util.Constants.Companion.TAG_FB
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    // FireBase Auth
    private lateinit var firebaseAuth : FirebaseAuth
    private var email = ""
    private var password = ""

    // Googel Auth
    private lateinit var googleSignInClient: GoogleSignInClient

    // facebook
    var callbackManager = create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // config progress dialog
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in..")
        progressDialog.setCanceledOnTouchOutside(false)

        // call google
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),googleSignInOption)

        // call facebook
        facebookLogin()

        // init firebaseauth
        firebaseAuth = FirebaseAuth.getInstance()

        // Google Sign in button
        binding.btGoogle.setOnClickListener {
            Log.d(TAG,"onCreate: begin google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        // handle click, open sign up page
        binding.tvNoAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_tabsFragment_to_signUpFragment)
        }

        // handle click, begin login
        binding.btLogin.setOnClickListener{
            //before logging in, validate data
            validateData()

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Request returned from launching the intent
        if (requestCode == RC_SIGN_IN){
            Log.d(TAG,"onActivityResult: Google sign in intent")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign in success
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e:Exception){
                // failed google signin
                Log.d(TAG,"onActivityResult: ${e.message}")
            }
        }
        // Pass the activity result back to the Facebook SDK
        if (facebookLoginCLick) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin firebase auth with google")
        val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //login success
                Log.d(TAG,"firebaseAuthWithGoogleAccount: LoggedIn")

                // Get logged in user
                val firebaseUser = firebaseAuth.currentUser

                // get user id
                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email

                Log.d(TAG,"firebaseAuthWithGoogleAccount: uid: ${uid}")
                Log.d(TAG,"firebaseAuthWithGoogleAccount: email: ${email}")

                //check if user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser){
                    //user is new - account created
                    Log.d(TAG,"firebaseAuthWithGoogleAccount: Account created ...\n$email")
                    Toast.makeText(activity,"Account created $email",Toast.LENGTH_SHORT).show()
                } else {
                    // existing user - logged in
                    Log.d(TAG,"firebaseAuthWithGoogleAccount: Existing user \n$email")
                    Toast.makeText(activity, "Logged in as $email",Toast.LENGTH_SHORT).show()
                }
                // open newsHome
                view?.findNavController()?.navigate(R.id.action_tabsFragment_to_newsHomeFragment)
            }
            .addOnFailureListener { e ->
                Log.d(TAG,"firebaseAuthWithGoogleAccount: Login failed due to ${e.message}")
                Toast.makeText(activity, "Login failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateData() {
        email = binding.etEmail.text.toString().trim()
        password = binding.etPass.text.toString().trim()

        // validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            binding.etEmail.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            // no password entered
            binding.etPass.error = "please enter password"
        }
        else {
            // data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        // show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()

                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(activity, "Logged in as $email",Toast.LENGTH_SHORT).show()

                // open newsHome
                view?.findNavController()?.navigate(R.id.action_tabsFragment_to_newsHomeFragment)
            }
            .addOnFailureListener { e ->
                // login failed
                progressDialog.dismiss()
                Toast.makeText(activity, "Login failed due to ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    var facebookLoginCLick = false

    // Facebook login methids
    private fun facebookLogin(){
        facebookLoginCLick = true

        binding.btFacebook.setOnClickListener {
            if(userLoggedIn()){
                firebaseAuth.signOut()
            }else{
                LoginManager.getInstance().logInWithReadPermissions(requireActivity(), listOf("punlic_profile","email"))
            }

        }

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG_FB, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG_FB, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG_FB, "facebook:onError", error)
            }
        })
    }

    private fun userLoggedIn(): Boolean {
        if(firebaseAuth.currentUser!=null && AccessToken.getCurrentAccessToken()!!.isExpired){
            return true
        }
        return false
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser

                    val email = user!!.email
                    Log.d(TAG,"fbSignInWithCredential: email: ${email}")

                    // open newsHome
                    view?.findNavController()?.navigate(R.id.action_tabsFragment_to_newsHomeFragment)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}