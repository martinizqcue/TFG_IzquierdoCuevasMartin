package com.example.sevillanasmaneras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.SignInMethodQueryResult

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Declaración de los campos y botones
    private lateinit var nombreEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var btnSalir: Button
    private lateinit var btnSiguiente: Button // Nuevo botón para la navegación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Obtener las referencias a los elementos del layout
        nombreEditText = findViewById(R.id.nombreEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        btnSalir = findViewById(R.id.btnSalir)
        btnSiguiente = findViewById(R.id.btnSiguiente) // Referencia al nuevo botón

        // Configuración del botón de registro
        registerButton.setOnClickListener {
            val nombre = nombreEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Verificación de campos vacíos
            if (nombre.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Verificar si el correo ya está registrado
                auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            if (!signInMethods.isNullOrEmpty()) {
                                // El correo ya está registrado
                                Toast.makeText(this, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
                            } else {
                                // El correo no está registrado, continuar con el registro
                                registrarUsuario(email, password, nombre)
                            }
                        } else {
                            // Error al verificar el correo
                            Toast.makeText(this, "Error al verificar el correo: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de salir
        btnSalir.setOnClickListener {
            finish() // Cerrar la actividad
        }

        // Configurar el botón de siguiente
        btnSiguiente.setOnClickListener {
            // Iniciar la actividad de SesionActivity
            startActivity(Intent(this, SesionActivity::class.java))
        }
    }

    private fun registrarUsuario(email: String, password: String, nombre: String) {
        // Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Usuario registrado exitosamente
                    val user = auth.currentUser
                    val userInfo = hashMapOf(
                        "nombre" to nombre,
                        "email" to email,
                        "uid" to user?.uid
                    )

                    // Guardar datos adicionales en Firestore
                    db.collection("usuarios")
                        .document(user?.uid ?: "")
                        .set(userInfo)
                        .addOnSuccessListener {
                            // Redirigir al usuario a la actividad principal
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, PrincipalActivity::class.java))
                            finish() // Cerrar la actividad actual
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Error en el registro
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
