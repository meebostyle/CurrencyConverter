package com.example.currencyconverter.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.currencyconverter.databinding.FragmentTransactionListBinding
import com.example.currencyconverter.ui.adapters.TransactionListAdapter
import com.example.currencyconverter.ui.base.BaseFragment
import com.example.currencyconverter.ui.viewmodel.TransactionListViewModel
import kotlinx.coroutines.launch

class TransactionListFragment: BaseFragment<FragmentTransactionListBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTransactionListBinding {
        return FragmentTransactionListBinding.inflate(inflater, container, false)
    }
    private val viewModel: TransactionListViewModel by viewModels<TransactionListViewModel>()

    override fun configureView() {
        super.configureView()
        val adapter = TransactionListAdapter()

        with(binding) {
            recyclerHolder.adapter = adapter


            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.content.collect {
                        adapter.submitList(it)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isContentVisible.collect {
                        recyclerHolder.isVisible = it
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isEmptyTvVisible.collect {
                        tvEmpty.isVisible = it
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



        }


    }

}