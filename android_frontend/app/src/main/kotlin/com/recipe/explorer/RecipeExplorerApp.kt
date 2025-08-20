package com.recipe.explorer

import android.app.Application


/**
 * Application class responsible for initializing app-wide singletons like the database.
 */
class RecipeExplorerApp : Application() {

    // PUBLIC_INTERFACE
    override fun onCreate() {
        /** Initialize application-level components. */
        super.onCreate()
        // Database is lazily initialized through AppDatabase.getInstance(context)
    }
}
