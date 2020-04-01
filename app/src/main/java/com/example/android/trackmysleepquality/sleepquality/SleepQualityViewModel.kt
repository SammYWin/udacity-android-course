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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(private val sleepNightId: Long = 0L, val dataBase: SleepDatabaseDao) : ViewModel(){

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _navigateToSleepTrackerEvent = MutableLiveData<Boolean?>()
    val navigateToSleepTrackerEvent: LiveData<Boolean?>
        get() = _navigateToSleepTrackerEvent

    fun navigationEventDone() {
        _navigateToSleepTrackerEvent.value = null
    }

    fun onSetSleepQuality(quality: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val currentNight = dataBase.getLastNight() ?: return@withContext
                currentNight.sleepQuality = quality
                dataBase.update(currentNight)
            }
            _navigateToSleepTrackerEvent.value = true
        }
    }

}
