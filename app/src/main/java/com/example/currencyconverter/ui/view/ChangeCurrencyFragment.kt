package com.example.currencyconverter.ui.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentChangeCurrencyBinding
import com.example.currencyconverter.domain.model.CurrencyModel
import com.example.currencyconverter.ui.adapters.ChangeListAdapter
import com.example.currencyconverter.ui.base.BaseFragment
import com.example.currencyconverter.ui.viewmodel.ChangeCurrencyViewModel
import com.example.currencyconverter.utils.formatDouble
import com.example.currencyconverter.utils.getNavOptionsWithoutBactrack
import com.example.currencyconverter.utils.toFormatDouble
import kotlinx.coroutines.launch

class ChangeCurrencyFragment: BaseFragment<FragmentChangeCurrencyBinding>() {

    private val viewModel: ChangeCurrencyViewModel by viewModels<ChangeCurrencyViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangeCurrencyBinding {
        return FragmentChangeCurrencyBinding.inflate(inflater, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun configureView() {
        super.configureView()

        val adapter = ChangeListAdapter()
        val navController = findNavController()

        val argumentsList = listOf<CurrencyModel>(arguments?.getParcelable<CurrencyModel>("item1")!!, arguments?.getParcelable<CurrencyModel>("item2")!!)
        val buyItem = argumentsList[0]
        val sellItem = argumentsList[1]

        with(binding) {
            recyclerHolder.adapter = adapter
            adapter.submitList(argumentsList)
            tvNameTransaction.text = "${sellItem.fullName} to ${buyItem.fullName}"
            tvActualCourse.text = "${buyItem.symbol} 1 = ${sellItem.symbol} ${formatDouble(toFormatDouble(sellItem.value)/ toFormatDouble(buyItem.value))}"
            btnTransaction.text = "Buy ${buyItem.fullName} for ${sellItem.fullName}"

            btnTransaction.setOnClickListener {
                viewModel.saveTransaction(buyItem, sellItem)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isContentVisible.collect {
                        recyclerHolder.isVisible = it
                        tvActualCourse.isVisible = it
                        tvNameTransaction.isVisible = it
                        btnTransaction.isVisible = it
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isProgressBarVisible.collect {
                        pb.isVisible = it
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.readyToBack.collect {
                        if (it)
                            navController.navigate(R.id.currency_list_fragment, null,
                                getNavOptionsWithoutBactrack(R.id.currency_change_fragment)
                            )
                    }
                }
            }

        }


    }


}