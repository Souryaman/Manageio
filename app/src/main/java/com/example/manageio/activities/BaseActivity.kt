package com.example.manageio.activities

import android.app.Dialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.manageio.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_progress.*
import android.os.Handler

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce =false
    private lateinit var mProgressDialog : Dialog

    fun showErrorSnackBar(message: String,errorMessage: Boolean){
        val snackbar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view

        if(errorMessage){
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,
                R.color.colorSnackBarError
            ))
        }else{
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,
                R.color.colorSnackBarSuccess
            ))
        }
        snackbar.show()
    }

    fun showProgressDialog(text:String){
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun getCurrentUserId() : String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce =true
        Toast.makeText(this,"Please click back again to exit",Toast.LENGTH_SHORT).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        },2000)
    }
}