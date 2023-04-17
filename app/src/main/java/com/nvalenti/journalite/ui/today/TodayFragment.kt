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
import com.nvalenti.journalite.ui.dialog.ConfirmDialog
import com.nvalenti.journalite.ui.dialog.JournalEntryDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var staleRecyclerView: RecyclerView
    private lateinit var currentRecyclerView: RecyclerView
    private lateinit var doneRecyclerView: RecyclerView

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

        staleRecyclerView = binding.staleRV
        staleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        currentRecyclerView = binding.todayRV
        currentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        doneRecyclerView = binding.completedRV
        doneRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val staleAdapter = TodayStaleAdapter({ task ->
            showDeleteDialog(task)
        }, { task ->
            showJournalDialog(task)
        })
        staleRecyclerView.adapter = staleAdapter

        val todayAdapter = TodayAdapter { task ->
            showJournalDialog(task)
        }
        currentRecyclerView.adapter = todayAdapter

        val doneAdapter = TodayAdapter(true) { task ->
            showJournalDialog(task)
        }
        doneRecyclerView.adapter = doneAdapter

        lifecycle.coroutineScope.launch {
            viewModel.journalTasks().collect { tasks ->
                val adapterList = tasks.filter { !it.isStale && !it.hasEntry }.sortedBy { it.dueDate }
                val staleList = tasks.filter { it.isStale }.sortedBy { it.dueDate }
                val doneList = tasks.filter { it.hasEntry }.sortedBy { it.dueDate }
                todayAdapter.submitList(adapterList)
                staleAdapter.submitList(staleList)
                doneAdapter.submitList(doneList)

                viewModel.waitingItems.value = adapterList.size + staleList.size

                if (adapterList.isNotEmpty()) {
                    _binding?.let{ it.todayEventCL.visibility = View.VISIBLE }
                } else {
                    _binding?.let{ it.todayEventCL.visibility = View.GONE }
                }

                if (staleList.isNotEmpty()) {
                    _binding?.let{ it.staleEventCL.visibility = View.VISIBLE }
                } else {
                    _binding?.let{ it.staleEventCL.visibility = View.GONE }
                }

                if (doneList.isNotEmpty()) {
                    _binding?.let{ it.completedEventCL.visibility = View.VISIBLE }
                } else {
                    _binding?.let{ it.completedEventCL.visibility = View.GONE }
                }

                if (adapterList.isNotEmpty() || staleList.isNotEmpty() || doneList.isNotEmpty()) {
                    _binding?.let{ it.todayEmptyHintTV.visibility = View.GONE }
                } else {
                    _binding?.let { it.todayEmptyHintTV.visibility = View.VISIBLE }
                }
            }
        }

        binding.todayEventCL.setOnClickListener {
            if (binding.todayRV.visibility == View.VISIBLE) {
                binding.todayRV.visibility = View.GONE
                binding.todayEventCL.setBackgroundResource(R.drawable.rounded_top_switch_all)
            } else {
                binding.todayRV.visibility = View.VISIBLE
                binding.todayEventCL.setBackgroundResource(R.drawable.rounded_top)
            }
        }
        binding.completedEventCL.setOnClickListener {
            if (binding.completedRV.visibility == View.VISIBLE) {
                binding.completedRV.visibility = View.GONE
                binding.completedEventCL.setBackgroundResource(R.drawable.rounded_top_switch_all)
            } else {
                binding.completedRV.visibility = View.VISIBLE
                binding.completedEventCL.setBackgroundResource(R.drawable.rounded_top)
            }
        }
        binding.staleEventCL.setOnClickListener {
            if (binding.staleRV.visibility == View.VISIBLE) {
                binding.staleRV.visibility = View.GONE
                binding.staleEventCL.setBackgroundResource(R.drawable.rounded_top_switch_all)
            } else {
                binding.staleRV.visibility = View.VISIBLE
                binding.staleEventCL.setBackgroundResource(R.drawable.rounded_top)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.updateTasks()
        }
    }

    private fun showDeleteDialog(task: JournalTask) {
        val confirmText = getString(R.string.delete) + " " + task.title + "?"
        val bodyText = getString(R.string.confirm_task_delete)
        val dialog = ConfirmDialog(confirmText, bodyText) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteTask(task)
            }
        }
        dialog.show(childFragmentManager, "deleteConfirm")
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