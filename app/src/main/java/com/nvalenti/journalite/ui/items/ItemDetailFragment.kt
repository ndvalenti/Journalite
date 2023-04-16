package com.nvalenti.journalite.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nvalenti.journalite.MainViewModel
import com.nvalenti.journalite.R
import com.nvalenti.journalite.controller.JournalItem
import com.nvalenti.journalite.databinding.FragmentItemDetailBinding
import com.nvalenti.journalite.databinding.FragmentSetStringDialogBinding
import com.nvalenti.journalite.ui.dialog.ConfirmDialog
import com.nvalenti.journalite.ui.dialog.DataEntryDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.*

class ItemDetailFragment : Fragment() {
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var id: UUID
    private val viewModel by activityViewModels<MainViewModel>()
    private val args: ItemDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainViewModel.navBarVisible.value = false

        id = args.uuid
        val itemObserver = Observer<JournalItem> { item: JournalItem? ->
            updateUI(item ?: JournalItem(UUID.randomUUID(), null))
        }
        viewModel.journalItem(id).observe(viewLifecycleOwner, itemObserver)
    }

    private fun updateUI(item: JournalItem) {
        binding.itemDetailArchiveCB.isChecked = item.isArchived
        binding.itemDetailNotifyCB.isChecked = item.isNotify
        item.timeDue?.let {
            binding.itemDetailTP.minute = it.minute
            binding.itemDetailTP.hour = it.hour
        }
        binding.itemDetailDayPicker.setDaysTo(item.days)
        binding.titleContentTV.text = item.title ?: ItemsFragment.deletionString
        binding.titleLayout.setOnClickListener {
            showTitleDialog(item.title)
        }

        binding.itemDetailDeleteButton.setOnClickListener {
            showConfirmDialog(item)
        }

        // TODO: Change button text on context, in layout file?
        binding.itemDetailSaveButton.setOnClickListener {
            val newJournalItem = JournalItem(id, getString(R.string.new_item_title))

            newJournalItem.title = binding.titleContentTV.text.toString()
            newJournalItem.isArchived = binding.itemDetailArchiveCB.isChecked
            newJournalItem.isNotify = binding.itemDetailNotifyCB.isChecked
            newJournalItem.timeDue = LocalTime.of(binding.itemDetailTP.hour, binding.itemDetailTP.minute)
            newJournalItem.days = binding.itemDetailDayPicker.days

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addOrUpdateItem(newJournalItem)
            }.invokeOnCompletion {
                requireActivity().runOnUiThread {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun showConfirmDialog(item: JournalItem) {
        val confirmText = getString(R.string.delete) + " " + item.title + "?"
        val bodyText = getString(R.string.confirm_item_delete)
        val dialog = ConfirmDialog(confirmText, bodyText) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteItem(item)
                viewModel.deleteTasksByItemId(item.id)
            }.invokeOnCompletion {
                requireActivity().runOnUiThread {
                    findNavController().popBackStack()
                }
            }
        }
        dialog.show(childFragmentManager, "deleteConfirm")
    }

    private fun showTitleDialog(title: String?) {
        val dialog = DataEntryDialog(title) {
            binding.titleContentTV.text = it
        }
        dialog.show(childFragmentManager, "dataEntry")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}