package com.example.sevillanasmaneras

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class YouTubeActivity : AppCompatActivity() {

    private val apiKey = "AIzaSyA5k7CsIocNQfPMQdfC1PEu8WxcW_ppWF0" // Tu clave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val webView = findViewById<WebView>(R.id.youtubeWebView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // 🔍 Cambia aquí el término de búsqueda
        val query = "Alcázar de Sevilla"

        lifecycleScope.launch {
            try {
                val response = YouTubeClient.apiService.searchVideos(
                    query = query,
                    apiKey = apiKey
                )

                if (response.items.isNotEmpty()) {
                    val videoId = response.items[0].id.videoId
                    val url = "https://www.youtube.com/embed/$videoId"
                    webView.loadData(
                        "<iframe width=\"100%\" height=\"100%\" src=\"$url\" frameborder=\"0\" allowfullscreen></iframe>",
                        "text/html",
                        "utf-8"
                    )
                } else {
                    Toast.makeText(this@YouTubeActivity, "No se encontraron videos", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("YouTubeError", "Error al buscar videos: ${e.message}")
                Toast.makeText(this@YouTubeActivity, "Error al cargar video", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
