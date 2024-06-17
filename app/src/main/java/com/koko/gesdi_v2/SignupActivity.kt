package com.koko.gesdi_v2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.koko.gesdi_v2.entidad.Usuario
import com.koko.gesdi_v2.request.SignupRequest
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class SignupActivity : AppCompatActivity() {
    private lateinit var inputNombre: EditText
    private lateinit var inputCorreo: EditText
    private lateinit var inputContrasena: EditText
    private lateinit var inputConfContrasena: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
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
        inputNombre = findViewById(R.id.inputNombre)
        inputCorreo = findViewById(R.id.inputCorreoSignup)
        inputContrasena = findViewById(R.id.inputContrasenaSignup)
        inputConfContrasena = findViewById(R.id.inputConfContrasena)
        btnRegistrar = findViewById(R.id.btnRegistrarSignup)
        btnIniciarSesion = findViewById(R.id.btnIniciarSignup)

        btnRegistrar.setOnClickListener {
            val name = inputNombre.text.toString()
            val email = inputCorreo.text.toString()
            val password = hashPassword(inputContrasena.text.toString())
            val password_confirmation = hashPassword(inputConfContrasena.text.toString())

            if (password != password_confirmation) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val rpta = RetrofitClient.webService.signup(SignupRequest(name, email, password))
                runOnUiThread {
                    if (rpta.isSuccessful) {
                        val usuario = rpta.body()
                        val intent = Intent(this@SignupActivity, DashboardActivity::class.java)
                        intent.putExtra("user_id", usuario?.user_id)
                        intent.putExtra("user_name", usuario?.user_name)
                        intent.putExtra("user_email", usuario?.user_email)
                        startActivity(intent)
                    } else {
                        mostrarMensaje("Error al registrar el usuario")
                    }
                }
            }
        }

        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
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