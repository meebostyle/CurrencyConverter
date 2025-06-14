package com.example.currencyconverter.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.domain.model.CurrencyTransactionModel

class CurrencyAdapterDiffCallback: DiffUtil.ItemCallback<CurrencyModel>(){
    override fun areItemsTheSame(
        oldItem: CurrencyModel,
        newItem: CurrencyModel
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: CurrencyModel,
        newItem: CurrencyModel
    ): Boolean {
        return oldItem == newItem
    }


}
class TransactionAdapterDiffCallback: DiffUtil.ItemCallback<CurrencyTransactionModel>(){
    override fun areItemsTheSame(
        oldItem: CurrencyTransactionModel,
        newItem: CurrencyTransactionModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: CurrencyTransactionModel,
        newItem: CurrencyTransactionModel
    ): Boolean {
        return oldItem == newItem
    }


}
