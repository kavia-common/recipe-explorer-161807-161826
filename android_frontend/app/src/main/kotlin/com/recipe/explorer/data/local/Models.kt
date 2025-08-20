package com.recipe.explorer.data.local

/**
 * Plain data models (no Room annotations) to work with SQLiteOpenHelper.
 */
data class RecipeEntity(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val ingredients: String,
    val steps: String,
    val category: String,
    val isFavorite: Boolean = false
)

data class CategoryEntity(
    val name: String
)
