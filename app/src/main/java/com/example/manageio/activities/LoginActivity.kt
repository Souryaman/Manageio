package com.example.manageio.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.manageio.R
import com.example.manageio.firebase.FirestoreClass
import com.example.manageio.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        login_register_text.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        login_forgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        login_loginBtn.setOnClickListener {
            logInRegisteredUser()
        }
    }

    fun loginSuccess(user : User){
        Toast.makeText(this,"You are Logged in Successfully ", Toast.LENGTH_LONG).show()
        hideProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun validateLoginDetails(): Boolean {
        return when {

            TextUtils.isEmpty(login_et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Enter Email", true)
                false
            }
            TextUtils.isEmpty(login_et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Enter Password", true)
                false
            }
            else -> {
                true
            }
        }

    }

    private fun logInRegisteredUser(){
        if(validateLoginDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = login_et_email.text.toString().trim { it <= ' ' }
            val password: String = login_et_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    hideProgressDialog()

                    if (task.isSuccessful) {
                        FirestoreClass().loadUserData(this@LoginActivity)

                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
}