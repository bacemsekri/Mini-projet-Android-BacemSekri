package com.mbds.bmp.newsletter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.databinding.ItemCategoryBinding
import com.mbds.bmp.newsletter.model.Category
import com.mbds.bmp.newsletter.tools.setImageFromUrl

class CategoryAdapter (private val dataSet: List<Category>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(private val root: View, private val dataSet: List<Category>) :
        RecyclerView.ViewHolder(root) {

        internal val binding = ItemCategoryBinding.bind(root)

        fun bind(item: Category) {
            binding.category = item
            binding.categoryImage.setImageFromUrl(item.image, R.drawable.placeholder_small, root)
            binding.categoryName.text = root.context.getText(item.nameId)

            binding.categoryCard.setOnClickListener {
                dataSet.forEach { category -> category.active = false }
                item.active = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(rootView, dataSet)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

}