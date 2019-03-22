package com.freeletics.tutorial.mvi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freeletics.tutorial.mvi.model.Item
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_item.*

class ItemAdapter(private val layoutInflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0

    var items: List<Item> = mutableListOf()

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is Item -> VIEW_TYPE_ITEM
        else -> throw IllegalArgumentException("Cant determine view type at position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_ITEM -> ItemViewHolder(layoutInflater.inflate(R.layout.item_item, parent, false))
            else -> throw IllegalArgumentException("Can't create ViewHolder for viewType $viewType")
        }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is Item -> (holder as ItemViewHolder).bind(item)
        }
    }

    private class ItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: Item) {
            text.text = item.name
        }
    }
}