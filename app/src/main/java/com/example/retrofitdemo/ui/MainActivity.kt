package com.example.retrofitdemo.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import com.example.retrofitdemo.viewmodel.PostViewModel
import com.example.retrofitdemo.viewmodel.UiState
import com.example.retrofitdemo.R

class MainActivity : ComponentActivity() {

    private val vm: PostViewModel by viewModels()
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.rv)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val tvError = findViewById<TextView>(R.id.tvError)

        adapter = PostAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        lifecycleScope.launchWhenStarted {
            vm.state.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        progress.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        progress.visibility = View.GONE
                        tvError.visibility = View.GONE
                        adapter.submit(state.data)
                    }
                    is UiState.Error -> {
                        progress.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                        tvError.text = state.message
                    }
                }
            }
        }

        vm.loadPosts()
    }
}