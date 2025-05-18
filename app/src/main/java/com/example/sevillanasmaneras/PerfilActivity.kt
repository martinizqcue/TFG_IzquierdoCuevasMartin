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
    private lateinit var txtListaFavoritos: TextView
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnAtras: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtListaFavoritos = findViewById(R.id.txtListaFavoritos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnAtras = findViewById(R.id.btnAtras)

        val user = auth.currentUser

        if (user != null) {
            val uid = user.uid

            // 🔹 Cargar datos del usuario
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    val nombre = document.getString("nombre") ?: "Sin nombre"
                    val correo = document.getString("email") ?: "Sin correo"

                    txtNombre.text = "Nombre: $nombre"
                    txtCorreo.text = "Correo: $correo"
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                }

            // 🔹 Cargar favoritos desde subcolección
            db.collection("usuarios").document(uid).collection("favoritos")
                .get()
                .addOnSuccessListener { snapshot ->
                    val favoritos = snapshot.documents.mapNotNull { it.getString("nombre") }
                    txtListaFavoritos.text = if (favoritos.isNotEmpty()) {
                        favoritos.joinToString("\n") { "• $it" }
                    } else {
                        "No hay favoritos aún."
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar favoritos", Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Cerrar sesión
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SesionActivity::class.java))
            finish()
        }

        // 🔹 Volver al menú principal
        btnAtras.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }
    }
}
