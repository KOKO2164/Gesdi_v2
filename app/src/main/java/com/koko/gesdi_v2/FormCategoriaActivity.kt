package com.koko.gesdi_v2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.koko.gesdi_v2.entidad.Categoria
import com.koko.gesdi_v2.entidad.Tipo
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormCategoriaActivity : AppCompatActivity() {
    private lateinit var inputNombreCategoria: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnRegistrarCategoria: Button
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_categoria)
        asignarReferencias()
        cargarDatos()
    }

    private fun cargarDatos(){
        userId = intent.getIntExtra("user_id", 1)
    }

    private fun asignarReferencias(){
        inputNombreCategoria = findViewById(R.id.inputNombreCate)
        spinnerTipo = findViewById(R.id.spinnerTipo)
        btnRegistrarCategoria = findViewById(R.id.btnRegistrarCategoria)

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getTipos()
            runOnUiThread {
                if(rpta.isSuccessful){
                    val tipos = rpta.body()!!.listaTipos
                    val adapter = ArrayAdapter(this@FormCategoriaActivity, android.R.layout.simple_spinner_item, tipos)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTipo.adapter = adapter
                }
            }
        }

        btnRegistrarCategoria.setOnClickListener {
            registrar()
        }
    }

    private fun registrar(){
        val categoria = Categoria(0, "",  userId, 0, "")
        val selectedType = spinnerTipo.selectedItem as Tipo

        categoria.category_name = inputNombreCategoria.text.toString()
        categoria.type_id = selectedType.type_id

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.addCategoria(categoria)
            runOnUiThread {
                if(rpta.isSuccessful){
                    mostrarMensaje("Categoría creada exitosamente")
                }
            }
        }
    }

    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this,CategoriesListActivity::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}