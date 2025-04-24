package com.example.sevillanasmaneras

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var txtNombre: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtFecha: TextView
    private lateinit var txtListaFavoritos: TextView
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnAtras: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Referencias UI
        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtFecha = findViewById(R.id.txtFecha)
        txtListaFavoritos = findViewById(R.id.txtListaFavoritos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnAtras = findViewById(R.id.btnAtras)

        // Obtener usuario actual
        val user = auth.currentUser

        if (user != null) {
            val uid = user.uid
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: "Sin nombre"
                        val correo = document.getString("email") ?: "Sin correo"
                        val fecha = document.getString("fechaRegistro") ?: "Sin fecha"

                        txtNombre.text = "Nombre: $nombre"
                        txtCorreo.text = "Correo: $correo"
                        txtFecha.text = "Fecha Registro: $fecha"

                        // Si tienes favoritos guardados como lista en Firestore:
                        val favoritos = document.get("favoritos") as? List<*>
                        if (favoritos != null && favoritos.isNotEmpty()) {
                            txtListaFavoritos.text = favoritos.joinToString(separator = "\n") { "• $it" }
                        } else {
                            txtListaFavoritos.text = "No hay favoritos aún."
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }

        // Botón cerrar sesión
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SesionActivity::class.java))
            finish()
        }

        // Botón atrás
        btnAtras.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }
    }
}
