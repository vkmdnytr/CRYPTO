package com.bitcoin.ticker.data.rest

import javax.inject.Inject


class CryptoRemoteDataSource @Inject constructor(
        private val cryptoApisService: CryptoApisService
):BaseDataSource() {

    suspend fun getAllAssetList(skip: Int = 0,
                                limit: Int =50,
                                type: String ="crypto") = getResult { cryptoApisService.getAllAssetList(skip,limit,type) }

    suspend fun getAssetDetail(assetId: String) = getResult { cryptoApisService.getAllAssetDetail(assetId = assetId) }

}