package com.hit.gymtime.ui.single_item

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.hit.gymtime.R

import com.hit.gymtime.databinding.DetailItemLayoutBinding
import com.hit.gymtime.ui.ItemsViewModel

class DetailItemFragment : Fragment() {
    private var _binding : DetailItemLayoutBinding? = null

    private val viewModel: ItemsViewModel by activityViewModels()
    private val binding get() = _binding!!

    private val locationRequestLauncher : ActivityResultLauncher<String>
    = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it){
            getLocationUpdates()
        } else {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
            {
                showPermissionDeniedDialog()
            } else {
                showPermissionRationaleDialog()
            }
        }
    }

    private var gymAddress : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailItemLayoutBinding.inflate(inflater,container,false)

        binding.mapButton.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdates()
            }else{
                locationRequestLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenItem.observe(viewLifecycleOwner){
            gymAddress = viewModel.addresses[it.location]

            val partnerInfo = if(it.partner == "") "." else getString(R.string.with_your_partner) + it.partner + "."

            binding.itemDescription.text =
                getString(R.string.workout_session_scheduled_on) +
                        it.date + getString(R.string.at) + it.hour +
                        partnerInfo + "\n\n" + getString(R.string.the_gym_is_located_at) +
                        gymAddress + ".\n\n" + getString(R.string.enjoy_your_workout_remember_every_step_brings_you_closer_to_your_goal)

            if (it.photo == null) {
                Glide.with(requireContext()).load(R.drawable.motivation).into(binding.itemImg)
            } else {
                Glide.with(requireContext()).load(it.photo).into(binding.itemImg)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLocationUpdates() {
        viewModel.location.observe(viewLifecycleOwner) {
            val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$it&destination=${Uri.encode(gymAddress)}&travelmode=driving")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.location_access_is_required_for_this_feature_please_grant_the_permission))
            .setPositiveButton(getString(R.string.grant)) { dialog, _ ->
                dialog.dismiss()
                locationRequestLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_denied))
            .setMessage(getString(R.string.location_access_has_been_permanently_denied_please_enable_it_in_the_app_settings))
            .setPositiveButton(getString(R.string.open_settings)) { dialog, _ ->
                dialog.dismiss()
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }
}

