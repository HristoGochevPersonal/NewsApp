package com.example.newsapp.presentation.newsTopics.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.SearchItemBinding

// The search history recycler view adapter
// Requires 1 interaction, for single tap
class SearchAdapter(  private val onSingleTap: ((Int, SearchItem) -> Unit)?=null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Item difference callback for checking the differences in a newly inserted list and an old one
    private val diffCallback = object : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(
            oldItem: SearchItem,
            newItem: SearchItem
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: SearchItem,
            newItem: SearchItem
        ): Boolean {
            return when {
                oldItem.name != newItem.name -> false
                else -> true
            }
        }
    }

    // Allows for the execution of the item difference callback on a background thread
    private val differ = AsyncListDiffer(this, diffCallback)

    // Standard adapter binding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryItemViewHolder(binding,onSingleTap)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchHistoryItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<SearchItem>) {
        differ.submitList(null)
        differ.submitList(list)
    }

    // Search history recycler view item view holder
    class SearchHistoryItemViewHolder constructor(
        private var binding: SearchItemBinding,
        private val onSingleTap: ((Int, SearchItem) -> Unit)?=null
    ) :
        RecyclerView.ViewHolder(binding.root) {

        // Contains a model it should be associated it
        private lateinit var searchHistoryItem: SearchItem

        // Function for binding
        fun bind(searchHistoryItem: SearchItem) {
            this.searchHistoryItem = searchHistoryItem
            binding.text.text = searchHistoryItem.name
            binding.root.setOnClickListener {
                onSingleTap?.let { it1 -> it1(adapterPosition,searchHistoryItem) }
            }
        }

    }
}