package com.nvalenti.journalite.ui.today

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvalenti.journalite.*
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.controller.JournalTask
import com.nvalenti.journalite.databinding.FragmentTodayBinding
import com.nvalenti.journalite.dialog.JournalEntryDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as JournalApplication).database.journalDao()
        )}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainViewModel.navBarVisible.value = true

        recyclerView = binding.todayRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val todayAdapter = TodayAdapter { task ->
            showJournalDialog(task)
        }
        recyclerView.adapter = todayAdapter

        lifecycle.coroutineScope.launch {
            viewModel.journalTasks().collect {
                todayAdapter.submitList(it)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.updateTasks()
        }
    }

    private fun showJournalDialog(task: JournalTask) {
        CoroutineScope(Dispatchers.IO).launch {
            val entry = viewModel.getEntrySnapshotById(task.entryId)
                ?: JournalEntry(task.entryId, task.itemId, task.dueDate, task.title)
            requireActivity().runOnUiThread {
                val dialog = JournalEntryDialogFragment(entry)
                dialog.show(parentFragmentManager, JournalEntryDialogFragment.TAG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}