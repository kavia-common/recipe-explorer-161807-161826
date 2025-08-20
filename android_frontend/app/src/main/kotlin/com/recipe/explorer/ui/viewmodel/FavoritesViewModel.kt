package com.recipe.explorer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.recipe.explorer.data.RecipeRepositorySqlite
import com.recipe.explorer.data.local.RecipeEntity

/**
 * ViewModel for the Favorites tab.
 */
class FavoritesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RecipeRepositorySqlite.getInstance(app)
    val favorites: LiveData<List<RecipeEntity>> = repo.favorites()
}
