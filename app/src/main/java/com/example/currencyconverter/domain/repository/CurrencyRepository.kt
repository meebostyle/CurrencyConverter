package com.example.currencyconverter.domain.repository


import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.domain.model.CurrencyTransactionModel
import com.example.currencyconverter.domain.model.getAccountDbo
import com.example.currencyconverter.domain.model.getTransactionDbo
import com.example.currencyconverter.domain.model.toCurrencyModel
import com.example.currencyconverter.domain.model.toCurrencyTransactionModel
import com.example.currencyconverter.utils.toFormatDouble


class CurrencyRepository()  {

    private val ratesService: RatesService = RemoteRatesServiceImpl()

    suspend fun getCurrencyListMode(baseCurrencyCode: String, amount: Double): List<CurrencyModel>{
        val response = ratesService.getRates(baseCurrencyCode, amount)
        val database = ConverterDatabase.db
        val accounts = database.accountDao().getAll().associateBy { it.code }
        return response.map {
            it.toCurrencyModel(it, accounts)
        }
    }

    suspend fun getCurrencyChangeMode(baseCurrencyCode: String, amount: Double): List<CurrencyModel>{
        val response = ratesService.getRates(baseCurrencyCode, amount)
        val database = ConverterDatabase.db
        val accounts = database.accountDao().getAll().associateBy { it.code }
        val result = listOf<RateDto>(response[0]) + response.filter{accounts.contains(it.currency)}.filter{
            accounts[it.currency]!!.amount >= it.value && accounts[it.currency]!!.code != response[0].currency}

        return result.map {
            it.toCurrencyModel(it, accounts)
        }
    }

    suspend fun saveTransaction (buyItem: CurrencyModel, sellItem: CurrencyModel){
        val database = ConverterDatabase.db
        val inputTransaction = getTransactionDbo(buyItem, sellItem)
        var boughtBalance = if (buyItem.balance == null) 0.0 else toFormatDouble(buyItem.balance)
        var soldBalance = if (sellItem.balance == null) 0.0 else toFormatDouble(sellItem.balance)
        boughtBalance += toFormatDouble(buyItem.value)
        soldBalance -= toFormatDouble(sellItem.value)
        val boughtAcc = getAccountDbo(buyItem.name,boughtBalance)
        val soldAcc = getAccountDbo(sellItem.name, soldBalance)
        database.transactionDao().insertAll(inputTransaction)
        database.accountDao().insertAll(boughtAcc)
        if (soldBalance > 0.0)
            database.accountDao().insertAll(soldAcc)
        else
            database.accountDao().deleteLowBalance(soldAcc)
    }

    suspend fun getTransactionList(): List<CurrencyTransactionModel>?{
        val transactions = ConverterDatabase.db.transactionDao().getAll()
        return if (transactions.isNotEmpty()){
            transactions.map {
                it.toCurrencyTransactionModel()
            }.reversed()
        } else null
    }
}
