package com.koko.gesdi_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class DashboardActivity : AppCompatActivity() {
    private lateinit var txtNombre: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var btnLogout: ImageButton
    private lateinit var btnCategories: ConstraintLayout
    private lateinit var btnTransactions: ConstraintLayout
    private lateinit var btnGoals: ConstraintLayout
    private var userId: Int = 0
    private var userName: String = ""
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        asignarReferencias()

        userId = intent.getIntExtra("user_id", 1)
        userName = intent.getStringExtra("user_name") ?: ""
        userEmail = intent.getStringExtra("user_email") ?: ""

        if (userName != null && userEmail != null) {
            txtNombre.text = userName
            txtCorreo.text = userEmail
        } else {
            Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun asignarReferencias() {
        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        btnLogout = findViewById(R.id.btnLogout)
        btnCategories = findViewById(R.id.btnCategories)
        btnTransactions = findViewById(R.id.btnTransactions)
        btnGoals = findViewById(R.id.btnGoals)

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCategories.setOnClickListener {
            val intent = Intent(this, CategoriesListActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("user_name", userName)
            intent.putExtra("user_email", userEmail)
            startActivity(intent)
        }

        btnTransactions.setOnClickListener {
            val intent = Intent(this, TransactionsListActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("user_name", userName)
            intent.putExtra("user_email", userEmail)
            startActivity(intent)
        }

        btnGoals.setOnClickListener {
            val intent = Intent(this, GoalsListActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("user_name", userName)
            intent.putExtra("user_email", userEmail)
            startActivity(intent)
        }
    }

    /*private fun cargarDatos() {
        val userId = intent.getIntExtra("user_id", 1)
        val call = RetrofitClient.webService.getUsuario(userId)
        call.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    val usuario = response.body()
                    txtNombre.text = usuario?.user_name
                    txtCorreo.text = usuario?.user_email
                }
            }
            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Error al cargar los datos", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }*/
}