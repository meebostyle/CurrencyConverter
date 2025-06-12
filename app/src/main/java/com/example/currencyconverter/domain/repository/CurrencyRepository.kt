package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.room.account.AccountDb
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.ui.model.CurrencyModel
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.round


class CurrencyRepository()  {

    private val ratesService: RatesService = RemoteRatesServiceImpl()

    suspend fun getCurrencyList(baseCurrencyCode: String,
                                amount: Double): List<CurrencyModel>{

        val response = ratesService.getRates(baseCurrencyCode,
            amount)

        val accountDb = AccountDb.db

        val result = response.map {
            CurrencyModel(
                enumValueOf<Currency>(it.currency).flag,
                it.currency,
                enumValueOf<Currency>(it.currency).fullName,
                enumValueOf<Currency>(it.currency).symbol,
                (if (accountDb.accountDao.getBalance(it.currency) != null)
                        "Balance: ${enumValueOf<Currency>(it.currency).symbol} ${myRound(accountDb.accountDao.getBalance(it.currency)!!)}"
                        else {"0"}),
                myRound(it.value),
            )
        }


        return result
    }


}

private fun myRound (value: Double): String{
    var count = 0
    var preResult = value % 1
    var int = value - preResult
    if (value<1){
        while (preResult < 10.0 && count < 8) {
            preResult = 10 * preResult
            count++
        }
        if (count == 8){
            count = 0
        }
        var newResult = divideAndFormat (round(preResult) , 10.0.pow(count.toDouble()))
        println(newResult)
        newResult = if(int!=0.0){ "${int.toInt()}$newResult"} else {
            newResult
        }

        var result = newResult.toString()
        while ((result.last() == '0' && result.contains('.')) || result.last() == '.'){
            result = result.dropLast(1)

        }
        return result.replace('.', ',')

    }
    var result = "%.2f".format(value).replace("\\.?0+$".toRegex(), "")
    result = result.replace(',', '.')
    while ((result.last() == '0' && result.contains('.')) || result.last() == '.'){
        result = result.dropLast(1)
    }

    return result.replace('.', ',')

}
fun divideAndFormat(a: Double, b: Double, scale: Int = 8): String {
    val result = BigDecimal(a.toString())
        .divide(BigDecimal(b.toString()), scale, RoundingMode.HALF_UP)
       return result.toString()
}