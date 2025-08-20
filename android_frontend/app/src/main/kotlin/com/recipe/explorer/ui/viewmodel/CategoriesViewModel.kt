package com.recipe.explorer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipe.explorer.data.RecipeRepositorySqlite
import com.recipe.explorer.data.local.CategoryEntity
import com.recipe.explorer.data.local.RecipeEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Categories tab, handling category list and filtered recipes.
 */
class CategoriesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RecipeRepositorySqlite.getInstance(app)

    val categories: LiveData<List<CategoryEntity>> = repo.categories()

    private val selectedCategory = MutableStateFlow<String?>(null)
    private val _recipes = MutableLiveData<List<RecipeEntity>>(emptyList())
    val recipes: LiveData<List<RecipeEntity>> = _recipes

    // PUBLIC_INTERFACE
    fun selectCategory(name: String?) {
        /** Select a category to filter recipes. If null, shows all. */
        viewModelScope.launch {
            selectedCategory.emit(name)
            val data = if (name.isNullOrBlank()) {
                repo.getAll().value ?: emptyList()
            } else {
                repo.byCategory(name).value ?: emptyList()
            }
            _recipes.postValue(data)
        }
    }
}
