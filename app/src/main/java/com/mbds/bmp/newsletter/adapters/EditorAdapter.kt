package com.mbds.bmp.newsletter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.databinding.ItemEditorBinding
import com.mbds.bmp.newsletter.model.Editor

class EditorAdapter(private val dataSet: List<Editor>) :
    RecyclerView.Adapter<EditorAdapter.ViewHolder>() {

    class ViewHolder(private val root: View, private val dataSet: List<Editor>) :
        RecyclerView.ViewHolder(root) {

        internal val binding = ItemEditorBinding.bind(root)

        fun bind(item: Editor) {
            binding.editor = item
            binding.editorName.text = item.name
            if (item.id.isNullOrBlank()) {
                binding.editorDescription.visibility = View.INVISIBLE
            } else {
                binding.editorDescription.text = item.description
            }

            binding.editorCard.setOnClickListener {
                if (item.id.isNullOrBlank()) {
                    val newValue = !item.active
                    dataSet.forEach { editor ->
                        editor.active = newValue
                    }
                } else {
                    item.active = !item.active

                    val newValueForNullChecked =
                        dataSet.filter { editor -> !editor.id.isNullOrBlank() }
                            .map { editor -> editor.active }
                            .reduce { e1, e2 -> e1 && e2 }
                    dataSet.filter { editor -> editor.id.isNullOrBlank() }
                        .forEach { editor -> editor.active = newValueForNullChecked }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_editor, parent, false)
        return ViewHolder(rootView, dataSet)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

}