package com.recipe.explorer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recipe.explorer.R
import com.recipe.explorer.ui.adapters.RecipeAdapter
import com.recipe.explorer.ui.screens.RecipeDetailBottomSheet
import com.recipe.explorer.ui.viewmodel.FavoritesViewModel

/**
 * Favorites tab without ViewBinding.
 */
class FavoritesFragment : Fragment() {

    private val vm: FavoritesViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): FavoritesFragment = FavoritesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RecipeAdapter {
            RecipeDetailBottomSheet.newInstance(it.id).show(parentFragmentManager, "recipe_detail")
        }
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        val emptyView = view.findViewById<LinearLayout>(R.id.empty_view)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        vm.favorites.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            emptyView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
