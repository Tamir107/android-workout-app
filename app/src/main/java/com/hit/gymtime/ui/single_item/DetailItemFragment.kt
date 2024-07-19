package com.hit.gymtime.ui.single_item

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
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
        }
    }

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
            binding.itemDate.text = it.date
            binding.itemHour.text = it.hour
            binding.itemType.text = it.type
            binding.itemLocation.text = it.location
            binding.itemAddress.text = viewModel.addresses[it.location]
            Glide.with(requireContext()).load(it.photo).circleCrop().into(binding.itemImg)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLocationUpdates() {
        viewModel.location.observe(viewLifecycleOwner) {
            val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$it&destination=${Uri.encode(binding.itemLocation.text.toString())}&travelmode=driving")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
    }
}

