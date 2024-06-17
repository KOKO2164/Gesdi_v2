package com.koko.gesdi_v2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koko.gesdi_v2.servicio.GoalsResponse
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalsListActivity : AppCompatActivity() {
    private lateinit var rvGoals: RecyclerView
    private lateinit var btnDashboard: ConstraintLayout
    private var userId: Int = 0

    private var adaptador: AdaptadorMetas = AdaptadorMetas()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals_list)
        asignarReferencias()
        cargarDatos()
    }

    private fun cargarDatos() {
        userId = intent.getIntExtra("user_id", 1)
        Log.d("GoalsListActivity", "userId: $userId")

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getMetas(userId)
            runOnUiThread {
                if(rpta.isSuccessful) {
                    val metas = rpta.body()!!.listaMetas
                    Log.d("GoalsListActivity", "metas: $metas")
                    adaptador.setListaMetas(metas ?: ArrayList())
                } else {
                    mostrarMensaje(rpta.message())
                    adaptador.setListaMetas(ArrayList())
                }
            }
        }
    }

    private fun asignarReferencias() {
        rvGoals = findViewById(R.id.rvGoals)
        rvGoals.layoutManager = LinearLayoutManager(this)
        rvGoals.adapter = adaptador
        btnDashboard = findViewById(R.id.btnDashboard)

        btnDashboard.setOnClickListener {
            finish()
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