package com.example.currencyconverter.utils

import android.text.Editable
import android.text.TextWatcher

class EditTextWatcher : TextWatcher {
    private var isFormatting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        if (isFormatting || editable == null) return

        isFormatting = true

        try {
            val input = editable.toString()

            when {
                input.isEmpty() -> {
                    editable.replace(0, editable.length, "0.")
                }
                input == "0" -> {
                    // Не изменяем, разрешаем вводить числа после 0
                }
                input == "." -> {
                    editable.replace(0, editable.length, "0.")
                }
                input.startsWith(".") -> {
                    editable.replace(0, editable.length, "0$input")
                }
                input.count { it == '.' } > 1 -> {
                    // Оставляем только первую точку
                    val firstDotIndex = input.indexOf('.')
                    val filtered = input.substring(0, firstDotIndex + 1) +
                            input.substring(firstDotIndex + 1).replace(".", "")
                    editable.replace(0, editable.length, filtered)
                }
                input.contains(".") -> {
                    // Проверяем количество знаков после запятой
                    val parts = input.split(".")
                    if (parts.size > 1 && parts[1].length > 2) {
                        val limited = "${parts[0]}.${parts[1].substring(0, 2)}"
                        editable.replace(0, editable.length, limited)
                    }
                }
                input.startsWith("0") && input.length > 1 -> {
                    // Разрешаем числа, начинающиеся с 0, только если это 0. или 0.x
                    if (!input.contains(".")) {
                        editable.replace(0, editable.length, input.trimStart('0').ifEmpty { "0" })
                    }
                }
            }
        } finally {
            isFormatting = false
        }
    }
}