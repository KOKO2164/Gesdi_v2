package com.koko.gesdi_v2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesListActivity : AppCompatActivity() {
    private lateinit var rvCategories: RecyclerView
    private lateinit var btnDashboard: ConstraintLayout
    private lateinit var inputCategoria: EditText
    private lateinit var btnCrearCategoria: Button
    private var userId: Int = 0

    private var adaptador: AdaptadorCategorias = AdaptadorCategorias()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_list)
        asignarReferencias()
        cargarDatos()
    }

    private fun cargarDatos() {
        userId = intent.getIntExtra("user_id", 1)
        Log.d("CategoriesListActivity", "userId: $userId")

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getCategorias(userId)
            runOnUiThread {
                if(rpta.isSuccessful) {
                    val categorias = rpta.body()!!.listaCategorias
                    Log.d("CategoriesListActivity", "categorias: $categorias")
                    adaptador.setListaCategorias(categorias ?: ArrayList())
                } else {
                    mostrarMensaje(rpta.message())
                    adaptador.setListaCategorias(ArrayList())
                }
            }
        }
    }

    private fun asignarReferencias() {
        rvCategories = findViewById(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this)
        rvCategories.adapter = adaptador
        btnDashboard = findViewById(R.id.btnDashboard)
        inputCategoria = findViewById(R.id.inputCategoria)
        btnCrearCategoria = findViewById(R.id.btnCrearCate)

        btnDashboard.setOnClickListener {
            finish()
        }

        btnCrearCategoria.setOnClickListener {
            val intent = Intent(this, FormCategoriaActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        inputCategoria.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val q = v.text.toString()
                Log.d("CategoriesListActivity", "q: $q")
                CoroutineScope(Dispatchers.IO).launch {
                    val rpta = RetrofitClient.webService.buscarCategorias(q)
                    Log.d("CategoriesListActivity", "rpta: $rpta")
                    runOnUiThread {
                        if(rpta.isSuccessful) {
                            val categorias = rpta.body()!!.listaCategorias
                            Log.d("CategoriesListActivity", "categorias: $categorias")
                            adaptador.setListaCategorias(categorias ?: ArrayList())
                        } else {
                            mostrarMensaje(rpta.message())
                            adaptador.setListaCategorias(ArrayList())
                        }
                    }
                }
                true
            } else {
                false
            }
        }

        adaptador.contexto(this)
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