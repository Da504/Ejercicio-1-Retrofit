package com.example.retrofitdemo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import com.example.retrofitdemo.viewmodel.PostViewModel
import com.example.retrofitdemo.viewmodel.UiState
import com.example.retrofitdemo.R

class CreatePostActivity : ComponentActivity() {

    private val vm: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etBody = findViewById<EditText>(R.id.etBody)
        val btnCreate = findViewById<Button>(R.id.btnCreate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        lifecycleScope.launchWhenStarted {
            vm.postCreation.collectLatest { state ->
                tvResult.text = when (state) {
                    is UiState.Loading -> "Enviando..."
                    is UiState.Success -> " Creado: ID=${state.data.id}\nTítulo: ${state.data.title}"
                    is UiState.Error -> " Error: ${state.message}"
                }
            }
        }

        btnCreate.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val body = etBody.text.toString().trim()
            if (title.isNotEmpty() && body.isNotEmpty()) {
                vm.createPost(title, body)
            } else {
                tvResult.text = " Completa ambos campos"
            }
        }
    }
}