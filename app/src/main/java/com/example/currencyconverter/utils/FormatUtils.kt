package com.example.currencyconverter.utils

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDouble(number: Double): String {
    var str = "%.15f".format(number).replace(",", ".")

    str = str.replaceFirst("^0+(?!\\.)".toRegex(), "")
    if (str.startsWith(".")) str = "0$str"

    if (!str.startsWith("0.")) {
        val rounded = "%.2f".format(number).replace(",", ".")
        str = rounded
            .trimEnd('0')
            .trimEnd('.')
            .replaceFirst("^0+(?!\\.)", "")
    }

    if (str.startsWith("0.")) {
        val afterDecimal = str.substring(2)
        val firstNonZeroIndex = afterDecimal.indexOfFirst { it != '0' }

        if (firstNonZeroIndex == -1) {
            return "0"
        } else {
            val significantDigits = afterDecimal
                .substring(firstNonZeroIndex)
                .take(2)

            val leadingZeros = afterDecimal.take(firstNonZeroIndex)
            str = "0.$leadingZeros$significantDigits"
        }
    }

    if (str.contains(".")) {
        val parts = str.split(".")
        if (parts[1].take(3).all { it == '0' } && parts[1].length >= 3) {
            str = parts[0]
        }
    }

    if (str.contains(".")) {
        str = str.trimEnd('0').trimEnd('.')
    }

    return str.ifEmpty { "0" }
}
fun toFormatDouble (value: String?): Double{
    if (value == null) return 1.0
    return value.replace(',', '.')
        .replace(Regex("[^0-9.-]"), "").toDouble()
}

fun formatDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return dateTime.format(formatter)
}

@SuppressLint("DefaultLocale")
fun Double.toDecimalNotationString() =
    String.format("%.99f", this)
        .trimEnd('0').replace(".","")