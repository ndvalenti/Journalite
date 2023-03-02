package com.nvalenti.journalite.ui.journal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvalenti.journalite.MainViewModel
import com.nvalenti.journalite.databinding.FragmentJournalBinding

class JournalFragment : Fragment() {
    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    lateinit var journalAdapter: JournalAdapter

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
        journalAdapter = JournalAdapter(viewModel.journal.journalEntries)

        binding.journalRV.adapter = journalAdapter
        binding.journalRV.layoutManager = LinearLayoutManager(requireContext())

        binding.journalTestButton.setOnClickListener {
            viewModel.journal.addEntry()
            journalAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
       _binding = null
    }
}