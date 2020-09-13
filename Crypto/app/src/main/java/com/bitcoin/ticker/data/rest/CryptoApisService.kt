package com.bitcoin.ticker.data.rest

import com.bitcoin.ticker.data.model.asset.PayLoadAssetResponce
import com.bitcoin.ticker.data.model.detail.PayLoadDetail
import com.bitcoin.ticker.data.rest.ServiceUrls.ASSETS_DETAIL_URL
import com.bitcoin.ticker.data.rest.ServiceUrls.ASSETS_URL
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoApisService{

    @GET(ASSETS_URL)
    suspend fun getAllAssetList(@Query("skip") skip: Int = 100,
                                @Query("limit") limit: Int =50,
                                @Query("type") type: String ="crypto") : Response<PayLoadAssetResponce>

    @GET(ASSETS_DETAIL_URL)
    suspend fun getAllAssetDetail(@Path("assetId") assetId: String) : Response<PayLoadDetail>
}