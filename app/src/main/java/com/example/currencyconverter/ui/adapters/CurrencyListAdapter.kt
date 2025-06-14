package com.example.currencyconverter.ui.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemCurrencyListBinding
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.utils.EditTextWatcher

class CurrencyListAdapter(
    val onItemClick: (name: String, value: Double ) -> Unit,
    val onChangeMode: (name: String, value: Double) -> Unit,
): ListAdapter<CurrencyModel, CurrencyListAdapter.CurrencyListViewHolder>(
    DiffCallback()
) {
    private var textWatcher = EditTextWatcher()



    private var enteredValue = 1.0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        return CurrencyListViewHolder(
            ItemCurrencyListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }


    inner class CurrencyListViewHolder(
        private val binding: ItemCurrencyListBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(currency: CurrencyModel, position: Int){

            with(binding) {
                tvName.text = currency.name
                tvFullName.text = currency.fullName
                ivFlag.setImageResource(currency.flag)
                tvBalance.text = "Balance: ${currency.symbol} ${currency.balance}"
                tvBalance.isVisible = currency.balance != null

                if (position == 0){
                    tvValue.isVisible = false
                    etValue.isVisible = true
                    etValue.setText("${currency.symbol} ${currency.value}")
                }
                else{
                    tvValue.isVisible = true
                    etValue.isVisible = false
                    tvValue.text = "${currency.symbol} ${currency.value}"
                }

                with(etValue) {

                    onFocusChangeListener = OnFocusChangeListener{view, hasFocus ->
                        if (hasFocus) {
                            tvRemove.isVisible = true
                            addTextChangedListener(textWatcher)
                        } else {
                            removeTextChangedListener(textWatcher)
                            tvRemove.isVisible = false
                            if (toFormatDouble(text?.toString()) != 1.0){
                                enteredValue = toFormatDouble(text.toString())
                                onChangeMode(currency.name, enteredValue)
                            }
                        }
                    }
                }

                container.setOnClickListener {
                    if (enteredValue == 1.0 && position != 0){
                        onItemClick(currency.name, 1.0)
                    }
                    etValue.clearFocus()


                }

                tvRemove.setOnClickListener {
                    etValue.removeTextChangedListener(textWatcher)
                    etValue.setText("${currency.symbol} ${currency.value}")
                    enteredValue = 1.0
                    etValue.clearFocus()
                    onItemClick(currency.name, enteredValue)
                }


            }
        }
    }
    fun Double.toDecimalNotationString() =
        String.format("%.99f", this)
            .trimEnd('0')

    fun toFormatDouble (value: String?): Double{
        if (value == null) return 1.0
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