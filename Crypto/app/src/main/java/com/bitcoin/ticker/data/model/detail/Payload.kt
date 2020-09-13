package com.bitcoin.ticker.data.model.detail

data class Payload(
    val _id: String,
    val assetId: String,
    val change: Double,
    val change1Hour: Double,
    val change1Week: Double,
    val cryptoType: Boolean,
    val earliestKnownPrice: Double,
    val earliestTradeDate: Int,
    val lastUpdate: Int,
    val logo: Logo,
    val marketCap: Double,
    val name: String,
    val originalSymbol: String,
    val price: Double,
    val slug: String,
    val supply: Double,
    val volume: Double
)