package com.koko.gesdi_v2

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class YearPickerDialog : DialogFragment() {

    private var listener: ((year: Int) -> Unit)? = null

    fun setListener(listener: ((year: Int) -> Unit)) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_year_picker, null)

        val yearPicker = view.findViewById<NumberPicker>(R.id.year_picker)
        yearPicker.minValue = 2000
        yearPicker.maxValue = 2099
        yearPicker.value = 2024

        val button = view.findViewById<Button>(R.id.button_year_ok)
        button.setOnClickListener {
            listener?.invoke(yearPicker.value)
            dialog?.dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}