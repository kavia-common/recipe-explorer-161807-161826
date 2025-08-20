package com.recipe.explorer.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Simple SQLiteOpenHelper replacing Room for environments without annotation processing.
 * Holds two tables: recipes and categories.
 */
class DbHelper(context: Context) : SQLiteOpenHelper(context, "recipes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE recipes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "ingredients TEXT NOT NULL," +
                    "steps TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "is_favorite INTEGER NOT NULL DEFAULT 0)"
        )
        db.execSQL("CREATE TABLE categories (name TEXT PRIMARY KEY NOT NULL)")

        // seed categories
        val categories = listOf("Breakfast","Lunch","Dinner","Dessert","Snack","Vegetarian","Vegan")
        categories.forEach { name ->
            val cv = ContentValues().apply { put("name", name) }
            db.insert("categories", null, cv)
        }

        // seed sample recipes
        insertRecipe(
            db, RecipeEntity(
                title = "Avocado Toast",
                description = "Crispy toast with mashed avocado, salt, pepper, and chili flakes.",
                ingredients = "Bread; Avocado; Salt; Pepper; Chili flakes; Lemon juice",
                steps = "Toast bread; Mash avocado; Mix with lemon; Spread; Season; Serve.",
                category = "Breakfast",
                isFavorite = false
            )
        )
        insertRecipe(
            db, RecipeEntity(
                title = "Veggie Pasta",
                description = "Pasta with sautéed vegetables and olive oil.",
                ingredients = "Pasta; Zucchini; Bell pepper; Onion; Garlic; Olive oil; Salt",
                steps = "Boil pasta; Sauté veg; Combine; Season; Serve.",
                category = "Dinner",
                isFavorite = true
            )
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // no-op for version 1
    }

    private fun insertRecipe(db: SQLiteDatabase, r: RecipeEntity) {
        val cv = ContentValues().apply {
            put("title", r.title)
            put("description", r.description)
            put("ingredients", r.ingredients)
            put("steps", r.steps)
            put("category", r.category)
            put("is_favorite", if (r.isFavorite) 1 else 0)
        }
        db.insert("recipes", null, cv)
    }

    fun getAllRecipes(): List<RecipeEntity> {
        readableDatabase.rawQuery("SELECT * FROM recipes ORDER BY title ASC", null).use { c ->
            return readRecipes(c)
        }
    }

    fun searchRecipes(query: String): List<RecipeEntity> {
        val args = arrayOf("%$query%", "%$query%")
        readableDatabase.rawQuery(
            "SELECT * FROM recipes WHERE title LIKE ? OR description LIKE ? ORDER BY title ASC", args
        ).use { c -> return readRecipes(c) }
    }

    fun getRecipesByCategory(category: String): List<RecipeEntity> {
        readableDatabase.rawQuery(
            "SELECT * FROM recipes WHERE category = ? ORDER BY title ASC", arrayOf(category)
        ).use { c -> return readRecipes(c) }
    }

    fun getFavoriteRecipes(): List<RecipeEntity> {
        readableDatabase.rawQuery(
            "SELECT * FROM recipes WHERE is_favorite = 1 ORDER BY title ASC", null
        ).use { c -> return readRecipes(c) }
    }

    fun insertRecipe(r: RecipeEntity): Long {
        val cv = ContentValues().apply {
            put("title", r.title)
            put("description", r.description)
            put("ingredients", r.ingredients)
            put("steps", r.steps)
            put("category", r.category)
            put("is_favorite", if (r.isFavorite) 1 else 0)
        }
        return writableDatabase.insert("recipes", null, cv)
    }

    fun updateRecipe(r: RecipeEntity) {
        val cv = ContentValues().apply {
            put("title", r.title)
            put("description", r.description)
            put("ingredients", r.ingredients)
            put("steps", r.steps)
            put("category", r.category)
            put("is_favorite", if (r.isFavorite) 1 else 0)
        }
        writableDatabase.update("recipes", cv, "id = ?", arrayOf(r.id.toString()))
    }

    fun deleteRecipe(r: RecipeEntity) {
        writableDatabase.delete("recipes", "id = ?", arrayOf(r.id.toString()))
    }

    fun setFavorite(id: Long, favorite: Boolean) {
        val cv = ContentValues().apply { put("is_favorite", if (favorite) 1 else 0) }
        writableDatabase.update("recipes", cv, "id = ?", arrayOf(id.toString()))
    }

    fun getById(id: Long): RecipeEntity? {
        readableDatabase.rawQuery(
            "SELECT * FROM recipes WHERE id = ? LIMIT 1", arrayOf(id.toString())
        ).use { c -> return if (c.moveToFirst()) readRecipe(c) else null }
    }

    fun getAllCategories(): List<CategoryEntity> {
        readableDatabase.rawQuery("SELECT * FROM categories ORDER BY name ASC", null).use { c ->
            val res = mutableListOf<CategoryEntity>()
            while (c.moveToNext()) {
                res.add(CategoryEntity(name = c.getString(c.getColumnIndexOrThrow("name"))))
            }
            return res
        }
    }

    private fun readRecipes(c: Cursor): List<RecipeEntity> {
        val res = mutableListOf<RecipeEntity>()
        while (c.moveToNext()) {
            res.add(readRecipe(c))
        }
        return res
    }

    private fun readRecipe(c: Cursor): RecipeEntity {
        return RecipeEntity(
            id = c.getLong(c.getColumnIndexOrThrow("id")),
            title = c.getString(c.getColumnIndexOrThrow("title")),
            description = c.getString(c.getColumnIndexOrThrow("description")),
            ingredients = c.getString(c.getColumnIndexOrThrow("ingredients")),
            steps = c.getString(c.getColumnIndexOrThrow("steps")),
            category = c.getString(c.getColumnIndexOrThrow("category")),
            isFavorite = c.getInt(c.getColumnIndexOrThrow("is_favorite")) == 1
        )
    }
}
