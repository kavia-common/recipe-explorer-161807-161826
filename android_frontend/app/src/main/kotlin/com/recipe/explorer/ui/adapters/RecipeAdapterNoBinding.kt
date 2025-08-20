package com.recipe.explorer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recipe.explorer.R
import com.recipe.explorer.data.local.RecipeEntity

/**
 * RecyclerView adapter for displaying recipes as cards.
 */
class RecipeAdapter(
    private val onClick: (RecipeEntity) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.VH>() {

    private val items: MutableList<RecipeEntity> = mutableListOf()

    // PUBLIC_INTERFACE
    fun submitList(list: List<RecipeEntity>) {
        /** Replace the current list with the given list and refresh. */
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val description: TextView = view.findViewById(R.id.description)
        private val category: TextView = view.findViewById(R.id.category)
        private val favoriteIcon: ImageView = view.findViewById(R.id.favorite_icon)

        fun bind(item: RecipeEntity) {
            title.text = item.title
            description.text = item.description
            category.text = item.category
            favoriteIcon.alpha = if (item.isFavorite) 1f else 0.2f
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_recipe_card, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
