package com.example.weatherprojecttry_1.data

import android.content.SearchRecentSuggestionsProvider

class RecentQueryProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }
    companion object {
        const val AUTHORITY = "com.example.weatherprojecttry_1.data.RecentQueryProvider"
        const val MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }

}