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

class TransactionsListActivity : AppCompatActivity() {
    private lateinit var rvTransactions: RecyclerView
    private lateinit var btnDashboard: ConstraintLayout
    private lateinit var inputTransaccion: EditText
    private lateinit var btnCrearTransaccion: Button
    private var userId: Int = 0
    private var categoryId: Int = 0

    private var adaptador: AdaptadorTransacciones = AdaptadorTransacciones()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_list)
        asignarReferencias()
        cargarDatos()
    }

    private fun cargarDatos() {
        userId = intent.getIntExtra("user_id", 1)
        println("userId: $userId")
        categoryId = intent.getIntExtra("category_id", 1)
        println("categoryId: $categoryId")

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("TransactionsListActivity", "Entro a CoroutineScope")
            val rpta = RetrofitClient.webService.getTransacciones(userId)
            Log.d("TransactionsListActivity", "rpta: $rpta")
            runOnUiThread {
                if (rpta.isSuccessful) {
                    val transacciones = rpta.body()!!.listaTransacciones
                    Log.d("TransactionsListActivity", "transacciones: $transacciones")
                    adaptador.setListaTransacciones(transacciones ?: ArrayList())
                } else {
                    mostrarMensaje(rpta.message())
                    adaptador.setListaTransacciones(ArrayList())
                }
            }
        }
    }

    private fun asignarReferencias() {
        rvTransactions = findViewById(R.id.rvTransactions)
        rvTransactions.layoutManager = LinearLayoutManager(this)
        rvTransactions.adapter = adaptador
        btnDashboard = findViewById(R.id.btnDashboard)
        btnCrearTransaccion = findViewById(R.id.btnCrearTransaccion)
        inputTransaccion = findViewById(R.id.inputTransaccion)

        btnDashboard.setOnClickListener {
            finish()
        }

        btnCrearTransaccion.setOnClickListener {
            val intent = Intent(this, FormTransaccionActivity::class.java)
            intent.putExtra("user_id", userId)
            println("userId: $userId")
            intent.putExtra("category_id", categoryId)
            println("categoryId: $categoryId")
            startActivity(intent)
        }

        inputTransaccion.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val descripcion = v.text.toString()
                Log.d("TransactionsListActivity", "descripcion: $descripcion")
                CoroutineScope(Dispatchers.IO).launch {
                    val rpta = RetrofitClient.webService.buscarTransacciones(userId, descripcion)
                    Log.d("TransactionsListActivity", "rpta: $rpta")
                    runOnUiThread {
                        if (rpta.isSuccessful) {
                            val transacciones = rpta.body()!!.listaTransacciones
                            Log.d("TransactionsListActivity", "transacciones: $transacciones")
                            adaptador.setListaTransacciones(transacciones ?: ArrayList())
                        } else {
                            mostrarMensaje(rpta.message())
                            adaptador.setListaTransacciones(ArrayList())
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