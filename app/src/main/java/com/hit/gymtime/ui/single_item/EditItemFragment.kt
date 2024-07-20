package com.hit.gymtime.ui.single_item

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.hit.gymtime.R
import com.hit.gymtime.data.models.Item
import com.hit.gymtime.databinding.EditItemLayoutBinding
import com.hit.gymtime.ui.ItemsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditItemFragment : Fragment() {

    private var _binding : EditItemLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ItemsViewModel by activityViewModels()

    private var imageUri: Uri? = null
    val pickImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
            binding.resImg.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            imageUri = it
        }

    private var selectedWorkoutType: String? = null
    private var selectedWorkoutLocation: String? = null

    private var selectedId : Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditItemLayoutBinding.inflate(inflater, container, false)

        binding.saveBtn.setOnClickListener{

            val item = Item(binding.dateTextview.text.toString(),
                binding.hourTextview.text.toString(),
                selectedWorkoutType,
                selectedWorkoutLocation,
                binding.contactTextview.text.toString(),
                imageUri?.toString())

            item.id = selectedId

            viewModel.updateItem(item)

            findNavController().navigate(R.id.action_editItemFragment_to_allItemsFragment)
        }

        binding.imageBtn.setOnClickListener{
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.contactButton.setOnClickListener {
            val bundle = bundleOf("date" to binding.dateTextview.text.toString(), "hour" to binding.hourTextview.text.toString(), "screen" to "edit")
            findNavController().navigate(R.id.action_editItemFragment_to_contactsFragment, bundle)
        }

        val selectedCities = getSelectedCities(requireContext())
        viewModel.updateGyms(selectedCities.toList())

        setupDateAndTimePickers()
        setupSpinners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenItem.observe(viewLifecycleOwner) {
            binding.dateTextview.text = arguments?.getString("date") ?: it.date
            binding.hourTextview.text = arguments?.getString("hour") ?: it.hour
            binding.contactTextview.text = arguments?.getString("contactName") ?: it.partner
            binding.workoutType.setSelection(viewModel.workoutTypes.indexOf(it.type))
            it.location?.let { it1 ->
                viewModel.gyms.value?.indexOf(it1)
            }?.let { it2 -> binding.workoutLocation.setSelection(it2) }

            selectedId = it.id

            if(it.photo == null){
                binding.resImg.setImageResource(R.drawable.ic_launcher_background)
                imageUri = null
            }
            else{
                binding.resImg.setImageURI(Uri.parse(it.photo))
                imageUri = Uri.parse(it.photo)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setupDateAndTimePickers() {
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            binding.dateTextview.text = dateFormat.format(calendar.time)
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
            binding.hourTextview.text = timeFormat.format(calendar.time)
        }

        binding.dateButton.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.hourButton.setOnClickListener {
            TimePickerDialog(requireContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                Calendar.MINUTE), true).show()
        }
    }

    private fun setupSpinners() {
        binding.workoutType.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewModel.workoutTypes)
        binding.workoutLocation.adapter = viewModel.gyms.value?.let {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it.toList()
            )
        }

        binding.workoutType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedWorkoutType = viewModel.workoutTypes[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedWorkoutType = null
            }
        }

        binding.workoutLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedWorkoutLocation = viewModel.gyms.value?.let {
                    it[p2]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedWorkoutLocation = null
            }
        }
    }

    private fun getSelectedCities(context: Context): Set<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getStringSet("city_preference", emptySet()) ?: emptySet()
    }
}