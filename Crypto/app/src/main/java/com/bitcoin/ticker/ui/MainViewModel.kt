package com.bitcoin.ticker.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitcoin.ticker.data.model.detail.PayLoadDetail
import com.bitcoin.ticker.data.rest.CryptoRepository
import com.bitcoin.ticker.util.sealed.Resource
import com.imobilecode.ekotaksi.rider.common.helper.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val cryptoRepository: CryptoRepository) : ViewModel() {

    private val _cryptoDetailDataLiveData = SingleLiveEvent<Resource<PayLoadDetail>>()
    val cryptoDetailDataLiveData: LiveData<Resource<PayLoadDetail>>
        get() = _cryptoDetailDataLiveData



}