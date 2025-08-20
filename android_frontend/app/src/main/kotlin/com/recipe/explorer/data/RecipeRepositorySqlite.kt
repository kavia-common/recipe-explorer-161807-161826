package com.recipe.explorer.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.recipe.explorer.data.local.CategoryEntity
import com.recipe.explorer.data.local.DatabaseHelper
import com.recipe.explorer.data.local.RecipeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository backed by SQLiteOpenHelper; provides simple LiveData sources.
 */
class RecipeRepositorySqlite private constructor(context: Context) {

    private val db = DatabaseHelper(context.applicationContext)

    fun getAll(): LiveData<List<RecipeEntity>> = live { db.getAll() }

    fun search(query: String): LiveData<List<RecipeEntity>> = live { db.search(query) }

    fun favorites(): LiveData<List<RecipeEntity>> = live { db.favorites() }

    fun byCategory(category: String): LiveData<List<RecipeEntity>> = live { db.byCategory(category) }

    fun categories(): LiveData<List<CategoryEntity>> = live { db.categories() }

    suspend fun add(recipe: RecipeEntity): Long = withContext(Dispatchers.IO) { db.insertRecipe(recipe) }

    suspend fun update(recipe: RecipeEntity) = withContext(Dispatchers.IO) { db.updateRecipe(recipe) }

    suspend fun delete(recipe: RecipeEntity) = withContext(Dispatchers.IO) { db.deleteRecipe(recipe.id) }

    suspend fun setFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) { db.setFavorite(id, isFavorite) }

    suspend fun getById(id: Long): RecipeEntity? = withContext(Dispatchers.IO) { db.getById(id) }

    private fun <T> live(provider: () -> T): LiveData<T> {
        val ld = MutableLiveData<T>()
        ld.value = provider()
        return ld
    }

    companion object {
        @Volatile private var INSTANCE: RecipeRepositorySqlite? = null

        // PUBLIC_INTERFACE
        fun getInstance(context: Context): RecipeRepositorySqlite {
            /** Singleton accessor. */
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RecipeRepositorySqlite(context).also { INSTANCE = it }
            }
        }
    }
}
