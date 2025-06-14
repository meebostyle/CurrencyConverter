package com.example.currencyconverter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangeCurrencyViewModel: ViewModel() {

    private var _isProgressBarVisible = MutableStateFlow(false)
    val isProgressBarVisible = _isProgressBarVisible.asStateFlow()

    private var _isContentVisible = MutableStateFlow(true)
    val isContentVisible = _isContentVisible.asStateFlow()

    private var _readyToBack = MutableStateFlow(false)
    val readyToBack = _readyToBack.asStateFlow()

    fun saveTransaction(buyItem: CurrencyModel, sellItem: CurrencyModel){
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            _isContentVisible.value = false
            try {
                val repository = CurrencyRepository()
                repository.saveTransaction(buyItem, sellItem)
            } catch (e: Exception){

            }
        }
        _readyToBack.value = true
    }


}