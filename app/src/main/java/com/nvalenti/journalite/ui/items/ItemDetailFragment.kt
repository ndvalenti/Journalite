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
import com.nvalenti.journalite.controller.JournalItem
import com.nvalenti.journalite.databinding.FragmentItemDetailBinding
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
            updateUI(item ?: JournalItem(UUID.randomUUID()))
        }
        viewModel.journalItem(id).observe(viewLifecycleOwner, itemObserver)
    }

    private fun updateUI(item: JournalItem) {
        binding.itemDetailArchivedTB.isChecked = item.isArchived
        binding.itemDetailNotificationSwitch.isChecked = item.isNotify
        item.timeDue?.let {
            binding.itemDetailTP.minute = it.minute
            binding.itemDetailTP.hour = it.hour
        }
        binding.itemDetailDayPicker.setDaysTo(item.days)
        binding.itemDetailTitleTV.setText(item.title)

        // TODO: Change button text on context, in layout file?
        binding.itemDetailAddButton.setOnClickListener {
            val newJournalItem = JournalItem(id)

            newJournalItem.title = binding.itemDetailTitleTV.text.toString()
            newJournalItem.isArchived = binding.itemDetailArchivedTB.isChecked
            newJournalItem.isNotify = binding.itemDetailNotificationSwitch.isChecked
            newJournalItem.timeDue = LocalTime.of(binding.itemDetailTP.hour, binding.itemDetailTP.hour)
            newJournalItem.days = binding.itemDetailDayPicker.days

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addOrUpdateItem(newJournalItem)
            }

            // TODO: All of this causes all sorts of unwanted behavior, need to update previous view properly
            findNavController().popBackStack()
            //val action = ItemDetailFragmentDirections.actionNavigationItemDetailToMainNavigation()
            //findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}