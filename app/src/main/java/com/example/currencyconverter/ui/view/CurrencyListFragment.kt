package com.example.currencyconverter.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.currencyconverter.databinding.FragmentCurrencyListBinding
import com.example.currencyconverter.ui.adapters.CurrencyListAdapter
import com.example.currencyconverter.ui.viewmodel.CurrencyListViewModel
import kotlinx.coroutines.launch

class CurrencyListFragment: Fragment() {

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrencyListViewModel by viewModels<CurrencyListViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CurrencyListAdapter(
            onItemClick = {name, value ->
                viewModel.getContent(name, value)
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            if (viewModel.content.value == null) viewModel.getContent()
        }



        with(binding) {
            recyclerHolder.adapter = adapter
            viewLifecycleOwner.lifecycleScope.launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isContentVisible.collect {
                        recyclerHolder.isVisible = it
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
                            adapter.submitList(it) {
                                viewModel.onListUpdated()
                                recyclerHolder.smoothScrollToPosition(0)
                        }
                    }
                }
            }



        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}