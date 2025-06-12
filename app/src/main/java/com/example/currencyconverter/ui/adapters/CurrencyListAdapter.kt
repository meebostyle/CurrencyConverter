package com.example.currencyconverter.ui.adapters


import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ItemCurrencyListBinding
import com.example.currencyconverter.ui.model.CurrencyModel

class CurrencyListAdapter(
    val onItemClick: (name: String, value: Double ) -> Unit,
): ListAdapter<CurrencyModel, CurrencyListAdapter.CurrencyListViewHolder>(
    DiffCallback()
) {

    private  var focusedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        return CurrencyListViewHolder(
            ItemCurrencyListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(getItem(position), position)

        holder.itemView.isClickable = focusedPosition == -1 || focusedPosition == position
        holder.itemView.isFocusable = focusedPosition == -1 || focusedPosition == position
    }

    inner class CurrencyListViewHolder(
        private val binding: ItemCurrencyListBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(currency: CurrencyModel, position: Int){
            with(binding) {
                tvName.text = currency.name
                tvValue.setText(currency.value)
                tvFullName.text = currency.fullName
                tvSymbol.text = currency.symbol
                ivFlag.setImageResource(currency.flag)
                if (currency.balance == "0")
                    tvBalance.isVisible = false
                        else{
                    tvBalance.isVisible = true
                    tvBalance.text = currency.balance
                }

                if (position != 0) {
                    tvValue.apply {
                        isEnabled = false
                        isFocusable = false
                        isClickable = false
                        background = null
                    }
                    tvRemove.isVisible = false
                } else {
                    tvValue.apply {
                        isEnabled = true
                        isFocusableInTouchMode = true
                        isClickable = true
                        background = ContextCompat.getDrawable(context, androidx.appcompat.R.drawable.abc_edit_text_material)
                    }
                    tvValue.onFocusChangeListener = OnFocusChangeListener{view, hasFocus ->
                        if (hasFocus) {
                            tvRemove.isVisible = true
                            focusedPosition = position
                        } else {
                            focusedPosition = -1
                            container.setOnClickListener {
                                tvValue.clearFocus()
                            }
                            tvRemove.isVisible = false
                        }
                    }

                }
                tvRemove.setOnClickListener {
                    tvValue.setText("1")
                    tvValue.clearFocus()
                }
                container.setOnClickListener {
                    tvValue.clearFocus()
                    if (position != 0)
                    onItemClick(currency.name, 1.0)
                }


            }
        }
    }

    fun toDouble (value: String): Double {
        return value.replace(',', '.')
            .replace(Regex("[^0-9.-]"), "").toDouble()
    }

}
class DiffCallback: DiffUtil.ItemCallback<CurrencyModel>(){
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