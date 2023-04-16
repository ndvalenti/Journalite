package com.nvalenti.journalite.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nvalenti.journalite.JournalApplication
import com.nvalenti.journalite.MainViewModel
import com.nvalenti.journalite.MainViewModelFactory
import com.nvalenti.journalite.R
import com.nvalenti.journalite.controller.JournalEntry
import com.nvalenti.journalite.databinding.FragmentJournalEntryDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalEntryDialogFragment(private val journalEntry: JournalEntry) : BottomSheetDialogFragment() {
    private var _binding: FragmentJournalEntryDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as JournalApplication).database.journalDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJournalEntryDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.journalEditTitleTV.text = journalEntry.title
        binding.journalEditDateTV.text = journalEntry.dueDate.toLocalTime().toString()

        binding.journalEditBodyET.setText(journalEntry.entry)
        view.requestFocusFromTouch()
        binding.journalEditBodyET.setSelection(journalEntry.entry?.length ?: 0)

        binding.journalEditSubmitButton.setOnClickListener {
            journalEntry.entry = binding.journalEditBodyET.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addOrUpdateEntry(journalEntry)
                viewModel.getTaskByEntryId(journalEntry.id)?.let { task ->
                    task.hasEntry = true
                    viewModel.addOrUpdateTask(task)
                }
            }.invokeOnCompletion {
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            //dialog.behavior.isFitToContents = false
            //dialog.behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "JournalEntryDialog"
    }
}