package com.example.currencyconverter.utils

import androidx.navigation.NavOptions
import com.example.currencyconverter.R

fun getDefaultNavOptions(): NavOptions{
    return NavOptions.Builder()
        .setEnterAnim(R.anim.anim_fragment_enter)
        .setExitAnim(R.anim.fragment_exit)
        .setPopEnterAnim(R.anim.fragment_pop_enter)
        .setPopExitAnim(R.anim.fragment_pop_exit)
        .build()
}

fun getNavOptionsWithoutBactrack(destinationId: Int): NavOptions{
    return NavOptions.Builder()
        .setEnterAnim(R.anim.anim_fragment_enter)
        .setExitAnim(R.anim.fragment_exit)
        .setPopEnterAnim(R.anim.fragment_pop_enter)
        .setPopExitAnim(R.anim.fragment_pop_exit)
        .setPopUpTo(destinationId, true)
        .build()
}