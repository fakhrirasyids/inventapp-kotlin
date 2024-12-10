package com.fakhrirasyids.inventapp.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fakhrirasyids.inventapp.R
import com.fakhrirasyids.inventapp.data.models.TransactionHistory
import com.fakhrirasyids.inventapp.databinding.ItemTransactionsRowBinding
import com.fakhrirasyids.inventapp.utils.Constants.asFormattedDate

class TransactionAdapter :
    ListAdapter<TransactionHistory, TransactionAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemTransactionsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { listStory ->
            listStory?.let { item ->
                with(holder.binding) {
                    ivTransaction.setImageURI(Uri.parse(item.image))
                    tvType.text = item.title
                    tvTime.text = item.timestamp.asFormattedDate

                    if (item.isAdded) {
                        tvAmount.setTextColor(
                            ContextCompat.getColor(
                                holder.binding.root.context,
                                R.color.primaryGreen
                            )
                        )
                    } else {
                        tvAmount.setTextColor(
                            ContextCompat.getColor(
                                holder.binding.root.context,
                                R.color.primaryRed
                            )
                        )
                    }

                    tvAmount.text = if (item.isAdded) "+ ${item.quantity}" else "- ${item.quantity}"
                }
            }
        }
    }

    inner class ViewHolder(var binding: ItemTransactionsRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionHistory>() {
            override fun areItemsTheSame(
                oldStories: TransactionHistory,
                newStories: TransactionHistory
            ): Boolean {
                return oldStories == newStories
            }

            override fun areContentsTheSame(
                oldStories: TransactionHistory,
                newStories: TransactionHistory
            ): Boolean {
                return oldStories.id == newStories.id
            }
        }
    }
}