package com.developer.ivan.coreapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developer.ivan.coreapp.R
import com.developer.ivan.coreapp.databinding.ItemArticleBinding
import com.developer.ivan.coreapp.ui.main.UIArticle
import com.google.android.material.chip.Chip


class ArticleListAdapter(val onPressItem: (UIArticle, TextView)->Unit) : ListAdapter<UIArticle,ArticleListAdapter.ViewHolder>(object: DiffUtil.ItemCallback<UIArticle>() {
    override fun areItemsTheSame(oldItem: UIArticle, newItem: UIArticle): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UIArticle, newItem: UIArticle): Boolean =
        oldItem == newItem

}) {

    private lateinit var binding: ItemArticleBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_article,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.onBind(getItem(position))

    }

    inner class ViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(item: UIArticle) = with(binding){
            txtTitle.apply {
                htmlText = item.title
                transitionName = item.id.toString()

            }
            txtDescription.htmlText = item.description

            chTag.removeAllViews()

            item.subjects.forEach {subject->
                chTag.addView(Chip(binding.root.context).apply {
                    text = subject

                })
            }

            itemView.setOnClickListener {
                onPressItem(item, binding.txtTitle)
            }
        }

    }


}