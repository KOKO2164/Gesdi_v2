package com.koko.gesdi_v2

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.koko.gesdi_v2.entidad.Meta
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class FormMetaActivity : AppCompatActivity() {
    private lateinit var inputNombreMeta: EditText
    private lateinit var inputMontoObjetivo: EditText
    private lateinit var inputMontoActual: EditText
    private lateinit var inputFechaLimite: EditText
    private lateinit var btnRegistrarMeta: Button
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_meta)
        cargarDatos()
        asignarReferencias()
    }

    private fun cargarDatos() {
        userId = intent.getIntExtra("user_id", 1)
    }

    private fun asignarReferencias() {
        inputNombreMeta = findViewById(R.id.inputNombreMeta)
        inputMontoObjetivo = findViewById(R.id.inputMontoObj)
        inputMontoActual = findViewById(R.id.inputMontoAct)
        inputFechaLimite = findViewById(R.id.inputFechaLim)
        btnRegistrarMeta = findViewById(R.id.btnRegistrarMeta)

        inputFechaLimite.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedYear}-${selectedMonth+1}-${selectedDay}"
                inputFechaLimite.setText(selectedDate)
            }, year, month, day).show()
        }

        btnRegistrarMeta.setOnClickListener {
            registrar()
        }
    }

    private fun registrar() {
        val meta = Meta(0, "", 0.0, 0.0, "", userId)
        meta.goal_name = inputNombreMeta.text.toString()
        meta.goal_target_amount = inputMontoObjetivo.text.toString().toDouble()
        meta.goal_actual_amount = inputMontoActual.text.toString().toDouble()
        meta.goal_deadline = inputFechaLimite.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.addMeta(meta)
            runOnUiThread {
                if(rpta.isSuccessful) {
                    Toast.makeText(this@FormMetaActivity, "Meta creada exitosamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}