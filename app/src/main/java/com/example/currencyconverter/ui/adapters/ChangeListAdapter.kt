package com.example.currencyconverter.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemCurrencyListBinding
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.utils.CurrencyAdapterDiffCallback

class ChangeListAdapter(): ListAdapter<CurrencyModel, ChangeListAdapter.ChangeListViewHolder>(
    CurrencyAdapterDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeListViewHolder {
        return ChangeListViewHolder(
            ItemCurrencyListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChangeListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }


    inner class ChangeListViewHolder(
        private val binding: ItemCurrencyListBinding
    ): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(currency: CurrencyModel, position: Int){

            with(binding) {
                tvName.text = currency.name
                tvFullName.text = currency.fullName
                ivFlag.setImageResource(currency.flag)
                tvBalance.text = "Balance: ${currency.symbol} ${currency.balance}"
                tvBalance.isVisible = currency.balance != null
                tvRemove.isVisible = false
                etValue.isVisible = false
                if (position == 0)
                    tvValue.text = "+ ${currency.symbol} ${currency.value}"
                else
                    tvValue.text = "- ${currency.symbol} ${currency.value}"
            }
        }
    }




}
