package com.nvalenti.journalite.ui.items

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.JournalApplication
import com.nvalenti.journalite.MainViewModel
import com.nvalenti.journalite.MainViewModelFactory
import com.nvalenti.journalite.databinding.FragmentItemsBinding
import kotlinx.coroutines.launch
import java.util.*

class ItemsFragment : Fragment() {
    private var _binding: FragmentItemsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as JournalApplication).database.journalDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentItemsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainViewModel.navBarVisible.value = true

        recyclerView = binding.itemsRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemsAdapter = ItemsAdapter()
        recyclerView.adapter = itemsAdapter

        lifecycle.coroutineScope.launch {
            viewModel.journalItems().collect {
                itemsAdapter.submitList(it)
            }
        }

        binding.itemsAddFAB.setOnClickListener {
            val action = ItemsFragmentDirections.actionGlobalItemDetailFragment(UUID.randomUUID())
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}