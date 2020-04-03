/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(val database: SleepDatabaseDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var currentNight = MutableLiveData<SleepNight?>()

    val allNights = database.getAllNights()

    val startButtonVisible = Transformations.map(currentNight){
        it == null
    }
    val stopButtonVisible = Transformations.map(currentNight){
        it != null
    }
    val clearButtonVisible = Transformations.map(allNights){
        it.isNotEmpty()
    }

    private val _onNavigatingToSleepQuality = MutableLiveData<SleepNight>()
    val onNavigatingToSleepQuality : LiveData<SleepNight>
        get() = _onNavigatingToSleepQuality

    init {
        initializeCurrentNight()
    }


    private fun initializeCurrentNight() {
        uiScope.launch{
            currentNight.value = getCurrentNightFromDataBase()
        }
    }
    private suspend fun getCurrentNightFromDataBase(): SleepNight? {
        return withContext(Dispatchers.IO){
            var night = database.getLastNight()
            if(night?.endTimeMilli != night?.startTimeMilli){
                night = null
            }
            night
        }
    }


    //click handler for Start button
    fun onStartTracking(){
        uiScope.launch {
            val newNight = SleepNight()
            insertNewNight(newNight)
            currentNight.value = getCurrentNightFromDataBase()
        }
    }
    private suspend fun insertNewNight(newNight: SleepNight) {
        withContext(Dispatchers.IO){
            database.insert(newNight)
        }

    }


    //click handler for Stop button
    fun onStopTracking(){
        uiScope.launch {
            val finishedNight = currentNight.value ?: return@launch
            finishedNight.endTimeMilli = System.currentTimeMillis()
            updateNight(finishedNight)
            _onNavigatingToSleepQuality.value = finishedNight
        }
    }
    private suspend fun updateNight(finishedNight: SleepNight) {
        withContext(Dispatchers.IO){
            database.update(finishedNight)
        }
    }


    //click handler for Clear button
    fun onClear(){
        uiScope.launch {
            clearDataBase()
            currentNight.value = null
        }
    }
    private suspend fun clearDataBase() {
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

    fun doneNavigating(){
        _onNavigatingToSleepQuality.value = null
    }
}

