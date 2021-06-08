package com.example.manageio.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.MimeTypeFilter
import com.bumptech.glide.Glide
import com.example.manageio.R
import com.example.manageio.firebase.FirestoreClass
import com.example.manageio.models.User
import com.example.manageio.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.IOException


class ProfileActivity : BaseActivity() {


    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL: String = ""
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        FirestoreClass().loadUserData(this)
        profile_iv_userImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        profile_updateBtn.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadUserImage()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    "You denied the permission for storage.You can allow it from settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data
            try {
                Glide
                    .with(this@ProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_nav_user)
                    .into(profile_iv_userImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    fun setUserDataInUi(user: com.example.manageio.models.User) {
        mUserDetails = user
        Glide
            .with(this@ProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_nav_user)
            .into(profile_iv_userImage)

        profile_etUsername.setText(user.name)
        profile_et_email.setText(user.email)
        if (user.mobile != 0L) {
            profile_et_mobile.setText(user.mobile.toString())
        }
    }

    private fun updateUserProfileData() {
        val userHashmap = HashMap<String, Any>()

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashmap[Constants.IMAGE] = mProfileImageURL
        }

        if (profile_etUsername.text.toString() != mUserDetails.name) {
            userHashmap[Constants.NAME] = profile_etUsername.text.toString()
        }

        if (profile_et_mobile.text.toString() != mUserDetails.mobile.toString()) {
            userHashmap[Constants.MOBILE] = profile_et_mobile.text.toString().toLong()
        }

        FirestoreClass().updateUserProfileData(this, userHashmap)
    }

    private fun uploadUserImage() {
        showProgressDialog(resources.getString(R.string.please_wait))
        if (mSelectedImageFileUri != null) {
            val sRef: StorageReference = FirebaseStorage.getInstance()
                .reference.child(
                    "USER_IMAGE" + System.currentTimeMillis() + "." +
                            Constants.getFileExtension(this,mSelectedImageFileUri!!)
                )

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
                Log.i(
                    "FirebaseImageUrl",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("DownloadImageUrl", uri.toString())
                    mProfileImageURL = uri.toString()
                    updateUserProfileData()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this@ProfileActivity, exception.message, Toast.LENGTH_LONG).show()
                hideProgressDialog()
            }

        }
    }



    fun profileUpdateSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}