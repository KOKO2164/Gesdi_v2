package com.koko.gesdi_v2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koko.gesdi_v2.entidad.Presupuesto
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetsListActivity : AppCompatActivity() {
    private lateinit var rvBudgets: RecyclerView
    private lateinit var btnDashboard: ConstraintLayout
    private lateinit var inputPresupuesto: EditText
    private lateinit var btnCrearPresupuesto: Button
    private var userId: Int = 0
    private var categoryId: Int = 0

    private var adaptador: AdaptadorPresupuestos = AdaptadorPresupuestos()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budgets_list)
        asignarReferencias()
        cargarDatos()
    }

    private fun cargarDatos() {
        userId = intent.getIntExtra("user_id", 1)
        categoryId = intent.getIntExtra("category_id", 1)

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getPresupuestos(userId)
            runOnUiThread {
                if (rpta.isSuccessful) {
                    val presupuestos = rpta.body()!!.listaPresupuestos
                    adaptador.setListaPresupuestos(presupuestos ?: ArrayList())
                } else {
                    mostrarMensaje(rpta.message())
                    adaptador.setListaPresupuestos(ArrayList())
                }
            }
        }
    }

    private fun asignarReferencias() {
        rvBudgets = findViewById(R.id.rvBudgets)
        rvBudgets.layoutManager = LinearLayoutManager(this)
        rvBudgets.adapter = adaptador
        btnDashboard = findViewById(R.id.btnBudgetToDashboard)
        btnCrearPresupuesto = findViewById(R.id.btnCrearPresupuesto)
        inputPresupuesto = findViewById(R.id.inputPresupuesto)

        btnDashboard.setOnClickListener {
            finish()
        }

        btnCrearPresupuesto.setOnClickListener {
            val intent = Intent(this, FormPresupuestoActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("category_id", categoryId)
            startActivity(intent)
        }

        inputPresupuesto.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val q = v.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    val rpta = RetrofitClient.webService.buscarPresupuestos(userId, q)
                    runOnUiThread {
                        if (rpta.isSuccessful) {
                            val presupuestos = rpta.body()!!.listaPresupuestos
                            adaptador.setListaPresupuestos(presupuestos ?: ArrayList())
                        } else {
                            mostrarMensaje(rpta.body()!!.mensaje)
                            adaptador.setListaPresupuestos(ArrayList())
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