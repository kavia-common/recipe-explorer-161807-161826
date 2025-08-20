package com.recipe.explorer.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.activityViewModels
import com.recipe.explorer.R
import com.recipe.explorer.ui.viewmodel.SharedRecipeViewModel

/**
 * Bottom sheet showing recipe details with actions to favorite, edit, or delete.
 */
class RecipeDetailBottomSheet : BottomSheetDialogFragment() {

    private val vm: SharedRecipeViewModel by activityViewModels()

    companion object {
        private const val ARG_ID = "id"

        // PUBLIC_INTERFACE
        fun newInstance(id: Long): RecipeDetailBottomSheet {
            /** Factory method to create a new instance for the given recipe id. */
            val f = RecipeDetailBottomSheet()
            f.arguments = Bundle().apply { putLong(ARG_ID, id) }
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = requireArguments().getLong(ARG_ID)
        vm.load(id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.sheet_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title = view.findViewById<TextView>(R.id.title)
        val category = view.findViewById<TextView>(R.id.category)
        val description = view.findViewById<TextView>(R.id.description)
        val ingredients = view.findViewById<TextView>(R.id.ingredients)
        val steps = view.findViewById<TextView>(R.id.steps)
        val favoriteIcon = view.findViewById<ImageView>(R.id.favorite_icon)
        val actionFavorite = view.findViewById<View>(R.id.action_favorite)
        val actionEdit = view.findViewById<View>(R.id.action_edit)
        val actionDelete = view.findViewById<View>(R.id.action_delete)

        vm.currentRecipe.observe(viewLifecycleOwner) { r ->
            if (r == null) return@observe
            title.text = r.title
            category.text = r.category
            description.text = r.description
            ingredients.text = r.ingredients
            steps.text = r.steps
            favoriteIcon.alpha = if (r.isFavorite) 1f else 0.2f

            actionFavorite.setOnClickListener {
                vm.toggleFavorite { /* UI updates via observer */ }
            }
            actionEdit.setOnClickListener {
                EditRecipeBottomSheet.newInstance(r.id).show(parentFragmentManager, "edit_recipe")
            }
            actionDelete.setOnClickListener {
                vm.deleteCurrent {
                    dismissAllowingStateLoss()
                }
            }
        }
    }
}
