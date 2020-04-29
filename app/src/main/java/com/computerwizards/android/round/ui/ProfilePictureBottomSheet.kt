package com.computerwizards.android.round.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.computerwizards.android.round.databinding.ProfilePictureOptionsFragmentBinding
import com.computerwizards.android.round.services.DownloadService
import com.computerwizards.android.round.services.UploadService
import com.computerwizards.android.round.utils.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

class ProfilePictureBottomSheet : BottomSheetDialogFragment() {

    private var fileUri: Uri? = null
    private var downloadUrl: Uri? = null
    private var isProfilePic: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ProfilePictureViewModel> { viewModelFactory }

    private lateinit var broadcastReciever: BroadcastReceiver


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ProfilePictureOptionsFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@ProfilePictureBottomSheet.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        broadcastReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                Log.d(TAG, "onRecieve:$intent")
//                hideProgressBar()

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

        with(viewModel) {
            openCameraEvent.observe(viewLifecycleOwner, EventObserver {
                this@ProfilePictureBottomSheet.launchCamera()
            })
            openGalleryEvent.observe(viewLifecycleOwner, EventObserver {
                this@ProfilePictureBottomSheet.selectImage()
            })
        }

        // Restore instance state
        savedInstanceState?.let {
            fileUri = it.getParcelable(KEY_FILE_URI)
            downloadUrl = it.getParcelable(KEY_DOWNLOAD_URL)
            isProfilePic = it.getBoolean(KEY_IS_PROFILE_PIC)
        }
        val mainActivity = activity as MainActivity
        mainActivity.onNewIntent(mainActivity.intent)

        return binding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onSaveInstanceState(out: Bundle) {
        super.onSaveInstanceState(out)
        out.putParcelable(KEY_FILE_URI, fileUri)
        out.putParcelable(KEY_DOWNLOAD_URL, downloadUrl)
        out.putBoolean(KEY_IS_PROFILE_PIC, isProfilePic)

    }

    private fun showMessageDialog(title: String, message: String) {
        val ad = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .create()
        ad.show()
    }


    private fun launchCamera() {

        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        startActivityForResult(
            takePicture, RC_TAKE_PHOTO
        )

    }

    private fun selectImage() {
        Log.d(TAG, "launchCamera")

        // Pick an image from storage
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, RC_TAKE_PHOTO)
    }

    private fun onUploadResultIntent(intent: Intent) {
        // Got a new intent from UploadService with a success or failure
        downloadUrl = intent.getParcelableExtra(UploadService.EXTRA_DOWNLOAD_URL)
        fileUri = intent.getParcelableExtra(UploadService.EXTRA_FILE_URI)
        isProfilePic = intent.getBooleanExtra(UploadService.EXTRA_PROFILE_BOOLEAN, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var selectedImage: Uri? = null
        when (requestCode) {
            RC_TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                selectedImage = data?.data
            }
            RC_SELECT_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                selectedImage = data?.data
            }
        }
        selectedImage?.let {
            uploadFromUri(it)
        }


    }

    private fun uploadFromUri(uploadUri: Uri) {
        Log.d(TAG, "uploadFromUri:src: $uploadUri")


        // Save the File URI
        fileUri = uploadUri

        // Clear the last-download, if any

        // Start UploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background

        (activity as MainActivity).startService(
            Intent(context, UploadService::class.java)
                .putExtra(UploadService.EXTRA_FILE_URI, uploadUri)
                .putExtra(UploadService.EXTRA_PROFILE_BOOLEAN, true)
                .setAction(UploadService.ACTION_UPLOAD)
        )
    }

    companion object {
        private const val RC_TAKE_PHOTO = 1020
        private const val RC_SELECT_IMAGE = 1010
        private const val KEY_FILE_URI = "key_file_uri"
        private const val KEY_DOWNLOAD_URL = "key_download_url"
        private const val KEY_IS_PROFILE_PIC = "key_is_profile_pic"
        private const val TAG = "ProfileDialogFragment"
    }

}


