package com.firelord.socialx.models

import com.firelord.socialx.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)