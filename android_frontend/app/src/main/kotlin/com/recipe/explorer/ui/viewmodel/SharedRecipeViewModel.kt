package com.recipe.explorer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipe.explorer.data.RecipeRepositorySqlite
import com.recipe.explorer.data.local.RecipeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Shared ViewModel for recipe detail and edit screens.
 */
class SharedRecipeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RecipeRepositorySqlite.getInstance(app)

    private val _currentRecipe = MutableLiveData<RecipeEntity?>()
    val currentRecipe: LiveData<RecipeEntity?> = _currentRecipe

    // PUBLIC_INTERFACE
    fun load(id: Long) {
        /** Loads a recipe by id from repository. */
        viewModelScope.launch(Dispatchers.IO) {
            val r = repo.getById(id)
            _currentRecipe.postValue(r)
        }
    }

    // PUBLIC_INTERFACE
    fun clearCurrent() {
        /** Clears current recipe selection. */
        _currentRecipe.value = null
    }

    // PUBLIC_INTERFACE
    fun toggleFavorite(onDone: (() -> Unit)? = null) {
        /** Toggles favorite flag for currently selected recipe. */
        val recipe = _currentRecipe.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repo.setFavorite(recipe.id, !recipe.isFavorite)
            val updated = repo.getById(recipe.id)
            _currentRecipe.postValue(updated)
            onDone?.invoke()
        }
    }

    // PUBLIC_INTERFACE
    fun saveOrUpdate(
        id: Long?,
        title: String,
        description: String,
        ingredients: String,
        steps: String,
        category: String,
        isFavorite: Boolean,
        onDone: (() -> Unit)? = null
    ) {
        /** Creates a new recipe or updates an existing one. */
        viewModelScope.launch(Dispatchers.IO) {
            if (id == null || id == 0L) {
                repo.add(
                    RecipeEntity(
                        title = title,
                        description = description,
                        ingredients = ingredients,
                        steps = steps,
                        category = category,
                        isFavorite = isFavorite
                    )
                )
            } else {
                repo.update(
                    RecipeEntity(
                        id = id,
                        title = title,
                        description = description,
                        ingredients = ingredients,
                        steps = steps,
                        category = category,
                        isFavorite = isFavorite
                    )
                )
            }
            onDone?.invoke()
        }
    }

    // PUBLIC_INTERFACE
    fun deleteCurrent(onDone: (() -> Unit)? = null) {
        /** Deletes the currently selected recipe. */
        val recipe = _currentRecipe.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(recipe)
            onDone?.invoke()
        }
    }
}
