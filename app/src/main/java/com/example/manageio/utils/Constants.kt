package com.example.manageio.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.manageio.activities.ProfileActivity

object Constants{
    const val USERS : String = "users"
    const val BOARDS : String = "boards"
    const val IMAGE : String = "image"
    const val NAME : String = "name"
    const val MOBILE : String = "mobile"
    const val ASSIGNED_TO : String = "assignedTo"
    const val READ_STORAGE_PERMISSION_CODE = 111
    const val PICK_IMAGE_REQUEST_CODE = 222
    const val DOCUMENT_ID : String = "documentId"
    const val TASK_LIST : String = "taskList"
    const val BOARD_DETAIL : String = "board_detail"
    const val ID : String = "id"
    const val EMAIL : String = "email"
    const val BOARD_MEMBERS_LIST = "board_members_list"
    const val SELECT : String = "select"
    const val UNSELECT : String = "unSelect"
    const val TASK_LIST_ITEM_POSITION : String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION : String = "card_list_item_position"
    const val MANAGEIO_PREFERENCES : String = "ManageioPrefs"
    const val FCM_TOKEN_UPDATED : String= "fcmTokenUpdated"
    const val FCM_TOKEN : String = "fcmToken"

    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_SERVER_KEY:String = "AAAAZf_W5Vw:APA91bErg4aYqXEVFGVKKR0DbZ_-ZmvIcBVfy1jrmviCPl1yd_shWBzeWholu1zfuklGyGqgz5p9_jxmHadCWFqOoa6PkpcjdTEDFSJMXNj7YprxkJkIvLWidUuUG5tdayEi6BQVQ1Ne"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"

     fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)

    }

     fun getFileExtension(activity: Activity,uri: Uri): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}