package com.firelord.socialx.repository

import androidx.room.Query
import com.firelord.socialx.api.RetrofitInstance
import com.firelord.socialx.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getAllNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlineNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)
}