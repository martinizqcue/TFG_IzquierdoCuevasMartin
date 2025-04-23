package com.example.sevillanasmaneras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sevillanasmaneras.RegistroValidator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var nombreEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var btnSalir: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        nombreEditText = findViewById(R.id.nombreEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        btnSalir = findViewById(R.id.btnSalir)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        registerButton.setOnClickListener {
            val nombre = nombreEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val error = RegistroValidator.validarDatos(nombre, email, password)

            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods
                        if (!signInMethods.isNullOrEmpty()) {
                            Toast.makeText(this, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
                        } else {
                            registrarUsuario(email, password, nombre)
                        }
                    } else {
                        Toast.makeText(this, "Error al verificar el correo: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnSalir.setOnClickListener {
            finish()
        }

        btnSiguiente.setOnClickListener {
            startActivity(Intent(this, SesionActivity::class.java))
        }
    }

    private fun registrarUsuario(email: String, password: String, nombre: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userInfo = hashMapOf(
                        "nombre" to nombre,
                        "email" to email,
                        "uid" to user?.uid
                    )

                    db.collection("usuarios")
                        .document(user?.uid ?: "")
                        .set(userInfo)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, PrincipalActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
