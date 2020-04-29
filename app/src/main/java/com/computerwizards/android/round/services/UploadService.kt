package com.computerwizards.android.round.services

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.computerwizards.android.round.R
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.ui.MainActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.android.AndroidInjection
import javax.inject.Inject

class UploadService : MyBaseTaskService() {

    private lateinit var storageRef: StorageReference

    @Inject
    lateinit var user: User

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        storageRef = Firebase.storage.reference
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand:$intent:$startId")
        if (ACTION_UPLOAD == intent.action) {
            val fileUri = intent.getParcelableExtra<Uri>(EXTRA_FILE_URI)
            val isProfilePicture = intent.getBooleanExtra(EXTRA_PROFILE_BOOLEAN, false)

            // Make sure we have permission to read the data
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                contentResolver.takePersistableUriPermission(
                    fileUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                uploadFromUri(fileUri, isProfilePicture)
            }

        }

        return START_REDELIVER_INTENT
    }

    private fun uploadFromUri(fileUri: Uri, isProfilePicture: Boolean = false) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString())

        taskStarted()
        showProgressNotification(getString(R.string.progress_uploading), 0, 0)

        // Get a reference to store file at photos/<FILENAME>.jpg
        var mediaPath = "photos/${user.uid}"
        fileUri.lastPathSegment?.let {
            if (isProfilePicture) {
                mediaPath += "/profilePic"
            }
            val photoRef = storageRef.child(mediaPath)
                .child(it)

            // Upload file to Firebase Storage
            Log.d(TAG, "uploadFromUri:dst:" + photoRef.path)
            photoRef.putFile(fileUri).addOnProgressListener { taskSnapshot ->
                showProgressNotification(
                    getString(R.string.progress_uploading),
                    taskSnapshot.bytesTransferred,
                    taskSnapshot.totalByteCount
                )
            }.continueWithTask { task ->
                // Forward any exceptions
                if (!task.isSuccessful) {
                    throw task.exception!!
                }

                Log.d(TAG, "uploadFromUri: upload success")

                // Request the public download URL
                photoRef.downloadUrl
            }.addOnSuccessListener { downloadUri ->
                // Upload succeeded
                Log.d(TAG, "uploadFromUri: getDownloadUri success")

                // [START_EXCLUDE]
                broadcastUploadFinished(downloadUri, fileUri)
                showUploadFinishedNotification(downloadUri, fileUri)
                taskCompleted()
                // [END_EXCLUDE]
            }.addOnFailureListener { exception ->
                // Upload failed
                Log.w(TAG, "uploadFromUri:onFailure", exception)

                broadcastUploadFinished(null, fileUri)
                showUploadFinishedNotification(null, fileUri)
                taskCompleted()
            }
        }
    }

    /*
    * Broadcast finished upload (success or failure).
    * @return true if a running reciever received the broadcast.
    * */
    private fun broadcastUploadFinished(downloadUrl: Uri?, fileUri: Uri?): Boolean {
        val success = downloadUrl != null

        val action = if (success) UPLOAD_COMPLETED else UPLOAD_ERROR

        val broadcast = Intent(action)
            .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
            .putExtra(EXTRA_FILE_URI, fileUri)
        return LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(broadcast)
    }

    /*
    * Show a notification for a finished upload.
    * */
    private fun showUploadFinishedNotification(downloadUrl: Uri?, fileUri: Uri?) {
        // Hide the progress notification
        dismissProgressNotification()

        // Make Intent to MainActivity
        val intent = Intent(this, MainActivity::class.java)
            .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
            .putExtra(EXTRA_FILE_URI, fileUri)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val success = downloadUrl != null
        val caption =
            if (success) getString(R.string.upload_finished) else getString(R.string.upload_failed)
        showFinishedNotification(caption, intent, success)
    }

    companion object {

        private const val TAG = "UploadService"

        /** Intent Actions */
        const val ACTION_UPLOAD = "action_upload"
        const val UPLOAD_COMPLETED = "upload_completed"
        const val UPLOAD_ERROR = "upload_error"

        /* Intent Extras */
        const val EXTRA_FILE_URI = "extra_file_uri"
        const val EXTRA_DOWNLOAD_URL = "extra_download_url"
        const val EXTRA_PROFILE_BOOLEAN = "extra_profile_boolean"

        val intentFilter: IntentFilter
            get() {
                val filter = IntentFilter()
                filter.addAction(UPLOAD_COMPLETED)
                filter.addAction(UPLOAD_ERROR)

                return filter
            }
    }
}
