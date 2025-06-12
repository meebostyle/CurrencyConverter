package com.example.currencyconverter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.repository.CurrencyRepository
import com.example.currencyconverter.ui.model.CurrencyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyListViewModel: ViewModel() {


    private var _isProgressBarVisible = MutableStateFlow<Boolean>(false)
    val isProgressBarVisible = _isProgressBarVisible.asStateFlow()

    private var _isErrorVisible = MutableStateFlow<Boolean>(false)
    val isErrorVisible = _isErrorVisible.asStateFlow()

    private var _isContentVisible = MutableStateFlow<Boolean>(false)
    val isContentVisible = _isContentVisible.asStateFlow()

    private var _content = MutableStateFlow<List<CurrencyModel>?> (null)
    val content = _content.asStateFlow()


    private suspend fun loadContent(baseCurrencyCode: String ,
                                    amount: Double){
        val repository = CurrencyRepository()
        _content.value = repository.getCurrencyList(baseCurrencyCode,
            amount)
    }

     fun getContent(baseCurrencyCode: String = "USD",
                           amount: Double = 1.0) {
         _isContentVisible.value = false
         viewModelScope.launch {
             _isProgressBarVisible.value = true
             _isErrorVisible.value = false
             try {
                 loadContent(baseCurrencyCode,
                     amount)
             } catch (e: Exception){
                 _isErrorVisible.value = true
                 Log.e("Response Error", "$e")
             }
         }
    }

    fun onListUpdated(){
        _isContentVisible.value = true
        _isProgressBarVisible.value = false
    }



}