package com.example.sevillanasmaneras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SesionActivity : AppCompatActivity() {

    // Inicializar el Auth de Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sesion)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Referencias a los elementos del layout
        val correoEditText = findViewById<EditText>(R.id.emailInput)
        val contraseñaEditText = findViewById<EditText>(R.id.passwordInput)
        val btnIniciarSesion = findViewById<Button>(R.id.loginButton)

        // Lógica para el botón "Iniciar Sesión"
        btnIniciarSesion.setOnClickListener {
            val correo = correoEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()

            // Verificar que el correo y la contraseña no estén vacíos
            if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
                auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Si la autenticación es exitosa, mostrar mensaje y navegar
                            Toast.makeText(this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show()
                            // startActivity(Intent(this, MainActivity::class.java)) // Aquí navegas a la actividad principal
                            finish() // Finaliza la actividad actual
                        } else {
                            // Si hay un error en la autenticación, mostrar mensaje
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                // Si los campos están vacíos, mostrar mensaje
                Toast.makeText(this, "Por favor ingrese el correo y la contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Lógica para el botón "Registrarse"
        val btnRegistrarse = findViewById<Button>(R.id.registerButton)
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}

