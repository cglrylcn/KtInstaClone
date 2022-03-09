package com.caglar.ktinstaclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caglar.ktinstaclone.databinding.RecyclerRowBinding
import com.caglar.ktinstaclone.model.Post

class FeedAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedAdapter.PostHolder>() {

    class PostHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerViewEmailText.text = postList[position].email
        holder.binding.recyclerViewCommentText.text = postList[position].comment

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}