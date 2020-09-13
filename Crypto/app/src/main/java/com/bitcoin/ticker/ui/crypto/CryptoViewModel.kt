package com.bitcoin.ticker.ui.crypto

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bitcoin.ticker.data.model.asset.PayLoadAssetResponce
import com.bitcoin.ticker.data.rest.CryptoRepository
import com.bitcoin.ticker.util.sealed.Resource
import com.imobilecode.ekotaksi.rider.common.helper.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CryptoViewModel @ViewModelInject constructor(private val cryptoRepository: CryptoRepository) : ViewModel() {

    private val _bitcoinDataLiveData = SingleLiveEvent<Resource<PayLoadAssetResponce>>()
    val bitcoinDataLiveData: LiveData<Resource<PayLoadAssetResponce>>
        get() = _bitcoinDataLiveData

    fun getAllAssetList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val data= cryptoRepository.getAllAssetList()
            _bitcoinDataLiveData.postValue(data)

        } catch (e: Exception) {
            _bitcoinDataLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }

}