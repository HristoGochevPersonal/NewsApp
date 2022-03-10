package com.example.newsapp.presentation.articles.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentArticlesHistoryBinding
import com.example.newsapp.presentation.articles.ArticleModel
import com.example.newsapp.presentation.articles.list.ArticlesAdapter
import com.example.newsapp.presentation.articles.list.ArticlesFragmentDirections
import com.example.newsapp.presentation.utils.collectLatestLifecycleFlow


class ArticlesHistoryFragment : Fragment() {
    private var _binding: FragmentArticlesHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArticlesHistoryViewModel by viewModels()
    private lateinit var adapter: ArticlesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing the view model observers
        initViewModelObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Refreshing the history whenever the view is recreated
        viewModel.refreshHistory()

        // Initializing the recycler view
        initRecyclerView()

        // Navigating back
        binding.backHistoryButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.deleteHistoryButton.setOnClickListener {
            displayDeleteHistoryConfirmation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        val onSingleTap = { _: Int, item: ArticleModel ->
            openArticleDetails(item)
        }
        val onLongPress = { _: Int, item: ArticleModel ->
            displayDeleteHistoryArticleConfirmation(item)
        }
        adapter = ArticlesAdapter(onSingleTap, onLongPress)
        binding.articlesHistoryRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = this@ArticlesHistoryFragment.adapter
        }
    }

    private fun initViewModelObservers() {
        collectLatestLifecycleFlow(viewModel.articlesStateFlow) {
            if (it.isEmpty()) {
                adapter.submitList(it)
                binding.noHistoryText.visibility = View.VISIBLE
            } else {
                binding.noHistoryText.visibility = View.GONE
                adapter.submitList(it)
            }
            adapter.submitList(it)
        }
    }

    // Opens the article details fragment with a model from the history
    private fun openArticleDetails(article: ArticleModel) {
        val action =
            ArticlesHistoryFragmentDirections.actionArticlesHistoryFragmentToArticleDetailsFragment(
                article
            )
        findNavController().navigate(action)
    }


    // Displays the confirmation dialog for the deletion of the whole history
    private fun displayDeleteHistoryConfirmation() {
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setTitle("Confirm")
            setMessage("Clear history?")
            setCancelable(false)
            setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                viewModel.deleteHistory()
            }
            setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }

    // Displays the confirmation dialog for the deletion of only one article
    private fun displayDeleteHistoryArticleConfirmation(article: ArticleModel) {
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setTitle("Confirm")
            setMessage("Delete selected item?")
            setCancelable(false)
            setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                viewModel.deleteHistoryArticle(article)
            }
            setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }
}