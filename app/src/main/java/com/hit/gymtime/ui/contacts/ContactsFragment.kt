package com.hit.gymtime.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hit.gymtime.R
import com.hit.gymtime.databinding.ContactsLayoutBinding

class ContactsFragment : Fragment() {

    var _binding : ContactsLayoutBinding? = null
    val binding get() = _binding!!

    private lateinit var _adapter: MyContactsAdapter

    private val contactsPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it)
            getContacts()
    }

    private val viewModel : ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ContactsLayoutBinding.inflate(inflater,container,false)

        initRecycler()

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        } else {
            getContacts()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        _adapter = MyContactsAdapter(listOf()) {
            Toast.makeText(requireContext(),it.toString(), Toast.LENGTH_SHORT).show()
            val bundle = bundleOf("contactName" to it.name, "date" to arguments?.getString("date"), "hour" to arguments?.getString("hour"))
            Log.d("FragmentC", "Source Fragment: ${arguments?.getString("screen")}")
            when(arguments?.getString("screen")){
                "edit" -> {
                    findNavController().navigate(R.id.action_contactsFragment_to_editItemFragment,bundle)
                }
                "add" -> {
                    findNavController().navigate(R.id.action_contactsFragment_to_additemFragment,bundle)
                }
            }
        }
        binding.recycler.apply {
            adapter = _adapter
            layoutManager = LinearLayoutManager(this@ContactsFragment.requireContext())
        }
    }

    private fun getContacts() {
        viewModel.contacts.observe(viewLifecycleOwner) {
            when(it) {
                is ContactResult.Loading -> {
                    binding.progress.isVisible = true
                }
                is ContactResult.Success -> {
                    binding.progress.isVisible = false
                    _adapter.setList(it.contacts)
                }
                is ContactResult.Failure -> {
                    binding.progress.isVisible = false
                    Snackbar.make(binding.root,it.message,Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}