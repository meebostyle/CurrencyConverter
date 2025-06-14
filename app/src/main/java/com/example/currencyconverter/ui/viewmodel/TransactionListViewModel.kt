package com.example.currencyconverter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.model.CurrencyTransactionModel
import com.example.currencyconverter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TransactionListViewModel: ViewModel() {

    private var _isProgressBarVisible = MutableStateFlow(false)
    val isProgressBarVisible = _isProgressBarVisible.asStateFlow()

    private var _isContentVisible = MutableStateFlow(true)
    val isContentVisible = _isContentVisible.asStateFlow()

    private var _isEmptyTvVisible = MutableStateFlow(false)
    val isEmptyTvVisible = _isEmptyTvVisible.asStateFlow()

    private var _content = MutableStateFlow<List<CurrencyTransactionModel>?>(listOf())
    val content = _content.asStateFlow()

    init{
        getContent()
    }

    private fun getContent(){
        _isContentVisible.value = false
        _isProgressBarVisible.value = true
        viewModelScope.launch {
            try {
                val repository = CurrencyRepository()
                if (repository.getTransactionList() != null){
                    _content.value = repository.getTransactionList()
                    _isContentVisible.value = true
                } else
                    _isEmptyTvVisible.value = true

            } catch (e: Exception){

            }
        }
        _isProgressBarVisible.value = false


    }
}