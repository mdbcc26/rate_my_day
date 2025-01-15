package com.example.rate_my_day.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rate_my_day.data.Preferences
import com.example.rate_my_day.data.RateDayRepository
import com.example.rate_my_day.data.db.RateDayEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RateMyDayViewModel(private val repository: RateDayRepository) : ViewModel() {

    private val _ratedDays = MutableStateFlow<List<RateDayEntity>>(emptyList())
    val ratedDays: StateFlow<List<RateDayEntity>> = _ratedDays.asStateFlow()

    init {
        fetchRatedDays()
    }

    private fun fetchRatedDays() {
        viewModelScope.launch {
            repository.getAllRatedDays().collect { ratedDays ->
                _ratedDays.value = ratedDays
            }
        }
    }

    fun saveRatedDay(rateDay: RateDayEntity) {
        viewModelScope.launch {
            repository.saveRatedDay(rateDay)
        }
    }

    fun deleteRatedDayByDate(date: Long) {
        viewModelScope.launch {
            repository.deleteRatedDayByDate(date)
            fetchRatedDays() // Refresh the state after deletion
        }
    }

}