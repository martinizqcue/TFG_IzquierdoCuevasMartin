package com.example.sevillanasmaneras

import android.content.Intent
import android.graphics.BitmapFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("usuarios").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    val nombre = document.getString("nombre") ?: "Usuario"
                    userNameTextView.text = nombre
                }
        }

        userNameTextView.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = resources.getColor(android.R.color.black, theme)

        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener {
            val title = it.title?.toString() ?: ""
            toolbarTitle.text = title
            cargarElementosPorTipo(title)
            drawerLayout.closeDrawers()
            true
        }

        // Cargar por defecto los lugares
        cargarElementosPorTipo("Lugares")
    }

    private fun cargarElementosPorTipo(tipo: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("elementos")
            .whereEqualTo("tipo", tipo)
            .get()
            .addOnSuccessListener { documentos ->
                val elementos = documentos.mapNotNull { it.toObject(Elemento::class.java) }
                if (elementos.isNotEmpty()) {
                    mostrarElementos(elementos)
                } else {
                    showToast("No hay elementos del tipo $tipo")
                }
            }
            .addOnFailureListener {
                showToast("Error al cargar elementos")
            }
    }

    private fun mostrarElementos(elementos: List<Elemento>) {
        var index = 0
        val imageView = findViewById<ImageView>(R.id.imageCarousel)
        val nombreLugar = findViewById<TextView>(R.id.nombreLugarText)
        val descripcionLugar = findViewById<TextView>(R.id.descripcionLugarText)
        val prevBtn = findViewById<Button>(R.id.prevButton)
        val nextBtn = findViewById<Button>(R.id.nextButton)

        fun actualizar() {
            val elemento = elementos[index]
            cargarImagenEscalada(elemento.imagen, imageView)
            nombreLugar.text = elemento.nombre
            descripcionLugar.text = elemento.descripcion
        }

        actualizar()

        prevBtn.setOnClickListener {
            index = if (index > 0) index - 1 else elementos.size - 1
            actualizar()
        }

        nextBtn.setOnClickListener {
            index = (index + 1) % elementos.size
            actualizar()
        }

        findViewById<Button>(R.id.mapsButton).setOnClickListener {
            showToast("Ir a Google Maps")
        }

        findViewById<Button>(R.id.favoritoButton).setOnClickListener {
            showToast("Añadido a favoritos")
        }
    }

    private fun cargarImagenEscalada(nombreDrawable: String?, imageView: ImageView) {
        val resId = resources.getIdentifier(nombreDrawable, "drawable", packageName)
        if (resId != 0) {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeResource(resources, resId, options)

            options.inSampleSize = calculateInSampleSize(options, 800, 600)
            options.inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeResource(resources, resId, options)
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(R.drawable.placeholder)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
