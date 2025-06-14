package com.example.currencyconverter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangeCurrencyViewModel: ViewModel() {



    private var _readyToBack = MutableStateFlow(false)
    val readyToBack = _readyToBack.asStateFlow()

    fun saveTransaction(buyItem: CurrencyModel, sellItem: CurrencyModel){
        viewModelScope.launch {
            try {
                val repository = CurrencyRepository()
                repository.saveTransaction(buyItem, sellItem)
            } catch (e: Exception){

            }
        }
        _readyToBack.value = true
    }


}