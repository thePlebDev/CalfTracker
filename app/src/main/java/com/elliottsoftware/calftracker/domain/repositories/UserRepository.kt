package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.util.AppTheme
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val appTheme: Flow<AppTheme>

    suspend fun updateAppTheme(appTheme: AppTheme)
}