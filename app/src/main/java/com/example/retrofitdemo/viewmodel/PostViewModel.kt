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

    private val _state = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Post>>> = _state

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
}