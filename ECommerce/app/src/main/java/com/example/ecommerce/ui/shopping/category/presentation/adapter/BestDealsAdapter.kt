package com.example.ecommerce.ui.shopping.category.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.BestDealsRvItemBinding
import com.example.ecommerce.ui.shopping.category.data.Product

class BestDealsAdapter : RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>() {
    inner class BestDealsViewHolder(private val binding: BestDealsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                // Load the product image
                Glide.with(itemView).load(product.images[0]).into(imgBestDeal)

                // Check if there's an offer and calculate the price after the discount
                if (product.offerPercentage != null && product.offerPercentage > 0) {
                    val priceAfterOffer = product.price * (1f - product.offerPercentage)
                    tvNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                    tvNewPrice.visibility = View.VISIBLE
                } else {
                    tvNewPrice.visibility = View.INVISIBLE
                }

                // Always show the old price
                tvOldPrice.text = "$ ${String.format("%.2f", product.price)}"

                // Set the product name
                tvDealProductName.text = product.name
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(
            BestDealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null
}













