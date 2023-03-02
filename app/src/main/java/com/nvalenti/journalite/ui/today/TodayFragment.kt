package com.nvalenti.journalite.ui.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvalenti.journalite.MainViewModel
import com.nvalenti.journalite.databinding.FragmentTodayBinding

// TODO: Dialog Fragments to pop up a modal window and enter data
class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    lateinit var todayAdapter: TodayAdapter

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
        todayAdapter = TodayAdapter(viewModel.journalTasks)

        binding.todayRV.adapter = todayAdapter
        binding.todayRV.layoutManager = LinearLayoutManager(requireContext())

        binding.todayTestButton.setOnClickListener {
            viewModel.journal.addTask()
            todayAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}