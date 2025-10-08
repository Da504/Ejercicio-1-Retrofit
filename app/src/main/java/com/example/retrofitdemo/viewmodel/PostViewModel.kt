package com.example.retrofitdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.retrofitdemo.data.PostRepository
import com.example.retrofitdemo.model.Post

class PostViewModel(
    private val repository: PostRepository = PostRepository()
) : ViewModel() {

    // Estado para la lista de posts
    private val _state = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Post>>> = _state

    // Estado para la creación de un post
    private val _postCreation = MutableStateFlow<UiState<Post>>(UiState.Loading)
    val postCreation: StateFlow<UiState<Post>> = _postCreation

    fun loadPosts() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val posts = repository.fetchPosts()
                _state.value = UiState.Success(posts)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun createPost(title: String, body: String) {
        viewModelScope.launch {
            _postCreation.value = UiState.Loading
            val post = Post(userId = 1, id = 0, title = title, body = body)
            val result = repository.sendPost(post)
            _postCreation.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Error al crear post") }
            )
        }
    }
}