package com.koko.gesdi_v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.koko.gesdi_v2.entidad.Categoria
import com.koko.gesdi_v2.entidad.Presupuesto
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormPresupuestoActivity : AppCompatActivity() {
    private lateinit var inputNombre: EditText
    private lateinit var inputMonto: EditText
    private lateinit var inputMes: EditText
    private lateinit var inputAnio: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var btnRegistrarPresupuesto: Button
    private var userId: Int = 0
    private var categoryId: Int = 0
    private var monthNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_presupuesto)
        cargarDatos()
        asignarReferencias()
    }

    private fun cargarDatos() {
        userId = intent.getIntExtra("user_id", 1)
        println("userId: $userId")
        categoryId = intent.getIntExtra("category_id", 1)
        println("categoryId: $categoryId")
    }

    private fun asignarReferencias() {
        inputNombre = findViewById(R.id.inputNombrePres)
        inputMonto = findViewById(R.id.inputMontoPres)
        inputMes = findViewById(R.id.inputMesPres)
        inputAnio = findViewById(R.id.inputAnioPres)
        spinnerCategoria = findViewById(R.id.spinnerCategoriaPres)
        btnRegistrarPresupuesto = findViewById(R.id.btnRegistrarPresupuesto)

        inputMes.setOnClickListener {
            val dialog = MonthPickerDialog()
            dialog.setListener { month, monthName ->
                inputMes.setText(monthName)
                monthNumber = month
            }
            dialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

        inputAnio.setOnClickListener {
            val dialog = YearPickerDialog()
            dialog.setListener { year ->
                inputAnio.setText("$year")
            }
            dialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getCategorias(userId)
            runOnUiThread {
                if (rpta.isSuccessful) {
                    val categorias = rpta.body()!!.listaCategorias
                    val categoriaVacia = Categoria(0, "Seleccione una categoría", userId, 0, "")
                    categorias.add(0, categoriaVacia)

                    val adapter = ArrayAdapter(this@FormPresupuestoActivity, android.R.layout.simple_spinner_item, categorias)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategoria.adapter = adapter
                }
            }
        }

        btnRegistrarPresupuesto.setOnClickListener {
            registrar()
        }
    }

    private fun registrar() {
        val presupuesto = Presupuesto(0, "", 0.0, null, 0, 0, null, "", userId)
        val selectedCategory = spinnerCategoria.selectedItem as Categoria
        if (selectedCategory.category_id == 0) {
            println("No se seleccionó categoría")
            presupuesto.category_id = null
            presupuesto.category_name = ""
        } else {
            presupuesto.category_id = selectedCategory.category_id
            presupuesto.category_name = selectedCategory.category_name
        }
        presupuesto.budget_name = inputNombre.text.toString()
        presupuesto.budget_amount = inputMonto.text.toString().toDouble()
        presupuesto.budget_year = inputAnio.text.toString().toInt()
        if (monthNumber == 0) {
            presupuesto.budget_month = null
            presupuesto.budget_is_total = 1
        } else {
            presupuesto.budget_month = monthNumber
            presupuesto.budget_is_total = 0
        }
        println(presupuesto)
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.addPresupuesto(presupuesto)
            runOnUiThread {
                if (rpta.isSuccessful) {
                    Toast.makeText(this@FormPresupuestoActivity, "Presupuesto registrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FormPresupuestoActivity, "Error al registrar presupuesto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}