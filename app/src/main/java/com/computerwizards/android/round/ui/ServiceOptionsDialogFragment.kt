package com.computerwizards.android.round.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.computerwizards.android.round.databinding.ServiceOptionsBinding
import com.computerwizards.android.round.model.Service
import com.computerwizards.android.round.utils.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ServiceOptionsDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ServiceOptionsViewModel> { viewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ServiceOptionsBinding.inflate(
            inflater,
            container,
            false
        )

        binding.viewModel = viewModel

        val args = requireArguments().getParcelable<Service>("service")


        binding.service = args

        return binding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            dismiss()
        })
        viewModel.deleteServiceEvent.observe(viewLifecycleOwner, EventObserver {
            val toast = Toast.makeText(
                context, "${it.name} deleted from your service",
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        })
    }


}