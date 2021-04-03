package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.local.getDatabase
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = Repository(database)

    val picture =repository.pictureData
    val asteroids = repository.asteroidData

    init {
      viewModelScope.launch { repository.refreshData() }

    }
}