package com.nvalenti.journalite.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.JournalApplication
import com.nvalenti.journalite.MainViewModel
import com.nvalenti.journalite.MainViewModelFactory
import com.nvalenti.journalite.databinding.FragmentJournalBinding
import kotlinx.coroutines.launch

class JournalFragment : Fragment() {
    private var _binding: FragmentJournalBinding? = null
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
        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainViewModel.navBarVisible.value = true

        recyclerView = binding.journalRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val journalAdapter = JournalAdapter()
        recyclerView.adapter = journalAdapter

        lifecycle.coroutineScope.launch {
            viewModel.journalEntries().collect { entry ->
                val adapterList = entry.sortedBy { it.dueDate }
                journalAdapter.submitList(adapterList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
       _binding = null
    }
}