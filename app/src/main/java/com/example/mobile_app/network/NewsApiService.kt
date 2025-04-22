package com.example.mobile_app.network



import com.example.mobile_app.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {
    @GET("api/1/news")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apikey") apiKey: String
    ): NewsResponse
}

