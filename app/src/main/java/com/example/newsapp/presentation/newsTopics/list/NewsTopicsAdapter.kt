package com.example.newsapp.presentation.newsTopics.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.NewsItemBinding

// The news recycler view adapter
// Requires 1 interactions, for single tap
class NewsTopicsAdapter(private val onSingleTap: ((Int, NewsTopicModel) -> Unit)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Item difference callback for checking the differences in a newly inserted list and an old one
    private val diffCallback = object : DiffUtil.ItemCallback<NewsTopicModel>() {
        override fun areItemsTheSame(oldItem: NewsTopicModel, newItem: NewsTopicModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: NewsTopicModel, newItem: NewsTopicModel): Boolean {
            return when {
                oldItem.name != newItem.name -> false
                oldItem.uriToImage != newItem.uriToImage -> false
                else -> true
            }
        }
    }

    // Allows for the execution of the item difference callback on a background thread
    private val differ = AsyncListDiffer(this, diffCallback)

    // Standard adapter binding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsTopicItemViewHolder(binding, onSingleTap)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsTopicItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<NewsTopicModel>) {
        differ.submitList(list)
    }

    // News topics recycler view item view holder
    class NewsTopicItemViewHolder constructor(
        private var binding: NewsItemBinding,
        private val onSingleTap: ((Int, NewsTopicModel) -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(binding.root) {

        // Contains a model it should be associated it
        private lateinit var newsTopicModel: NewsTopicModel

        // Function for binding
        fun bind(newsTopicModel: NewsTopicModel) {
            this.newsTopicModel = newsTopicModel
            binding.topicName.text = newsTopicModel.name
            Glide.with(binding.root).load(newsTopicModel.uriToImage).into(binding.topicImage)
            binding.root.setOnClickListener {
                onSingleTap?.let { it1 -> it1(adapterPosition, newsTopicModel) }
            }
        }
    }
}