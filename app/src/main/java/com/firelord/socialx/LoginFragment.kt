package com.firelord.socialx

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.firelord.socialx.databinding.FragmentLoginBinding
import com.firelord.socialx.databinding.FragmentTabsBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    // FireBase Auth
    private lateinit var firebaseAuth : FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)

        // config progress dialog
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in..")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebaseauth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

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
                startActivity(Intent(activity,NewsHomeFragment::class.java))
                activity?.finish()
            }
            .addOnFailureListener { e ->
                // login failed
                progressDialog.dismiss()
                Toast.makeText(activity, "Login failed due to ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    private fun checkUser(){
        // if user is already logged in go to profile activity
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            // user is already logged in
            startActivity(Intent(activity,SignUpFragment::class.java))
            activity?.finish()
        }
    }
}