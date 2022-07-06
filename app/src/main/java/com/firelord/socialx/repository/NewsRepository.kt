package com.firelord.socialx.repository

import com.firelord.socialx.api.RetrofitInstance
import com.firelord.socialx.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getAllNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlineNews(countryCode,pageNumber)
}