package com.example.newsapp.presentation.articles.list


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentArticlesBinding
import com.example.newsapp.presentation.articles.ArticleModel
import com.example.newsapp.presentation.utils.collectLatestLifecycleFlow


class ArticlesFragment : Fragment() {
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private val args: ArticlesFragmentArgs by navArgs()
    private  val viewModel: ArticlesViewModel by viewModels()
    private lateinit var adapter: ArticlesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Refreshes the articles recycler view
        viewModel.refreshArticles(args.searchQuery)

        // Initializing the view model observers
        initViewModelObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing the recycler view
        initRecyclerView()

        // Navigating back
        binding.backArticlesButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Initializes the recycler view
    private fun initRecyclerView() {
        val onSingleTap = { _: Int, item: ArticleModel ->
            openArticleDetails(item)
        }
        adapter = ArticlesAdapter(onSingleTap)
        binding.articlesRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = this@ArticlesFragment.adapter
        }
    }

    // Setting up the view model observers
    private fun initViewModelObservers() {
        collectLatestLifecycleFlow(viewModel.articlesStateFlow) {
            if (it.articles.isEmpty()) {
                adapter.submitList(it.articles)
                binding.noArticlesText.visibility = View.VISIBLE
            } else {
                binding.noArticlesText.visibility = View.GONE
                adapter.submitList(it.articles)
            }
        }
    }

    // Opens the article details fragment with an article model
    private fun openArticleDetails(article: ArticleModel) {
        val action =
            ArticlesFragmentDirections.actionArticlesFragmentToArticleDetailsFragment(article)
        findNavController().navigate(action)
    }
}