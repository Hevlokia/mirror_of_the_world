package com.solovev.mirroroftheworld

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.solovev.mirroroftheworld.ui.main.Crime
import java.util.*

class DiaryDetailViewModel() : ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()
    var diaryLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData) { crimeId ->
            crimeRepository.getCrime(crimeId)
        }
    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }
    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }
}