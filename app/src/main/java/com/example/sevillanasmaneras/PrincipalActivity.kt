package com.example.sevillanasmaneras

import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class PrincipalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    /*private val imagenes = listOf(
        R.drawable.imagen_lugar1,
        R.drawable.imagen_lugar2,
        R.drawable.imagen_lugar3
    )*/

    private var indexImagen = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        // Drawer y Toolbar
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_lugares -> showToast("Lugares")
                R.id.nav_tradiciones -> showToast("Tradiciones")
                R.id.nav_cultura -> showToast("Cultura")
                R.id.nav_monumentos -> showToast("Monumentos")
            }
            drawerLayout.closeDrawers()
            true
        }

        // Imagen y descripción
        val imageView = findViewById<ImageView>(R.id.imageCarousel)
        val nombreLugar = findViewById<TextView>(R.id.nombreLugarText)
        val descripcionLugar = findViewById<TextView>(R.id.descripcionLugarText)

        // Botones carrusel
        val prevBtn = findViewById<Button>(R.id.prevButton)
        val nextBtn = findViewById<Button>(R.id.nextButton)

        fun actualizarImagen() {
            //imageView.setImageResource(imagenes[indexImagen])
            nombreLugar.text = "Lugar ${indexImagen + 1}"
            descripcionLugar.text = "Descripción del lugar ${indexImagen + 1} con curiosidades."
        }

        prevBtn.setOnClickListener {
            if (indexImagen > 0) indexImagen--
            //else indexImagen = imagenes.size - 1
            actualizarImagen()
        }

        nextBtn.setOnClickListener {
            //indexImagen = (indexImagen + 1) % imagenes.size
            actualizarImagen()
        }

        actualizarImagen()

        // Botones acción
        findViewById<Button>(R.id.mapsButton).setOnClickListener {
            showToast("Ir a Google Maps")
        }

        findViewById<Button>(R.id.favoritoButton).setOnClickListener {
            showToast("Añadido a favoritos")
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
