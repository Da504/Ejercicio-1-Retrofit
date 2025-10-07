package com.example.retrofitdemo.network

import com.example.retrofitdemo.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}