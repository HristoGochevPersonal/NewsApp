package com.example.newsapp.presentation.articles.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ArticlesItemBinding
import com.example.newsapp.presentation.articles.ArticleModel

// The articles recycler view adapter
// Requires 2 interactions, for single tap and long press ( optional )
class ArticlesAdapter(
    private val onSingleTap: ((Int, ArticleModel) -> Unit)? = null,
    private val onLongPress: ((Int, ArticleModel) -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Item difference callback for checking the differences in a newly inserted list and an old one
    private val diffCallback = object : DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return when {
                oldItem.title != newItem.title -> false
                oldItem.author != newItem.author -> false
                oldItem.url != newItem.url -> false
                oldItem.description != newItem.description -> false
                oldItem.content != newItem.content -> false
                oldItem.publishedAt != newItem.publishedAt -> false
                oldItem.urlToImage != newItem.urlToImage -> false
                oldItem.source != newItem.source -> false
                else -> true
            }
        }
    }

    // Allows for the execution of the item difference callback on a background thread
    private val differ = AsyncListDiffer(this, diffCallback)

    // Standard adapter binding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ArticlesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticlesItemViewHolder(binding, onSingleTap,onLongPress)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticlesItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ArticleModel>) {
        differ.submitList(list)
    }

    // Articles recycler view item view holder
    class ArticlesItemViewHolder constructor(
        private var binding: ArticlesItemBinding,
        private val onSingleTap: ((Int, ArticleModel) -> Unit)? = null,
        private val onLongPress: ((Int, ArticleModel) -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(binding.root) {

        // Contains a model it should be associated it
        private lateinit var articleModel: ArticleModel

        // Function for binding
        fun bind(articleModel: ArticleModel) {
            this.articleModel = articleModel
            binding.articleName.text = articleModel.title
            Glide.with(binding.root).load(articleModel.urlToImage).into(binding.articleImage)
            binding.root.clipToOutline = true
            binding.root.setOnClickListener {
                onSingleTap?.let { it1 -> it1(adapterPosition, articleModel) }
            }
            binding.root.setOnLongClickListener {
                onLongPress?.let { it1 -> it1(adapterPosition, articleModel) }
                true
            }
        }
    }
}