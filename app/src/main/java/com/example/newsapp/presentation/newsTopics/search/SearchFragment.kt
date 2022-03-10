package com.example.newsapp.presentation.newsTopics.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.presentation.utils.collectLatestLifecycleFlow
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import java.util.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter


    // Speech to text contract
    private val speechToTextContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // If voice was successfully recognized
            if (it.resultCode == Activity.RESULT_OK) {
                // Extract it
                val intent = it.data ?: return@registerForActivityResult
                val matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?: return@registerForActivityResult
                if (matches.isNotEmpty()) {
                    val search = matches[0]
                    // Update the search history
                    viewModel.updateSearches(search)
                    // Initiate a search
                    searchWithQuery(search)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Refresh the search history recycler view
        viewModel.refreshSearches()

        // Initializing the view model observers
        initViewModelObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing the recycler view
        initRecyclerView()


        // Navigates back
        binding.searchBarInstance.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Checks if we have entered text in the search bar
        binding.searchBarInstance.searchBarInput.addTextChangedListener {
            it?.let { text ->
                // If we have
                if (text.isNotEmpty()) {
                    // Display the clear icon
                    binding.searchBarInstance.speechButton.setImageResource(R.drawable.ic_baseline_clear_24)
                } else { // If we have not
                    // Display the speech to text icon
                    binding.searchBarInstance.speechButton.setImageResource(R.drawable.ic_baseline_mic_24)
                }
            }
        }

        // Whenever the text input is complete initiate a search with it
        binding.searchBarInstance.searchBarInput.setOnEditorActionListener { caller, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val search = caller.text.toString()
                viewModel.updateSearches(search)
                searchWithQuery(search)
            }
            true
        }

        // Starts the speech to text contract
        binding.searchBarInstance.speechButton.setOnClickListener {
            if (binding.searchBarInstance.searchBarInput.text.isNotEmpty()) {
                binding.searchBarInstance.searchBarInput.text.clear()
            } else {
                val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                speechIntent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")
                speechToTextContract.launch(speechIntent)
            }

        }

        // Deletes the search history
        binding.deleteSearchHistoryButton.setOnClickListener {
            viewModel.clearSearches()
        }

        // Requests focus of the search bar whenever the view is created
        binding.searchBarInstance.searchBarInput.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        // When the user single taps on a search item
        val onSingleTap = { _: Int, item: SearchItem ->
            // Update the search history
            viewModel.updateSearches(item.name)
            // And start a search
            searchWithQuery(item.name)
        }
        searchAdapter = SearchAdapter(onSingleTap)
        binding.searchHistory.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = searchAdapter
        }
    }


    // Setting up the view model observers
    private fun initViewModelObservers() {
        collectLatestLifecycleFlow(viewModel.searchesStateFlow) {
            searchAdapter.submitList(it)
        }
    }

    // Searches by navigating to the articles fragment with the given search query
    private fun searchWithQuery(query: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToArticlesFragment(query)
        findNavController().navigate(action)
    }
}