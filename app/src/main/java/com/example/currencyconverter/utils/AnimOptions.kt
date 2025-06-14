package com.example.currencyconverter.utils

import androidx.navigation.NavOptions



fun getNavOptionsWithoutBactrack(destinationId: Int): NavOptions{
    return NavOptions.Builder()
        .setPopUpTo(destinationId, true)
        .build()
}