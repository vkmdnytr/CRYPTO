package com.bitcoin.ticker.data.rest

import javax.inject.Inject

class CryptoRepository @Inject constructor(private val cryptoRemoteDataSource: CryptoRemoteDataSource) {


    suspend fun getAllAssetList() =  cryptoRemoteDataSource.getAllAssetList()
    suspend fun getAssetDetail(assetId:String) =  cryptoRemoteDataSource.getAssetDetail(assetId = assetId)

}