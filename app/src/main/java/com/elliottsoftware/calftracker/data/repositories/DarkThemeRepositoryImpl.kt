package com.elliottsoftware.calftracker.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val SORT_ORDER_KEY = "sort_order"

class DarkThemeRepositoryImpl  private constructor(

    context: Context
) {

    //GETTING THE INSTANCE OF THE sharedPreferences
    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)


    // Keep the sort order as a stream of changes
    private val darkThemeFlow = MutableStateFlow(darkTheme)
    val sortOrderFlow: StateFlow<Boolean> = darkThemeFlow

    /**
     * Get the sort order. By default, sort order is None.
     */
    private val darkTheme: Boolean
        get() {
            val theme = sharedPreferences.getBoolean("darkTheme",false)
            return theme
        }

    companion object {
        @Volatile
        private var INSTANCE: DarkThemeRepositoryImpl? = null

        fun getInstance(context: Context): DarkThemeRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = DarkThemeRepositoryImpl(context)
                INSTANCE = instance
                instance
            }
        }
    }
}