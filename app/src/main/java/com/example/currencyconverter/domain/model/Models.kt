package com.example.currencyconverter.domain.model

import android.os.Parcelable
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.utils.formatDouble
import com.example.currencyconverter.utils.toFormatDouble
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class CurrencyModel(
    val flag: Int,
    val name: String,
    val fullName: String,
    val symbol: String,
    val balance: String?,
    val value: String,
): Parcelable

data class CurrencyTransactionModel(
    val buyFlag: Int,
    val buyName: String,
    val buyFullName: String,
    val buySymbol: String,
    val buyValue: String,

    val sellFlag: Int,
    val sellName: String,
    val sellFullName: String,
    val sellSymbol: String,
    val sellValue: String,

    val time: LocalDateTime
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
        balance = balance?.let {formatDouble(it) },
        value = formatDouble(rateDto.value),
    )
    return currencyModel
}


fun getTransactionDbo(buyItem: CurrencyModel, sellItem: CurrencyModel): TransactionDbo = TransactionDbo(
        id = 0,
        from = sellItem.name,
        to = buyItem.name,
        fromAmount = toFormatDouble(sellItem.value),
        toAmount = toFormatDouble(buyItem.value),
        dateTime = LocalDateTime.now()
    )


fun getAccountDbo(code: String, amount: Double): AccountDbo =
    AccountDbo(
        code = code,
        amount = amount
    )


fun TransactionDbo.toCurrencyTransactionModel(): CurrencyTransactionModel{
    val sellCurrency = enumValueOf<Currency>(from)
    val buyCurrency = enumValueOf<Currency>(to)
    return CurrencyTransactionModel(
        buyFlag = buyCurrency.flag,
        buyName = to,
        buyFullName = buyCurrency.fullName,
        buySymbol = buyCurrency.symbol,
        buyValue = formatDouble(toAmount),
        sellFlag = sellCurrency.flag,
        sellName = from,
        sellFullName = sellCurrency.fullName,
        sellSymbol = sellCurrency.symbol,
        sellValue = formatDouble(fromAmount),
        time = dateTime
    )
}







