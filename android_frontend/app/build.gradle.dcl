androidApplication {
    namespace = "com.recipe.explorer"

    // Ensure the applicationId aligns with the namespace for resource resolution
    applicationId = "com.recipe.explorer"

    dependencies {
        // AndroidX core
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.2")

        // Lifecycle ViewModel and LiveData
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
        // Fragment KTX for viewModels() delegates
        implementation("androidx.fragment:fragment-ktx:1.8.2")

        // SQLiteOpenHelper is part of Android SDK; no extra dependency required

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    }
}
