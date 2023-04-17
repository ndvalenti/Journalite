package com.nvalenti.journalite.ui.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.nvalenti.journalite.R
import com.nvalenti.journalite.databinding.FragmentSetStringDialogBinding

class DataEntryDialog(private var itemLabel: String?, val dialogSuccess: (String?) -> Unit) : DialogFragment() {
    private var _binding: FragmentSetStringDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentSetStringDialogBinding.inflate(LayoutInflater.from(context))
        binding.setStringContentET.setText(itemLabel ?: "")
        binding.setStringContentET.setSelection(binding.setStringContentET.length())

        return AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme)
            .setView(binding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                //listener.onDialogPositiveClick(inputView.text.toString())
                dialogSuccess(binding.setStringContentET.text.toString())
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                dialog?.cancel()
            }
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}