package com.example.newsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.newsapp.R
import com.example.newsapp.core.repository.NewsRepository

import com.example.newsapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        // Pop navigation backstack if it does not contain only the start fragment
        // If it does, resort to normal back press event
        val navController = findNavController(R.id.fragmentContainerView)
        val backQueue = navController.backQueue
        if (backQueue.size < 3) {
            super.onBackPressed()
        } else {
            // If the last fragment was the search fragment pop back stack twice
            if (backQueue[backQueue.size - 2].destination.id==R.id.searchFragment) {
                navController.popBackStack()
                navController.popBackStack()
            } else {
                navController.popBackStack()
            }
        }
    }
}