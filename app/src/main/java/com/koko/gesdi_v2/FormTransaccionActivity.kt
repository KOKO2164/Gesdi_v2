package com.koko.gesdi_v2

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.koko.gesdi_v2.entidad.CategoryTransaction
import com.koko.gesdi_v2.entidad.Transaccion
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class FormTransaccionActivity : AppCompatActivity() {
    private lateinit var inputMonto: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var inputDescripcion: EditText
    private lateinit var inputFecha: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var btnRegistrarTransaccion: Button
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_transaccion)
        asignarReferencias()
        cargarDatos()
    }

    private fun cargarDatos(){
        userId = intent.getIntExtra("user_id", 1)
    }

    private fun asignarReferencias(){
        inputMonto = findViewById(R.id.inputMontoTran)
        spinnerTipo = findViewById(R.id.spinnerTipoTran)
        inputDescripcion = findViewById(R.id.inputDescTran)
        inputFecha = findViewById(R.id.inputFechaTran)
        spinnerCategoria = findViewById(R.id.spinnerCategoriaTran)
        btnRegistrarTransaccion = findViewById(R.id.btnRegistrarTransaccion)

        inputFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedYear}-${selectedMonth+1}-${selectedDay}"
                inputFecha.setText(selectedDate)
            }, year, month, day).show()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getTiposCategoria()
            runOnUiThread {
                if(rpta.isSuccessful){
                    val tipos = rpta.body()!!.listaTiposCategoria
                    val adapter = ArrayAdapter(this@FormTransaccionActivity, android.R.layout.simple_spinner_item, tipos)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTipo.adapter = adapter
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getCategoriasTransaccion(userId)
            runOnUiThread {
                if(rpta.isSuccessful){
                    val categorias = rpta.body()!!.listaCategoriasTransaccion
                    val adapter = ArrayAdapter(this@FormTransaccionActivity, android.R.layout.simple_spinner_item, categorias)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategoria.adapter = adapter
                }
            }
        }

        btnRegistrarTransaccion.setOnClickListener {
            registrar()
        }
    }

    private fun registrar(){
        val transaccion = Transaccion(0, 0.0, "", "", "", userId, 0)
        val selectedCategory = spinnerCategoria.selectedItem as CategoryTransaction
        transaccion.transaction_amount = inputMonto.text.toString().toDouble()
        transaccion.transaction_type = spinnerTipo.selectedItem.toString()
        transaccion.transaction_description = inputDescripcion.text.toString()
        transaccion.transaction_date = inputFecha.text.toString()
        transaccion.category_id = selectedCategory.category_id

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.addTransaccion(transaccion)
            runOnUiThread {
                if (rpta.isSuccessful) {
                    mostrarMensaje("Transacción creada exitosamente")

                }
            }
        }
    }

    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this,TransactionsListActivity::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}