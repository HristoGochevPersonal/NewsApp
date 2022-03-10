package com.example.newsapp.presentation.articles.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.FragmentArticleDetailsBinding
import com.example.newsapp.presentation.utils.*


class ArticleDetailsFragment : Fragment() {
    private var _binding: FragmentArticleDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArticleDetailsViewModel by viewModels()
    private val args: ArticleDetailsFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Updating the articles history
        viewModel.updateArticlesHistory(args.articleModel)

        // Initialized the view model observers
        initViewModelObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Updating the view with info from the model
        with(args.articleModel) {
            // Formatting the data
            val authorSource = "By $author, Source: ${source.name}"
            val publishedAtDate = dateParser.parseOrNull(publishedAt)
            val publishedAtFormatted = publishedAtDate?.let {
                dateFormatter.formatOrNull(publishedAtDate) ?: "Date unknown"
            } ?: "Date unknown"

            // Displaying it
            Glide.with(binding.root).load(urlToImage).into(binding.bigArticleImage)
            binding.title.text = title
            binding.authorSource.text = authorSource
            binding.publishedAt.text = publishedAtFormatted
            binding.description.text = description

            // Setting up the listeners
            binding.readMoreText.setOnClickListener {
                // Toggles between reading description and contents
                viewModel.toggleRead()
            }
            binding.openInWeb.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                startActivity(intent)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Initializes the view model observers
    private fun initViewModelObservers() {


        collectLatestLifecycleFlow(viewModel.readingContentStateFlow) {
            // If we are reading the whole contents
            if (it) {
                // Display the read less option
                val readLess="Read less…"
                binding.readMoreText.text=readLess
                binding.description.text = args.articleModel.content
            }
            else{ // Otherwise
                // Display the read more option
                val readMore="Read more…"
                binding.readMoreText.text=readMore
                binding.description.text = args.articleModel.description
            }
        }
    }

}