package com.recipe.explorer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.recipe.explorer.R
import com.recipe.explorer.ui.adapters.RecipeAdapter
import com.recipe.explorer.ui.screens.RecipeDetailBottomSheet
import com.recipe.explorer.ui.viewmodel.CategoriesViewModel

/**
 * Categories tab with chips to filter recipes.
 */
class CategoriesFragment : Fragment() {

    private val vm: CategoriesViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): CategoriesFragment {
            /** Factory method to create a new instance of CategoriesFragment. */
            return CategoriesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RecipeAdapter {
            RecipeDetailBottomSheet.newInstance(it.id).show(parentFragmentManager, "recipe_detail")
        }
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group)
        val emptyView = view.findViewById<LinearLayout>(R.id.empty_view)

        vm.categories.observe(viewLifecycleOwner) { categories ->
            chipGroup.removeAllViews()
            val allChip = layoutInflater.inflate(R.layout.item_chip_filter, chipGroup, false) as Chip
            allChip.text = "All"
            allChip.isChecked = true
            allChip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) vm.selectCategory(null)
            }
            chipGroup.addView(allChip)

            categories.forEach { cat ->
                val chip = layoutInflater.inflate(R.layout.item_chip_filter, chipGroup, false) as Chip
                chip.text = cat.name
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        vm.selectCategory(cat.name)
                        chipGroup.children.forEach { v ->
                            if (v is Chip && v != chip) v.isChecked = false
                        }
                    }
                }
                chipGroup.addView(chip)
            }
        }

        vm.recipes.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            emptyView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
