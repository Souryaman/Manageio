package com.example.manageio.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.manageio.R
import com.example.manageio.firebase.FirestoreClass
import com.example.manageio.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        register_login_text.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
        register_registerBtn.setOnClickListener {
            registerUserWithDetails()
        }
    }

    private fun validateRegisterDetails(name : String, email : String, password : String) : Boolean{
        return when {
            TextUtils.isEmpty(register_etUsername.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar("Enter Username",true)
                false
            }
            TextUtils.isEmpty(register_et_email.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Enter Email",true)
                false
            }
            TextUtils.isEmpty(register_et_password.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Enter Password",true)
                false
            }
            else ->{
                true
            }
        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this,"You have successfully registered",Toast.LENGTH_LONG).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun registerUserWithDetails(){
        val name : String = register_etUsername.text.toString().trim(){it <= ' '}
        val email : String = register_et_email.text.toString().trim{it <= ' '}
        val password : String = register_et_password.text.toString().trim(){it <= ' '}

        if(validateRegisterDetails(name,email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))


            // Creating Instance to register with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid,name,registeredEmail)
                        FirestoreClass().registerUser(this,user)
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)

                        }
                    }
                }
        }
}
