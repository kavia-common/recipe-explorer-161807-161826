# Recipe Explorer Android App

A modern, minimalistic Android app to browse, search, and manage recipes with local persistence using Room (SQLite).
It features tabbed navigation (Home, Categories, Favorites, Profile), recipe cards, scrollable details, add/edit functionality, favorites, and category filtering.

## Tech
- Kotlin
- AndroidX + Material
- Room (SQLite)
- ViewModel + LiveData
- ViewBinding

## Theme
- Primary: #388e3c
- Secondary: #ffffff
- Accent: #ff7043
- Light theme

## Build and Run
From the android_frontend directory:
- ./gradlew build
- ./gradlew :app:installDebug

Then launch "Recipe Explorer" on the connected device/emulator.

## Structure
- data/local: Room entities, DAO, database
- data: Repository
- ui/viewmodel: ViewModels
- ui: MainActivity
- ui/fragments: Tabs (Home, Categories, Favorites, Profile)
- ui/adapters: Recycler adapters
- ui/screens: Bottom sheets for detail and edit

