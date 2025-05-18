package com.example.sevillanasmaneras

data class YouTubeResponse(
    val items: List<YouTubeVideoItem>
)

data class YouTubeVideoItem(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(val videoId: String)
data class Snippet(val title: String, val description: String)