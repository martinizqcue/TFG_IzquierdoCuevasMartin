package com.example.sevillanasmaneras

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UbicacionActivity : AppCompatActivity() {

    private lateinit var nombreLugar: String
    private lateinit var db: FirebaseFirestore

    private val API_KEY = "AIzaSyA5k7CsIocNQfPMQdfC1PEu8WxcW_ppWF0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion)

        db = FirebaseFirestore.getInstance()
        nombreLugar = intent.getStringExtra("nombreLugar") ?: ""

        if (nombreLugar.isBlank()) {
            Toast.makeText(this, "No se recibió el lugar", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val nombreTextView = findViewById<TextView>(R.id.ubicacionLugarNombre)
        val recomendacionesText = findViewById<TextView>(R.id.recomendacionesTexto)
        val videoWebView = findViewById<WebView>(R.id.videoWebView)
        val btnAbrirMaps = findViewById<Button>(R.id.btnAbrirMaps)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        nombreTextView.text = nombreLugar

        db.collection("elementos")
            .whereEqualTo("nombre", nombreLugar)
            .get()
            .addOnSuccessListener { docs ->
                val doc = docs.documents.firstOrNull()
                if (doc != null) {
                    val recomendaciones = doc.getString("recomendaciones").orEmpty()
                    val ubicacion = doc.getString("ubicacionMaps").orEmpty()
                    val videoField = doc.getString("video").orEmpty() // puede ser ID o URL completa

                    recomendacionesText.text = recomendaciones

                    // Si Firestore trae un video directamente
                    if (videoField.isNotBlank()) {
                        val videoId = extraerVideoId(videoField)
                        mostrarVideoEnWebView(videoId, videoWebView)
                    } else {
                        // Buscarlo en YouTube por nombre
                        buscarVideoDesdeYouTube(nombreLugar, videoWebView)
                    }

                    btnAbrirMaps.setOnClickListener {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ubicacion)).apply {
                                setPackage("com.google.android.apps.maps")
                            })
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this, "Google Maps no está instalado", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Lugar no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar el lugar", Toast.LENGTH_SHORT).show()
                finish()
            }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun extraerVideoId(url: String): String {
        return when {
            url.contains("youtube.com/watch?v=") -> {
                Uri.parse(url).getQueryParameter("v") ?: ""
            }
            url.contains("youtu.be/") -> {
                Uri.parse(url).lastPathSegment ?: ""
            }
            else -> url // si ya es el ID directamente
        }
    }

    private fun mostrarVideoEnWebView(videoId: String, webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        val html = """
            <html>
            <body style="margin:0;">
                <iframe width="100%" height="100%" 
                    src="https://www.youtube.com/embed/$videoId" 
                    frameborder="0" allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
    }

    private fun buscarVideoDesdeYouTube(query: String, webView: WebView) {
        lifecycleScope.launch {
            try {
                val response = YouTubeClient.apiService.searchVideos(
                    query = query,
                    apiKey = API_KEY // ← Aquí pon tu clave real
                )
                val videoId = response.items.firstOrNull()?.id?.videoId
                if (videoId != null) {
                    val embedUrl = "https://www.youtube.com/embed/$videoId"
                    webView.settings.javaScriptEnabled = true
                    webView.webChromeClient = android.webkit.WebChromeClient()

                    val html = """
                    <html>
                    <body style="margin:0;">
                        <iframe width="100%" height="100%" 
                            src="$embedUrl"
                            frameborder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                            allowfullscreen>
                        </iframe>
                    </body>
                    </html>
                """.trimIndent()

                    webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
                } else {
                    Toast.makeText(this@UbicacionActivity, "No se encontró un video relacionado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@UbicacionActivity, "Error al cargar video: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
