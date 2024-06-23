package com.koko.gesdi_v2

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MonthPickerDialog : DialogFragment() {

    private var listener: ((month: Int, monthName: String) -> Unit)? = null

    fun setListener(listener: ((month: Int, monthName: String) -> Unit)) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_month_picker, null)

        val months = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

        val monthPicker = view.findViewById<NumberPicker>(R.id.month_picker)
        monthPicker.minValue = 0
        monthPicker.maxValue = months.size - 1
        monthPicker.displayedValues = months
        monthPicker.value = 5

        val button = view.findViewById<Button>(R.id.button_month_ok)
        button.setOnClickListener {
            listener?.invoke(monthPicker.value + 1, months[monthPicker.value])
            dialog?.dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}