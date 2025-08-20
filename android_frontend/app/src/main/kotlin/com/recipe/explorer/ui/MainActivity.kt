package com.recipe.explorer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.recipe.explorer.R
import com.recipe.explorer.ui.fragments.CategoriesFragment
import com.recipe.explorer.ui.fragments.FavoritesFragment
import com.recipe.explorer.ui.fragments.HomeFragment
import com.recipe.explorer.ui.fragments.ProfileFragment
import com.recipe.explorer.ui.screens.EditRecipeBottomSheet

/**
 * The main entry point activity hosting tabbed navigation and the add button.
 */
class MainActivity : AppCompatActivity() {

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        /** Sets up the bottom navigation tabs and the add recipe action. */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Recipe Explorer"

        val nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        nav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.menu_home -> HomeFragment.newInstance()
                R.id.menu_categories -> CategoriesFragment.newInstance()
                R.id.menu_favorites -> FavoritesFragment.newInstance()
                R.id.menu_profile -> ProfileFragment.newInstance()
                else -> HomeFragment.newInstance()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit()
        }

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            EditRecipeBottomSheet.newInstance(null).show(supportFragmentManager, "edit_recipe")
        }
    }
}
