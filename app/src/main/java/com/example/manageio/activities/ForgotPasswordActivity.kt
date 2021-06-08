package com.example.manageio.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.manageio.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        forgot_submitBtn.setOnClickListener {
            helpRetreivePassword()
        }
    }

    private fun validateForgotPasswordDetails() : Boolean{
        return when {
            TextUtils.isEmpty(forgot_et_email.text.toString().trim(){it<=' '}) -> {
                showErrorSnackBar("Enter Email",true)
                false

            }
            else ->{
                true
            }
        }
    }

    private fun helpRetreivePassword(){

        if(validateForgotPasswordDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val email : String = forgot_et_email.text.toString().trim{it<=' '}

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                hideProgressDialog()
                if(task.isSuccessful){
                    Toast.makeText(this,"Email sent successfully",Toast.LENGTH_LONG).show()
                    finish()
                }
                else{
                    showErrorSnackBar(task.exception!!.message.toString(),true)
                }
            }
        }
    }
}