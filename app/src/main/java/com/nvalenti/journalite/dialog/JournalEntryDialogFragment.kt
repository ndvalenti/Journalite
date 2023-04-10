package com.nvalenti.journalite.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import com.nvalenti.journalite.controller.JournalTask
import com.nvalenti.journalite.databinding.FragmentJournalEntryDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//class JournalEntryDialogFragment(private val task: JournalTask) : BottomSheetDialogFragment() {
class JournalEntryDialogFragment(private val journalEntry: JournalEntry) : BottomSheetDialogFragment() {
    private var _binding: FragmentJournalEntryDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as JournalApplication).database.journalDao()
        )
    }
    //private val viewModel by activityViewModels<MainViewModel>()
    //private val task = parentTask

    //private lateinit var journalEntry: JournalEntry

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

        //val journalEntry = viewModel.getEntriesById(task.entryId).firstOrNull()
        //    ?: JournalEntry(task.entryId, task.itemId, task.dueDate, task.title)

        // TODO: This is clearly flawed
        /*
        val journalEntry: JournalEntry = if (task.entryId != null) {
            viewModel.getEntriesById(task.entryId!!).firstOrNull()
                ?: JournalEntry(task.itemId, task.dueDate, task.title)
        } else {
            JournalEntry(task.itemId, task.dueDate, task.title)
        }
         */
        //journalEntry = viewModel.journalEntries.firstOrNull { it.id == task.entryId }
        //    ?: JournalEntry(task.itemId, task.dueDate, task.title)

        // TODO: We need to retrieve this metadata from the item using _itemId in journalentry constructor
        binding.journalEditTitleTV.text = journalEntry.title
        binding.journalEditDateTV.text = journalEntry.entryDate.toString()

        binding.journalEditBodyET.setText(journalEntry.entry)

        binding.journalEditSubmitButton.setOnClickListener {
            journalEntry.entry = binding.journalEditBodyET.text.toString()
            //viewModel.journalTasks.firstOrNull { it.itemId == task.itemId }?.let {

            CoroutineScope(Dispatchers.IO).launch {
                /*
                val tasks = viewModel.getTasksById(task.itemId)
                tasks.firstOrNull { it.itemId == task.itemId }?.let {
                    it.entryId = journalEntry.id
                    viewModel.addOrUpdateEntry(journalEntry)
                }
                */
                viewModel.addOrUpdateEntry(journalEntry)
                viewModel.getTaskByEntryId(journalEntry.id)?.let { task ->
                    task.hasEntry = true
                    viewModel.addOrUpdateTask(task)
                }
            }.invokeOnCompletion {
                /*
            viewModel.journalTasks.firstOrNull { it.itemId == task.itemId }?.let {
                it.entryId = journalEntry.id
                viewModel.addOrUpdateEntry(journalEntry)
            }
            */
                dismiss()
            }
        }
        // https://stackoverflow.com/questions/44625365/keyboard-hides-bottomsheetdialogfragment for keyboard not overlapping elements
        // bind stuff
        // call dismiss() to close
        // ondismiss for futher delegate info
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


/*
override fun onStart() {
    super.onStart()
    dialog?.window?.let { window ->
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        window.setLayout(width, height)
        //window.setWindowAnimations(R.style.Slide)
    }
}

override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

    val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    dialog.setOnShowListener {
        val bottomSheet = dialog
            .findViewById<ConstraintLayout>(com.google.android.material.R.id.design_bottom_sheet)

        if (bottomSheet != null) {
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable = true
        }
    }
    return dialog
}

override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = activity?.let {
        val builder = AlertDialog.Builder(it)
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_item_dialog, null))
            // Add action buttons
            .setPositiveButton("Add Item",
                DialogInterface.OnClickListener { dialog, id ->
                    // https://developer.android.com/develop/ui/views/components/dialogs
                    // Do stuff
                })
        builder.create()
    } ?: throw IllegalStateException("Activity cannot be null")

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

    return dialog
}
*/