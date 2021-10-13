package com.solovev.mirroroftheworld

import androidx.lifecycle.ViewModel
import com.solovev.mirroroftheworld.ui.main.Crime


class DiaryListViewModel : ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()
    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }
}