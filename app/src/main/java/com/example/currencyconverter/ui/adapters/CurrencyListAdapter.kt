package com.example.currencyconverter.ui.adapters


import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemCurrencyListBinding
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.utils.CurrencyAdapterDiffCallback
import com.example.currencyconverter.utils.EditTextWatcher
import com.example.currencyconverter.utils.toDecimalNotationString
import com.example.currencyconverter.utils.toFormatDouble

class CurrencyListAdapter(
    val onItemClick: (name: String, value: Double ) -> Unit,
    val onChangeMode: (name: String, value: Double) -> Unit,
    val transaction: (position: Int) -> Unit,
): ListAdapter<CurrencyModel, CurrencyListAdapter.CurrencyListViewHolder>(
    CurrencyAdapterDiffCallback()
) {
    private var textWatcher = EditTextWatcher()
    private var enteredValue = 1.0
    private var isChanged = false


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

        @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
        fun bind(currency: CurrencyModel, position: Int){

            with(binding) {
                tvName.text = currency.name
                tvFullName.text = currency.fullName
                ivFlag.setImageResource(currency.flag)
                tvBalance.text = "Balance: ${currency.symbol} ${currency.balance}"
                tvBalance.isVisible = currency.balance != null
                tvRemove.isVisible = false
                etValue.clearFocus()
                if (position == 0){
                    tvValue.text = currency.symbol
                    tvValue.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_NONE)
                    tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    etValue.isVisible = true
                    etValue.setText(currency.value)
                }
                else{
                    etValue.isVisible = false
                    tvValue.text = "${currency.symbol} ${currency.value}"
                }

                with(etValue) {
                    setOnTouchListener { view, event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            (view as EditText).setSelection(view.text.length)
                            tvRemove.isVisible = true
                            addTextChangedListener(textWatcher)
                            isChanged = true
                        }
                        false
                    }

                    setOnFocusChangeListener { view, hasFocus ->
                        if (!hasFocus) {
                            removeTextChangedListener(textWatcher)
                            tvRemove.isVisible = false
                            if (toFormatDouble(text?.toString()).toDecimalNotationString() != "1" && isChanged) {
                                if (toFormatDouble(text?.toString()).toDecimalNotationString() == "0") {
                                    etValue.setText("1")
                                }
                                enteredValue = toFormatDouble(text.toString())
                                onChangeMode(currency.name, enteredValue)
                            } else {
                                etValue.setText("1")
                            }
                        }
                    }
                }

                container.setOnClickListener {
                    if (enteredValue == 1.0 && position != 0){
                        isChanged = false
                        onItemClick(currency.name, 1.0)
                    }
                    etValue.clearFocus()
                    if (position != 0 && enteredValue != 1.0){
                        transaction(position)
                    }
                }

                tvRemove.setOnClickListener {
                    isChanged == false
                    etValue.removeTextChangedListener(textWatcher)
                    etValue.setText("1")
                    enteredValue = 1.0
                    etValue.clearFocus()
                    onItemClick(currency.name, 1.0)
                }
            }
        }
    }





}
