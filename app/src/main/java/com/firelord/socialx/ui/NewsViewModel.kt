package com.firelord.socialx.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firelord.socialx.models.NewsResponse
import com.firelord.socialx.repository.NewsRepository
import com.firelord.socialx.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
): ViewModel() {

    val allNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val allNewsPage = 1

    init {
        getAllNews("in",)
    }

    fun getAllNews(countryCode: String) = viewModelScope.launch {
        allNews.postValue(Resource.Loading())

        val response = newsRepository.getAllNews(countryCode,allNewsPage)
        allNews.postValue(handleAllNewsResponse(response))
    }

    private fun handleAllNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}