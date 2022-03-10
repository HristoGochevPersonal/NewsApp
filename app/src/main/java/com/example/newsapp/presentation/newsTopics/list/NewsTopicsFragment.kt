package com.example.newsapp.presentation.newsTopics.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsTopicsBinding
import com.example.newsapp.presentation.MainActivity
import com.example.newsapp.presentation.utils.collectLatestLifecycleFlow
import kotlin.reflect.typeOf

class NewsTopicsFragment : Fragment() {
    private var _binding: FragmentNewsTopicsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsTopicsViewModel by viewModels()
    private lateinit var topicsAdapter: NewsTopicsAdapter
    private var navigateToHistory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Refreshes the topics with info
        viewModel.refreshTopics()

        // Initializing the view model observers
        initViewModelObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsTopicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing the recycler view
        initRecyclerView()


        // Refreshes the read articles counter
        viewModel.refreshArticlesRead()

        binding.navBarInstance.searchButton.setOnClickListener {
            openSearch()
        }
        binding.navBarInstance.searchBarInput.setOnClickListener {
            openSearch()
        }
        // Navigation drawer controls
        binding.navBarInstance.navButton.setOnClickListener {
            binding.root.openDrawer(GravityCompat.START)
        }

        // Opens the history fragment if it has been selected
        // Once the drawer is closed
        binding.drawer.setNavigationItemSelectedListener {
            if (it.itemId == R.id.tab1) {
                navigateToHistory = true
            }
            binding.root.closeDrawer(GravityCompat.START)
            true
        }
        binding.root.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerClosed(drawerView: View) {
                if (navigateToHistory) {
                    navigateToHistory = false
                    openHistory()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Initializes the recycler view
    private fun initRecyclerView() {
        val onSingleTap = { _: Int, item: NewsTopicModel ->
            openWithQuery(item.name)
        }
        topicsAdapter = NewsTopicsAdapter(onSingleTap)
        binding.newsRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = this@NewsTopicsFragment.topicsAdapter
        }
    }

    // Setting up the view model observers
    private fun initViewModelObservers() {
        collectLatestLifecycleFlow(viewModel.newsTopicsStateFlow) {
            if (it.isEmpty()) {
                topicsAdapter.submitList(it)
                binding.noNewsText.visibility = View.VISIBLE
            } else {
                binding.noNewsText.visibility = View.GONE
                topicsAdapter.submitList(it)
            }
        }
        // Updating the count of articles read
        collectLatestLifecycleFlow(viewModel.articlesReadStateFlow) {
            val update = "Total number of read articles: $it"
            binding.newsReadHeader.text = update
        }
    }

    // Opens the search fragment
    private fun openSearch() {
        val action = NewsTopicsFragmentDirections.actionNewsFragmentToSearchFragment()
        findNavController().navigate(action)
    }

    // Opens the history fragment
    private fun openHistory() {
        val action = NewsTopicsFragmentDirections.actionNewsFragmentToArticlesHistoryFragment()
        findNavController().navigate(action)
    }

    // Opens the articles fragment with a search query
    private fun openWithQuery(query: String) {
        val action = NewsTopicsFragmentDirections.actionNewsFragmentToArticlesFragment(query)
        findNavController().navigate(action)
    }
}