package com.example.sevillanasmaneras

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YouTubeClient {
    val apiService: YouTubeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YouTubeApiService::class.java)
    }
}
