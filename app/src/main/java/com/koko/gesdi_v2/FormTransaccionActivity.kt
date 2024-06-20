package com.koko.gesdi_v2

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.koko.gesdi_v2.entidad.Categoria
import com.koko.gesdi_v2.entidad.Tipo
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
    private var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_transaccion)
        cargarDatos()
        asignarReferencias()
    }

    private fun cargarDatos(){
        userId = intent.getIntExtra("user_id", 1)
        println("userId: $userId")
        categoryId = intent.getIntExtra("category_id", 1)
        println("categoryId: $categoryId")
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
            val rpta = RetrofitClient.webService.getTipos()
            runOnUiThread {
                if(rpta.isSuccessful){
                    val tipos = rpta.body()!!.listaTipos
                    val adapter = ArrayAdapter(this@FormTransaccionActivity, android.R.layout.simple_spinner_item, tipos)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTipo.adapter = adapter
                }
            }
        }

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val tipoSeleccionado = parent?.getItemAtPosition(position) as Tipo

                CoroutineScope(Dispatchers.IO).launch {
                    val rpta = RetrofitClient.webService.getCategorias(userId)
                    runOnUiThread {
                        if (rpta.isSuccessful) {
                            val categorias = rpta.body()!!.listaCategorias
                            val categoriasLists =
                                categorias.filter { it.type_id == tipoSeleccionado.type_id }
                            val adapter = ArrayAdapter(
                                this@FormTransaccionActivity,
                                android.R.layout.simple_spinner_item,
                                categoriasLists
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerCategoria.adapter = adapter
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Code
            }
        }

        btnRegistrarTransaccion.setOnClickListener {
            registrar()
        }
    }

    private fun registrar(){
        val transaccion = Transaccion(0, 0.0, "", "", userId, 0, 0)
        val selectedType = spinnerTipo.selectedItem as Tipo
        val selectedCategory = spinnerCategoria.selectedItem as Categoria
        transaccion.transaction_amount = inputMonto.text.toString().toDouble()
        transaccion.transaction_description = inputDescripcion.text.toString()
        transaccion.transaction_date = inputFecha.text.toString()
        transaccion.category_id = selectedCategory.category_id
        transaccion.type_id = selectedType.type_id

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