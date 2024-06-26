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
    private lateinit var inputMeta: EditText
    private lateinit var btnCrearMeta: Button
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
        inputMeta = findViewById(R.id.inputMeta)
        btnCrearMeta = findViewById(R.id.btnCrearMeta)

        btnDashboard.setOnClickListener {
            finish()
        }

        btnCrearMeta.setOnClickListener {
            val intent = Intent(this, FormMetaActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        inputMeta.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val q = v.text.toString()
                Log.d("GoalsListActivity", "q: $q")
                CoroutineScope(Dispatchers.IO).launch {
                    val rpta = RetrofitClient.webService.buscarMetas(userId, q)
                    Log.d("GoalsListActivity", "rpta: $rpta")
                    runOnUiThread {
                        if (rpta.isSuccessful) {
                            val metas = rpta.body()!!.listaMetas
                            Log.d("GoalsListActivity", "metas: $metas")
                            adaptador.setListaMetas(metas ?: ArrayList())
                        } else {
                            mostrarMensaje(rpta.message())
                            adaptador.setListaMetas(ArrayList())
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