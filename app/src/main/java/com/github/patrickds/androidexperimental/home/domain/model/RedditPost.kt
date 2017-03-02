package com.github.patrickds.androidexperimental.home.domain.model

data class RedditPost(
        val title: String,
        val category: String,
        val content: String,
        val author: String,
        val comments: Int,
        var votes: Int
)
