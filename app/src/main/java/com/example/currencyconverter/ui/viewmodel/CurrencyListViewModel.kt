package com.example.currencyconverter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.repository.CurrencyRepository
import com.example.currencyconverter.domain.model.CurrencyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyListViewModel: ViewModel() {


    private var _isProgressBarVisible = MutableStateFlow<Boolean>(true)
    val isProgressBarVisible = _isProgressBarVisible.asStateFlow()


    private var _isContentVisible = MutableStateFlow<Boolean>(false)
    val isContentVisible = _isContentVisible.asStateFlow()

    private var _content = MutableStateFlow<List<CurrencyModel>?> (null)
    val content = _content.asStateFlow()

    private var _needToScroll = true
    val needToScroll get() = _needToScroll

    private lateinit var activeJob: Job


    private var mode = (Mode.LIST)

    private var baseCurrencyCode = "USD"

    private var _amount = 1.0
    val amount = _amount

    init {
        _isProgressBarVisible.value = true
        _isContentVisible.value = false
        viewModelScope.launch(Dispatchers.IO) {
            loadContentListMode()
        }
        updateContentListMode()
    }

    fun newInit(){
        _isProgressBarVisible.value = true
        _isContentVisible.value = false
        viewModelScope.launch(Dispatchers.IO) {
            loadContentListMode()
        }
        updateContentListMode()
    }

    fun reset() {
        _isProgressBarVisible.value = true
        _isContentVisible.value = false
        _content.value = null
        _needToScroll = true
        baseCurrencyCode = "USD"
        _amount = 1.0
        mode = Mode.LIST
        activeJob.cancel()
    }

    fun setDataListMode(baseCurrencyCode: String, amount: Double){
        mode = Mode.LIST
        activeJob.cancel()
        _amount = amount
        this.baseCurrencyCode = baseCurrencyCode
        _needToScroll = true
        _isProgressBarVisible.value = true
        _isContentVisible.value = false
        viewModelScope.launch(Dispatchers.IO) {
            loadContentListMode()
        }

    }

    fun setDataChangeMode(baseCurrencyCode: String, amount: Double){
        mode = Mode.CHANGE
        activeJob.cancel()
        _amount = amount
        this.baseCurrencyCode = baseCurrencyCode
        _needToScroll = true
        _isProgressBarVisible.value = true
        _isContentVisible.value = false
        viewModelScope.launch(Dispatchers.IO) {
            loadContentChangeMode()
        }
    }

    fun onListUpdated(){

        _needToScroll = false
        when (mode){
            Mode.LIST -> updateContentListMode()
            Mode.CHANGE -> updateContentChangeMode()
        }
    }


    private fun updateContentChangeMode(){

        activeJob = viewModelScope.launch(Dispatchers.IO) {
            _needToScroll = false
            while (mode == Mode.CHANGE){
                loadContentChangeMode()
                delay(DELAY)
            }
        }
    }


    private fun updateContentListMode() {
        activeJob = viewModelScope.launch(Dispatchers.IO) {
            _needToScroll = false
            while (mode == Mode.LIST){
                loadContentListMode()
                delay(DELAY)
            }
        }
    }

    private suspend fun loadContentChangeMode(){
        val repository = CurrencyRepository()
            try {
                _content.value = repository.getCurrencyChangeMode(baseCurrencyCode, _amount)
                _isProgressBarVisible.value = false
                _isContentVisible.value = true
            } catch (e: Exception){
                Log.e("Response Error", "$e")
            }
    }



    private suspend fun loadContentListMode(){
        val repository = CurrencyRepository()
            try {
                _content.value = repository.getCurrencyListMode(baseCurrencyCode, _amount)
                _isProgressBarVisible.value = false
                _isContentVisible.value = true
            } catch (e: Exception){
                Log.e("Response Error", "$e")
            }
    }



    private companion object{
        private const val DELAY = 1000L
    }
    enum class Mode{LIST, CHANGE}
}