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
        if (isFormatting) return

        isFormatting = true

        try {
            val input = editable.toString()

            when {
                input.isEmpty() -> editable?.replace(0, editable.length, "0.")
                input == "0" -> editable?.replace(0, editable.length, "1")
                input == "0." -> editable?.replace(0, editable.length, "0.")
                input == "." -> editable?.replace(0, editable.length, "0.")
                input.startsWith(".") -> editable?.replace(0, editable.length, "0$input")
                input.startsWith("0") && !input.contains(".") -> {
                    val number = input.trimStart('0')
                    editable?.replace(0, editable.length, if (number.isEmpty()) "1" else number)
                }
                input.startsWith("0") && input.contains(".") -> {
                    val parts = input.split(".")
                    val integerPart = parts[0].trimStart('0')
                    val result = if (integerPart.isEmpty()) "0.${parts[1]}" else "$integerPart.${parts[1]}"
                    editable?.replace(0, editable.length, result)
                }
                input.count { it == '.' } > 1 -> {
                    val firstDotIndex = input.indexOf('.')
                    val filtered = input.substring(0, firstDotIndex + 1) +
                            input.substring(firstDotIndex + 1).replace(".", "")
                    editable?.replace(0, editable.length, filtered)
                }
            }
        } finally {
            isFormatting = false
        }
    }
}