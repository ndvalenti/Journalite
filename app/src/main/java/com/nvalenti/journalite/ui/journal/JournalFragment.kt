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
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.controller.JournalEntryBlock
import com.nvalenti.journalite.databinding.FragmentJournalBinding
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    /*
    fun loadData(function: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val blockList = mutableListOf<JournalEntryBlock>()
            val entry = viewModel.getEntrySnapshot()

            entry.sortedBy { it.dueDate }

            var itDate: LocalDate? = null
            val tempEntries = mutableListOf<JournalEntry>()

            entry.forEach {
                if (itDate == null) {
                    itDate = it.dueDate.toLocalDate()
                }

                if (it.dueDate.toLocalDate() == itDate) {
                    tempEntries.add(it)
                    if (entry.last() == it) {
                        blockList.add(JournalEntryBlock(itDate, tempEntries))
                    }
                } else {
                    blockList.add(JournalEntryBlock(itDate, tempEntries))
                    tempEntries.clear()
                    tempEntries.add(it)
                    itDate = it.dueDate.toLocalDate()
                    if (entry.last() == it) {
                        tempEntries.add(it)
                        blockList.add(JournalEntryBlock(itDate, tempEntries))
                    }
                }
            }
            journalBlockAdapter.blocks = blockList
            requireActivity().runOnUiThread {
                function()
            }
        }
    }
    */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainViewModel.navBarVisible.value = true

        recyclerView = binding.journalRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val journalBlockAdapter = JournalBlockAdapter()
        recyclerView.adapter = journalBlockAdapter


        /***/
        lifecycle.coroutineScope.launch {
            viewModel.journalEntries().collect { entry ->
                /***/
                val blockList = mutableListOf<JournalEntryBlock>()
                val sortedEntry = entry.sortedBy { it.dueDate }

                var itDate: LocalDate? = null
                val tempEntries = mutableListOf<JournalEntry>()

                sortedEntry.forEach {
                    if (itDate == null) {
                        itDate = it.dueDate.toLocalDate()
                    }

                    if (it.dueDate.toLocalDate() == itDate) {
                        tempEntries.add(it)
                        if (entry.last() == it) {
                            blockList.add(JournalEntryBlock(itDate, tempEntries))
                        }
                    } else {
                        blockList.add(JournalEntryBlock(itDate, tempEntries.toMutableList()))
                        tempEntries.clear()
                        tempEntries.add(it)
                        itDate = it.dueDate.toLocalDate()
                        if (entry.last() == it) {
                            //tempEntries.add(it)
                            blockList.add(JournalEntryBlock(itDate, tempEntries))
                        }
                    }
                }
                blockList.reverse()

                // AT THIS POINT blockList should contain a list of lists that can be passed to adapters
                journalBlockAdapter.submitList(blockList)

                //val journalBlockAdapter = JournalBlockAdapter(blockList)
                //recyclerView.adapter = journalBlockAdapter

                /***/
                /*
                val adapterList = entry.sortedBy { it.dueDate }
                journalBlockAdapter.submitList(adapterList)
                 */
            }
        }

        /*
        lifecycle.coroutineScope.launch {
            viewModel.journalEntries().collect { entry ->
                /***/
                val blockList = mutableListOf<JournalEntryBlock>()
                //CoroutineScope(Dispatchers.IO).launch {
                //val entry = viewModel.getEntrySnapshot()
                entry.sortedBy { it.dueDate }

                var itDate: LocalDate? = null
                //val blockList = mutableListOf<JournalEntryBlock>()
                val tempEntries = mutableListOf<JournalEntry>()

                entry.forEach {
                    if (itDate == null) {
                        itDate = it.dueDate.toLocalDate()
                    }

                    if (it.dueDate.toLocalDate() == itDate) {
                        tempEntries.add(it)
                    } else {
                        blockList.add(JournalEntryBlock(itDate, tempEntries))
                        tempEntries.clear()
                        tempEntries.add(it)
                        itDate = it.dueDate.toLocalDate()
                        if (entry.last() == it) {
                            tempEntries.add(it)
                        }
                    }
                }
                requireActivity().runOnUiThread {
                    journalBlockAdapter.blocks = blockList
                    journalBlockAdapter.notifyDataSetChanged()
                }


                // AT THIS POINT blockList should contain a list of lists that can be passed to adapters
                //journalBlockAdapter.submitList(blockList)

                //val journalBlockAdapter = JournalBlockAdapter(blockList)
                //recyclerView.adapter = journalBlockAdapter

                /***/
                /*
                val adapterList = entry.sortedBy { it.dueDate }
                journalBlockAdapter.submitList(adapterList)
                 */
            }
        }

         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}