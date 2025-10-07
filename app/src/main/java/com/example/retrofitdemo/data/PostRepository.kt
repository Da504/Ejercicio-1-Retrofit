package com.example.retrofitdemo.data

import com.example.retrofitdemo.model.Post
import com.example.retrofitdemo.network.RetrofitClient

class PostRepository {
    suspend fun fetchPosts(): List<Post> {
        return RetrofitClient.api.getPosts()
    }
}