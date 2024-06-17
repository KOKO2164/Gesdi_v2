package com.koko.gesdi_v2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.koko.gesdi_v2.request.LoginRequest
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    private lateinit var inputCorreo: EditText
    private lateinit var inputContrasena: EditText
    private lateinit var btnIngresar: Button
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        asignarReferencias()
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedPassword = digest.fold("", { str, it -> str + "%02x".format(it) })

        return hashedPassword
    }

    private fun asignarReferencias() {
        inputCorreo = findViewById(R.id.inputCorreoLogin)
        inputContrasena = findViewById(R.id.inputContrasenaLogin)
        btnIngresar = findViewById(R.id.btnIniciarLogin)
        btnRegistrar = findViewById(R.id.btnRegistrarLogin)

        btnIngresar.setOnClickListener {
            val email = inputCorreo.text.toString()
            val password = hashPassword(inputContrasena.text.toString())

            CoroutineScope(Dispatchers.IO).launch {
                val rpta = RetrofitClient.webService.login(LoginRequest(email, password))
                runOnUiThread {
                    if (rpta.isSuccessful) {
                        val usuario = rpta.body()
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("user_id", usuario?.user_id)
                        intent.putExtra("user_name", usuario?.user_name)
                        intent.putExtra("user_email", usuario?.user_email)
                        startActivity(intent)
                    } else {
                        mostrarMensaje("Usuario o contraseña incorrectos")
                    }
                }
            }
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}