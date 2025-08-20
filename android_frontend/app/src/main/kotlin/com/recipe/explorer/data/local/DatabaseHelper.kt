package com.recipe.explorer.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Lightweight SQLiteOpenHelper managing recipes and categories tables.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "recipes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "ingredients TEXT," +
                "steps TEXT," +
                "category TEXT," +
                "is_favorite INTEGER DEFAULT 0" +
            ");"
        )
        db.execSQL(
            "CREATE TABLE categories (" +
                "name TEXT PRIMARY KEY NOT NULL" +
            ");"
        )
        // Seed categories
        val categories = listOf("Breakfast","Lunch","Dinner","Dessert","Snack","Vegetarian","Vegan")
        categories.forEach { name ->
            val cv = ContentValues().apply { put("name", name) }
            db.insert("categories", null, cv)
        }
        // Seed a few recipes
        insertRecipe(
            db, RecipeEntity(
                id = 0L,
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
                id = 0L,
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
        // For now, drop and recreate
        db.execSQL("DROP TABLE IF EXISTS recipes")
        db.execSQL("DROP TABLE IF EXISTS categories")
        onCreate(db)
    }

    private fun insertRecipe(db: SQLiteDatabase, r: RecipeEntity): Long {
        val cv = ContentValues().apply {
            put("title", r.title)
            put("description", r.description)
            put("ingredients", r.ingredients)
            put("steps", r.steps)
            put("category", r.category)
            put("is_favorite", if (r.isFavorite) 1 else 0)
        }
        return db.insert("recipes", null, cv)
    }

    fun insertRecipe(r: RecipeEntity): Long = insertRecipe(writableDatabase, r)

    fun updateRecipe(r: RecipeEntity) {
        val cv = ContentValues().apply {
            put("title", r.title)
            put("description", r.description)
            put("ingredients", r.ingredients)
            put("steps", r.steps)
            put("category", r.category)
            put("is_favorite", if (r.isFavorite) 1 else 0)
        }
        writableDatabase.update("recipes", cv, "id=?", arrayOf(r.id.toString()))
    }

    fun deleteRecipe(id: Long) {
        writableDatabase.delete("recipes", "id=?", arrayOf(id.toString()))
    }

    fun setFavorite(id: Long, favorite: Boolean) {
        val cv = ContentValues().apply { put("is_favorite", if (favorite) 1 else 0) }
        writableDatabase.update("recipes", cv, "id=?", arrayOf(id.toString()))
    }

    fun getById(id: Long): RecipeEntity? {
        val c = readableDatabase.rawQuery("SELECT * FROM recipes WHERE id=? LIMIT 1", arrayOf(id.toString()))
        c.use { if (it.moveToFirst()) return mapRecipe(it) }
        return null
    }

    fun getAll(): List<RecipeEntity> = queryToList("SELECT * FROM recipes ORDER BY title ASC")

    fun search(query: String): List<RecipeEntity> = queryToList(
        "SELECT * FROM recipes WHERE title LIKE ? OR description LIKE ? ORDER BY title ASC",
        arrayOf("%$query%", "%$query%")
    )

    fun favorites(): List<RecipeEntity> = queryToList("SELECT * FROM recipes WHERE is_favorite=1 ORDER BY title ASC")

    fun byCategory(category: String): List<RecipeEntity> =
        queryToList("SELECT * FROM recipes WHERE category=? ORDER BY title ASC", arrayOf(category))

    fun categories(): List<CategoryEntity> {
        val list = mutableListOf<CategoryEntity>()
        val c = readableDatabase.rawQuery("SELECT name FROM categories ORDER BY name ASC", emptyArray())
        c.use {
            while (it.moveToNext()) {
                list.add(CategoryEntity(it.getString(0)))
            }
        }
        return list
    }

    private fun queryToList(sql: String, args: Array<String> = emptyArray()): List<RecipeEntity> {
        val list = mutableListOf<RecipeEntity>()
        val c = readableDatabase.rawQuery(sql, args)
        c.use {
            while (it.moveToNext()) {
                list.add(mapRecipe(it))
            }
        }
        return list
    }

    private fun mapRecipe(c: Cursor): RecipeEntity {
        val id = c.getLong(c.getColumnIndexOrThrow("id"))
        val title = c.getString(c.getColumnIndexOrThrow("title"))
        val description = c.getString(c.getColumnIndexOrThrow("description"))
        val ingredients = c.getString(c.getColumnIndexOrThrow("ingredients"))
        val steps = c.getString(c.getColumnIndexOrThrow("steps"))
        val category = c.getString(c.getColumnIndexOrThrow("category"))
        val fav = c.getInt(c.getColumnIndexOrThrow("is_favorite")) == 1
        return RecipeEntity(id, title, description, ingredients, steps, category, fav)
    }
}
