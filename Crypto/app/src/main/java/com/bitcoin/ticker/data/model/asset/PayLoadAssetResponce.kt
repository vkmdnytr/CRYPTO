package com.bitcoin.ticker.data.model.asset

data class PayLoadAssetResponce(
    val meta: Meta,
    val payload: List<PayloadX>
)