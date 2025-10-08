package com.example.retrofitdemo.data

import com.example.retrofitdemo.model.Post
import com.example.retrofitdemo.network.RetrofitClient
import kotlin.Result

class PostRepository {
    suspend fun fetchPosts(): List<Post> {
        return RetrofitClient.api.getPosts()
    }

    suspend fun sendPost(post: Post): Result<Post> {
        return try {
            val response = RetrofitClient.api.createPost(post)
            if (response.isSuccessful) {
                val createdPost = response.body() ?: post.copy(id = -1)
                Result.success(createdPost)
            } else {
                Result.failure(IllegalStateException("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}