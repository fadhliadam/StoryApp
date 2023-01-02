package com.adam.submissionstoryapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adam.submissionstoryapp.databinding.ItemListStoryBinding
import com.adam.submissionstoryapp.model.StoryModel
import com.adam.submissionstoryapp.network.responses.ListStoryItem
import com.adam.submissionstoryapp.ui.detail.DetailActivity
import com.adam.submissionstoryapp.ui.detail.DetailActivity.Companion.EXTRA_STORY_DATA
import com.bumptech.glide.Glide

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK){
    class ListViewHolder(private var binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: ListStoryItem) {
            val storyModel = StoryModel()
            binding.tvItemName.text = data.name
            Glide.with(itemView)
                .load(data.photoUrl)
                .into(binding.imgItemPhoto)
            itemView.setOnClickListener{
                storyModel.apply {
                    id = data.id
                    name = data.name
                    description = data.description
                    photoUrl = data.photoUrl
                    createdAt = data.createdAt
                }
                val intent = Intent(itemView.context, DetailActivity::class.java)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgItemPhoto, "Image")
                    )
                intent.putExtra(EXTRA_STORY_DATA,storyModel)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}