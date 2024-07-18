package com.hit.gymtime.ui.single_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.hit.gymtime.databinding.DetailItemLayoutBinding
import com.hit.gymtime.ui.ItemsViewModel

class DetailItemFragment : Fragment() {
    var _binding : DetailItemLayoutBinding? = null

    val viewModel: ItemsViewModel by activityViewModels()
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailItemLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenItem.observe(viewLifecycleOwner){
            binding.itemTitle.text = it.date
            binding.itemDes.text = it.hour
            Glide.with(requireContext()).load(it.photo).circleCrop().into(binding.itemImg)
        }

//        arguments?.getInt("item")?.let {
//            val item = ItemManager.items[it]
//
//            binding.itemTitle.text = item.title
//            binding.itemDes.text = item.description
//            Glide.with(requireContext()).load(item.photo).circleCrop().into(binding.itemImg)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}