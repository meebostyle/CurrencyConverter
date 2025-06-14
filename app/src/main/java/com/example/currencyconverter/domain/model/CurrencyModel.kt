package com.example.currencyconverter.domain.model

import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Currency
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.round

data class CurrencyModel(
    val flag: Int,
    val name: String,
    val fullName: String,
    val symbol: String,
    val balance: String?,
    val value: String,
)

fun RateDto.toCurrencyModel(rateDto: RateDto, accounts: Map<String, AccountDbo>): CurrencyModel {

    val currency = enumValueOf<Currency>(rateDto.currency)
    val balance = accounts[rateDto.currency]?.amount
        ?: null
    val currencyModel = CurrencyModel(
        flag = currency.flag,
        name = rateDto.currency,
        fullName = currency.fullName,
        symbol = currency.symbol,
        balance = balance?.let { myRound(it) },
        value = myRound(rateDto.value),
    )
    return currencyModel
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
