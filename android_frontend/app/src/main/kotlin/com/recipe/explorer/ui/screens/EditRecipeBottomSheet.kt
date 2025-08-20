package com.recipe.explorer.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.recipe.explorer.R
import com.recipe.explorer.ui.viewmodel.CategoriesViewModel
import com.recipe.explorer.ui.viewmodel.SharedRecipeViewModel

/**
 * Bottom sheet to add or edit a recipe without ViewBinding.
 */
class EditRecipeBottomSheet : BottomSheetDialogFragment() {

    private val vmShared: SharedRecipeViewModel by activityViewModels()
    private val vmCategories: CategoriesViewModel by viewModels()

    private var editId: Long? = null

    companion object {
        private const val ARG_EDIT_ID = "edit_id"

        // PUBLIC_INTERFACE
        fun newInstance(editId: Long?): EditRecipeBottomSheet {
            /** Factory method for new/edit recipe. Pass null for creating. */
            val f = EditRecipeBottomSheet()
            if (editId != null) {
                f.arguments = Bundle().apply { putLong(ARG_EDIT_ID, editId) }
            }
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editId = arguments?.getLong(ARG_EDIT_ID)
        editId?.let { vmShared.load(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.sheet_edit_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title = view.findViewById<EditText>(R.id.title)
        val description = view.findViewById<EditText>(R.id.description)
        val ingredients = view.findViewById<EditText>(R.id.ingredients)
        val steps = view.findViewById<EditText>(R.id.steps)
        val category = view.findViewById<Spinner>(R.id.category)
        val favorite = view.findViewById<CheckBox>(R.id.favorite)
        val saveButton = view.findViewById<View>(R.id.save_button)

        vmCategories.categories.observe(viewLifecycleOwner) { list ->
            val items = list.map { it.name }
            category.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        }

        vmShared.currentRecipe.observe(viewLifecycleOwner) { r ->
            if (r == null) return@observe
            title.setText(r.title)
            description.setText(r.description)
            ingredients.setText(r.ingredients)
            steps.setText(r.steps)
            favorite.isChecked = r.isFavorite

            val adapter = category.adapter
            if (adapter != null) {
                for (i in 0 until adapter.count) {
                    if (adapter.getItem(i) == r.category) {
                        category.setSelection(i)
                        break
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            val id = editId
            val t = title.text?.toString()?.trim().orEmpty()
            val d = description.text?.toString()?.trim().orEmpty()
            val ing = ingredients.text?.toString()?.trim().orEmpty()
            val st = steps.text?.toString()?.trim().orEmpty()
            val cat = category.selectedItem?.toString() ?: ""
            val fav = favorite.isChecked

            if (t.isNotBlank() && cat.isNotBlank()) {
                vmShared.saveOrUpdate(
                    id = id,
                    title = t,
                    description = d,
                    ingredients = ing,
                    steps = st,
                    category = cat,
                    isFavorite = fav
                ) { dismissAllowingStateLoss() }
            } else {
                // minimal inline validation feedback can be added here
            }
        }
    }
}
