package com.example.currencyconverter.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemTransactionListBinding

import com.example.currencyconverter.domain.model.CurrencyTransactionModel
import com.example.currencyconverter.utils.TransactionAdapterDiffCallback
import com.example.currencyconverter.utils.formatDateTime

class TransactionListAdapter(): ListAdapter<CurrencyTransactionModel, TransactionListAdapter.TransactionListViewHolder>(
    TransactionAdapterDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        return TransactionListViewHolder(
            ItemTransactionListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionListViewHolder(
        private val binding: ItemTransactionListBinding
    ): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(model: CurrencyTransactionModel){
            with(binding) {
                tvTime.text = formatDateTime(model.time)
                tvBuyName.text = model.buyName
                tvBuyFullName.text = model.buyFullName
                tvBuyValue.text = "Amount: ${model.buySymbol} ${model.buyValue}"
                ivBuyFlag.setImageResource(model.buyFlag)
                tvSellName.text = model.sellName
                tvSellFullName.text = model.sellFullName
                tvSellValue.text = "Amount: ${model.sellSymbol} ${model.sellValue}"
                ivSellFlag.setImageResource(model.sellFlag)
            }
        }
    }




}