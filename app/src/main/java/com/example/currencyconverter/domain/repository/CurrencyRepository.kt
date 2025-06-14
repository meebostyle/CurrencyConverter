package com.example.currencyconverter.domain.repository

import android.util.Log
import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.AccountDb
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.domain.model.toCurrencyModel



class CurrencyRepository()  {

    private val ratesService: RatesService = RemoteRatesServiceImpl()

    suspend fun getCurrencyListMode(baseCurrencyCode: String, amount: Double): List<CurrencyModel>{
        val response = ratesService.getRates(baseCurrencyCode, amount)
        val accountDb = AccountDb.db
        val accounts = accountDb.accountDao.getAll().associateBy { it.code }
        return response.map {
            it.toCurrencyModel(it, accounts)
        }
    }

    suspend fun getCurrencyChangeMode(baseCurrencyCode: String, amount: Double): List<CurrencyModel>{
        val response = ratesService.getRates(baseCurrencyCode, amount)
        val accountDb = AccountDb.db
        val accounts = accountDb.accountDao.getAll().associateBy { it.code }
        val result = listOf<RateDto>(response[0]) + response.filter{accounts.contains(it.currency)}.filter{
            accounts[it.currency]!!.amount >= it.value }

        return result.map {
            it.toCurrencyModel(it, accounts)
        }
    }
}
