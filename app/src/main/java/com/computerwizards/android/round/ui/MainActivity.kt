package com.computerwizards.android.round.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.computerwizards.android.round.R
import com.computerwizards.android.round.services.DownloadService
import com.computerwizards.android.round.services.UploadService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var broadcastReciever: BroadcastReceiver

    private var downloadUrl: Uri? = null
    private var fileUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigationBar()


        broadcastReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                Log.d(TAG, "onRecieve:$intent")
                hideProgressBar()

                when (intent.action) {
                    DownloadService.DOWNLOAD_COMPLETED -> {
                        // Get number of bytes downloaded
                        val numBytes =
                            intent.getLongExtra(DownloadService.EXTRA_BYTES_DOWNLOADED, 0)

                        // Alert success
                        showMessageDialog(
                            "Success", String.format(
                                Locale.getDefault(),
                                "%d bytes downloaded from %s",
                                numBytes,
                                intent.getStringExtra(DownloadService.EXTRA_DOWNLOAD_PATH)
                            )
                        )
                    }
                    DownloadService.DOWNLOAD_ERROR ->
                        // Alert failure
                        showMessageDialog(
                            "Error", String.format(
                                Locale.getDefault(),
                                "Failed to download from %s",
                                intent.getStringExtra(DownloadService.EXTRA_BYTES_DOWNLOADED)
                            )
                        )

                    UploadService.UPLOAD_COMPLETED, UploadService.UPLOAD_ERROR -> onUploadResultIntent(
                        intent
                    )
                }
            }
        }

        // Restore instance state
        savedInstanceState?.let {
            fileUri = it.getParcelable(KEY_FILE_URI)
            downloadUrl = it.getParcelable(KEY_DOWNLOAD_URL)
        }
        onNewIntent(intent)

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // Check if this activity was launched by clicking on an upload notification
        if (intent.hasExtra(UploadService.EXTRA_DOWNLOAD_URL)) {
            onUploadResultIntent(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        // Register receiver for uploads and downloads
        val manager = LocalBroadcastManager.getInstance(this)
        manager.registerReceiver(broadcastReciever, DownloadService.intentFilter)
        manager.registerReceiver(broadcastReciever, UploadService.intentFilter)
    }

    override fun onStop() {
        super.onStop()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReciever)
    }

    override fun onSaveInstanceState(out: Bundle) {
        super.onSaveInstanceState(out)
        out.putParcelable(KEY_FILE_URI, fileUri)
        out.putParcelable(KEY_DOWNLOAD_URL, downloadUrl)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult:$requestCode:$resultCode:$data")
        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                fileUri = data?.data

                if (fileUri != null) {
                    uploadFromUri(fileUri!!)
                } else {
                    Log.w(TAG, "File URI is null")
                }
            } else {
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadFromUri(uploadUri: Uri) {
        Log.d(TAG, "uploadFromUri:src: $uploadUri")

        // Save the File URI
        fileUri = uploadUri

        // Clear the last-download, if any
        downloadUrl = null

        // Start UploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(
            Intent(this, UploadService::class.java)
                .putExtra(UploadService.EXTRA_FILE_URI, uploadUri)
                .setAction(UploadService.ACTION_UPLOAD)
        )

        // Show loading spinner
        showProgressBar("Uploading...")
    }

    private fun beginDownload() {
        fileUri?.let {
            // Get path
            val path = "photos/" + it.lastPathSegment

            // Kick off DownloadService to download the file
            val intent = Intent(this, DownloadService::class.java)
                .putExtra(DownloadService.EXTRA_DOWNLOAD_PATH, path)
                .setAction(DownloadService.ACTION_DOWNLOAD)
            startService(intent)

            // Show loading spinner
            showProgressBar("Downloading...")
        }
    }

    fun launchCamera() {
        Log.d(TAG, "launchCamera")

        // Pick an image from storage
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, RC_TAKE_PICTURE)
    }

    private fun onUploadResultIntent(intent: Intent) {
        // Got a new intent from UploadService with a success or failure
        downloadUrl = intent.getParcelableExtra(UploadService.EXTRA_DOWNLOAD_URL)
        fileUri = intent.getParcelableExtra(UploadService.EXTRA_FILE_URI)
    }

    private fun showMessageDialog(title: String, message: String) {
        val ad = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .create()
        ad.show()
    }

    private fun showProgressBar(progressCaption: String) {
        caption.text = progressCaption
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        caption.text = ""
        progressBar.visibility = View.INVISIBLE
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)
    }

    companion object {
        private const val TAG = "MainActivity"

        private const val RC_TAKE_PICTURE = 101

        private const val KEY_FILE_URI = "key_file_uri"
        private const val KEY_DOWNLOAD_URL = "key_download_url"
    }

}
