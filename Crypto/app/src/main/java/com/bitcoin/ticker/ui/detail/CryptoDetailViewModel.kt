package com.bitcoin.ticker.ui.detail

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

class CryptoDetailViewModel @ViewModelInject constructor(private val cryptoRepository: CryptoRepository) : ViewModel() {

    private val _cryptoDetailDataLiveData = SingleLiveEvent<Resource<PayLoadDetail>>()
    val cryptoDetailDataLiveData: LiveData<Resource<PayLoadDetail>>
        get() = _cryptoDetailDataLiveData

    fun getDetailAssetList(assetId:String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val data= cryptoRepository.getAssetDetail(assetId = assetId)
            _cryptoDetailDataLiveData.postValue(data)

        } catch (e: Exception) {
            _cryptoDetailDataLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }

}