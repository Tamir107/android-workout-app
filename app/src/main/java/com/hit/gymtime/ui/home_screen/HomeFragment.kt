package com.hit.gymtime.ui.home_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.hit.gymtime.R
import com.hit.gymtime.databinding.HomeLayoutBinding

class HomeFragment : Fragment(), MenuProvider {

    private var _binding : HomeLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomeLayoutBinding.inflate(inflater, container,false)

        binding.workoutsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allItemsFragment)
        }

        binding.preferencesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_preferencesFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_menu,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.action_info){
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("About This App").setMessage("This app provides information about [your app's functionality].").
            setPositiveButton("OK", null).show()
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}