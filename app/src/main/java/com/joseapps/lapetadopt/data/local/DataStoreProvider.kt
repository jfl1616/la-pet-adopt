package com.joseapps.lapetadopt.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.favoritesDataStore by preferencesDataStore(name = "favorites")
