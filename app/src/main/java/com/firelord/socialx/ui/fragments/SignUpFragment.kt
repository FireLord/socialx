package com.firelord.socialx.ui.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.firelord.socialx.R
import com.firelord.socialx.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        // config progress dialog
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("creating account in..")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // handle click, begin sign up
        binding.btSignUp.setOnClickListener {
            //validate data
            validateData()
        }


        return binding.root
    }

    private fun validateData() {
        // get data
        email = binding.etEmailSignUp.text.toString().trim()
        password = binding.etPassSignUp.text.toString().trim()

        // validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            binding.etEmailSignUp.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            // no password entered
            binding.etPassSignUp.error = "please enter password"
        }
        else if (password.length <6){
            // password length is less than 6
            binding.etPassSignUp.error = "Password must be atleast 6 char long"
        }
        else {
            // data is validated, continue sign up
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        progressDialog.show()

        // create account
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //signup succss
                progressDialog.dismiss()
                // get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(activity,"Account created with email $email",Toast.LENGTH_SHORT).show()

                // open profile
                view?.findNavController()?.navigate(R.id.action_tabsFragment_to_newsHomeFragment)
            }
            .addOnFailureListener { e->
                //sign up failed
                progressDialog.dismiss()
                Toast.makeText(activity,"Sign up Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}