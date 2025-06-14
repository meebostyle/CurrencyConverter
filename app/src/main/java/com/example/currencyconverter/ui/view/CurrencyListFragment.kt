package com.example.currencyconverter.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentCurrencyListBinding
import com.example.currencyconverter.ui.adapters.CurrencyListAdapter
import com.example.currencyconverter.ui.base.BaseFragment
import com.example.currencyconverter.ui.viewmodel.CurrencyListViewModel
import com.example.currencyconverter.utils.getDefaultNavOptions
import kotlinx.coroutines.launch

class CurrencyListFragment: BaseFragment<FragmentCurrencyListBinding>() {



    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCurrencyListBinding {
        return FragmentCurrencyListBinding.inflate(inflater, container, false)
    }

    private val viewModel: CurrencyListViewModel by viewModels<CurrencyListViewModel>()


    override fun configureView() {
        val navController = findNavController()
        super.configureView()
        val adapter = CurrencyListAdapter(
            onItemClick = {name, value ->
                viewModel.setDataListMode(name, value)
            },
            onChangeMode = {
                    name, value ->
                viewModel.setDataChangeMode(name, value)
            },
            transaction = { position ->
                val bundle = Bundle().apply {
                    putParcelable("item1", viewModel.content.value!![0])
                    putParcelable("item2", viewModel.content.value!![position])
                }
                navController.navigate(R.id.currency_change_fragment, bundle,
                    getDefaultNavOptions()
                )
            }
        )


        with(binding) {


            recyclerHolder.adapter = adapter


            btnTransaction.setOnClickListener {
                navController.navigate(R.id.transaction_list_fragment)
            }


            viewLifecycleOwner.lifecycleScope.launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isContentVisible.collect {
                        recyclerHolder.isVisible = it
                        btnTransaction.isVisible = it
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isProgressBarVisible.collect {
                        pb.isVisible = it
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.content.collect {
                        adapter.submitList(it){
                            if (viewModel.needToScroll){
                                recyclerHolder.scrollToPosition(0)
                                viewModel.onListUpdated()
                            }
                        }
                    }
                }
            }


        }




    }

}