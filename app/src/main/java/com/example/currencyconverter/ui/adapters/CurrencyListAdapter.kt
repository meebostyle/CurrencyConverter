package com.example.currencyconverter.ui.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemCurrencyListBinding
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.utils.CurrencyAdapterDiffCallback
import com.example.currencyconverter.utils.EditTextWatcher
import com.example.currencyconverter.utils.formatDouble
import com.example.currencyconverter.utils.toFormatDouble

class CurrencyListAdapter(
    val onItemClick: (name: String, value: Double ) -> Unit,
    val onChangeMode: (name: String, value: Double) -> Unit,
    val transaction: (position: Int) -> Unit,
    var enteredValue: Double
): ListAdapter<CurrencyModel, CurrencyListAdapter.CurrencyListViewHolder>(
    CurrencyAdapterDiffCallback()
) {
    private var textWatcher = EditTextWatcher()
    private var isChanged = false
    private var canLoad = false


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
                            canLoad = true
                        }
                        false
                    }
                    setOnEditorActionListener { view, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                            enteredValue = toFormatDouble(text.toString())
                            onChangeMode(currency.name, enteredValue)
                            view.clearFocus()
                            true
                        } else {
                            false
                        }
                    }

                    setOnFocusChangeListener { view, hasFocus ->
                        if (!hasFocus) {
                            removeTextChangedListener(textWatcher)
                            tvRemove.isVisible = false
                            if (formatDouble(toFormatDouble(text?.toString())) == "0") {
                                etValue.setText("1")
                            }
                        }
                    }
                }

                container.setOnClickListener {

                    if (!canLoad){
                        onItemClick(currency.name, 1.0)
                    }
                    etValue.clearFocus()
                    if (position != 0 && canLoad){
                        transaction(position)
                    }
                }

                tvRemove.setOnClickListener {
                    canLoad = false
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
