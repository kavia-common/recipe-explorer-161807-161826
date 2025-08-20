package com.recipe.explorer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipe.explorer.data.RecipeRepositorySqlite
import com.recipe.explorer.data.local.RecipeEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Home tab to provide all recipes and search functionality.
 */
class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RecipeRepositorySqlite.getInstance(app)

    private val queryFlow = MutableStateFlow("")
    private val _recipes = MutableLiveData<List<RecipeEntity>>(emptyList())
    val recipes: LiveData<List<RecipeEntity>> = _recipes

    init {
        // Initialize with all items
        viewModelScope.launch {
            queryFlow.collect { q ->
                val data = if (q.isBlank()) {
                    repo.getAll().value ?: emptyList()
                } else {
                    repo.search(q).value ?: emptyList()
                }
                _recipes.postValue(data)
            }
        }
    }

    // PUBLIC_INTERFACE
    fun setQuery(q: String) {
        /** Update the search query to filter results. */
        viewModelScope.launch {
            queryFlow.emit(q.trim())
        }
    }
}
