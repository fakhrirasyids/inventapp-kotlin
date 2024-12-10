package com.fakhrirasyids.inventapp.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fakhrirasyids.inventapp.data.models.Stocks
import com.fakhrirasyids.inventapp.databinding.ItemStocksGridBinding
import com.fakhrirasyids.inventapp.utils.Constants.asRupiah

class StocksAdapter : ListAdapter<Stocks, StocksAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onStocksClick: ((Stocks, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemStocksGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { listStory ->
            listStory?.let { item ->
                with(holder.binding) {
                    ivStock.setImageURI(Uri.parse(item.image))
                    tvTitle.text = item.title
                    tvPrice.text = item.price.asRupiah

                    tvStock.text = StringBuilder("Stock : ${item.quantity}")

                    layoutCard.setOnClickListener {
                        onStocksClick?.invoke(item, ivStock)
                    }
                }
            }
        }
    }

    inner class ViewHolder(var binding: ItemStocksGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Stocks>() {
            override fun areItemsTheSame(
                oldStories: Stocks,
                newStories: Stocks
            ): Boolean {
                return oldStories == newStories
            }

            override fun areContentsTheSame(
                oldStories: Stocks,
                newStories: Stocks
            ): Boolean {
                return oldStories.id == newStories.id
            }
        }
    }
}