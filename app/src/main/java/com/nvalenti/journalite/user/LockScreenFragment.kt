package com.nvalenti.journalite.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nvalenti.journalite.MainViewModel
import com.nvalenti.journalite.databinding.FragmentLockscreenBinding

class LockScreenFragment : Fragment() {
    private var _binding: FragmentLockscreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    //private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLockscreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        requireActivity().actionBar?.let {
            it.setHomeButtonEnabled(false)
        }
         */

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.unlock()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}