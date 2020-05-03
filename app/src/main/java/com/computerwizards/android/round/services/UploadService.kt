package com.computerwizards.android.round.services

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.computerwizards.android.round.R
import com.computerwizards.android.round.model.User
import com.computerwizards.android.round.repository.UserRepository
import com.computerwizards.android.round.ui.MainActivity
import com.computerwizards.android.round.utils.updateDp
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class UploadService : MyBaseTaskService() {

    private lateinit var storageRef: StorageReference

    @Inject
    lateinit var user: User

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var storage: FirebaseStorage

    @Inject
    lateinit var functions: FirebaseFunctions

    private lateinit var databaseUser: User

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        userRepository.getUser(user.uid!!).observe(this, Observer { user ->
            databaseUser = user
        })

        storageRef = storage.reference

    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.d("onStartCommand:$intent:$startId")
        if (intent != null) {
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
        }

        return START_REDELIVER_INTENT
    }

    private fun uploadFromUri(fileUri: Uri, isProfilePicture: Boolean = false) {
        Timber.d("uploadFromUri:src:" + fileUri.toString())

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

            Timber.d("photoRef: $photoRef")

            // Upload file to Firebase Storage
            Timber.d("uploadFromUri:dst:" + photoRef.path)
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

                Timber.d("uploadFromUri: upload success")

                // Request the public download URL
                photoRef.downloadUrl
            }.addOnSuccessListener { downloadUri ->
                // Upload succeeded
                Timber.d("uploadFromUri: getDownloadUri success")

                // [START_EXCLUDE]
                broadcastUploadFinished(downloadUri, fileUri)
                showUploadFinishedNotification(downloadUri, fileUri)
                taskCompleted()

                if (isProfilePicture) {
                    updateDp(downloadUri.toString()).addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            throw Exception(task.exception)
                        }
                    }
                }

                // [END_EXCLUDE]
            }.addOnFailureListener { exception ->
                // Upload failed
                Timber.w("uploadFromUri:onFailure:$exception")

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
