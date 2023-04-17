package com.nvalenti.journalite.ui.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.nvalenti.journalite.R
import com.nvalenti.journalite.databinding.FragmentDeleteItemConfirmationDialogBinding

class ConfirmDialog(private val confirmText: String, private val bodyText: String, val dialogSuccess: () -> Unit) : DialogFragment() {
    private var _binding: FragmentDeleteItemConfirmationDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentDeleteItemConfirmationDialogBinding.inflate(LayoutInflater.from(context))
        binding.confirmDialogTitleTV.text = confirmText
        binding.confirmDialogBodyTV.text = bodyText

        return AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme)
            .setView(binding.root)
            .setPositiveButton(R.string.delete) { _, _ ->
                dialogSuccess()
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