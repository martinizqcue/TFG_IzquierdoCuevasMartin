package com.example.sevillanasmaneras

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrincipalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    // Descomenta esto para agregar las imágenes en res/drawable
    /*private val imagenes = listOf(
        R.drawable.imagen_lugar1,
        R.drawable.imagen_lugar2,
        R.drawable.imagen_lugar3
    )*/

    private var indexImagen = 0
    private val totalLugares = 3 // Número de lugares ficticios por ahora

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // MOSTRAR NOMBRE DEL USUARIO EN TOOLBAR Y ABRIR PERFIL
        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("usuarios").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("nombre")) {
                        val nombre = document.getString("nombre") ?: "Usuario"
                        userNameTextView.text = nombre
                    }
                }
        }

        userNameTextView.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        // Configurar el botón del menú (hamburguesa)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener {
            val title = it.title?.toString() ?: ""
            toolbarTitle.text = title

            when (it.itemId) {
                R.id.nav_lugares -> showToast("Lugares")
                R.id.nav_tradiciones -> showToast("Tradiciones")
                R.id.nav_cultura -> showToast("Cultura")
                R.id.nav_monumentos -> showToast("Monumentos")
            }

            drawerLayout.closeDrawers()
            true
        }


        val imageView = findViewById<ImageView>(R.id.imageCarousel)
        val nombreLugar = findViewById<TextView>(R.id.nombreLugarText)
        val descripcionLugar = findViewById<TextView>(R.id.descripcionLugarText)

        val prevBtn = findViewById<Button>(R.id.prevButton)
        val nextBtn = findViewById<Button>(R.id.nextButton)

        // Actualiza la imagen y los textos
        fun actualizarContenido() {
            // Si tuvieras imágenes, descomenta esta línea
            // imageView.setImageResource(imagenes[indexImagen])

            nombreLugar.text = "Lugar ${indexImagen + 1}"
            descripcionLugar.text = "Descripción del lugar ${indexImagen + 1} con curiosidades interesantes."
        }

        // Acción para el botón de "prev"
        prevBtn.setOnClickListener {
            indexImagen = if (indexImagen > 0) indexImagen - 1 else totalLugares - 1
            actualizarContenido()
        }

        // Acción para el botón de "next"
        nextBtn.setOnClickListener {
            indexImagen = (indexImagen + 1) % totalLugares
            actualizarContenido()
        }

        actualizarContenido()

        // Acción para el botón de maps
        findViewById<Button>(R.id.mapsButton).setOnClickListener {
            showToast("Ir a Google Maps")
        }

        // Acción para el botón de favorito
        findViewById<Button>(R.id.favoritoButton).setOnClickListener {
            showToast("Añadido a favoritos")
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
