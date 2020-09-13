package com.bitcoin.ticker.data.model.asset


data class PayloadX(
    val _id: String,
    val change: Double,
    val change1Hour: Double,
    val change1Week: Double,
    val cryptoType: Boolean,
    val earliestKnownPrice: Double,
    val earliestTradeDate: Int,
    val lastUpdate: Double,
    val logo: Logo?,
    val marketCap: Double,
    val name: String,
    val originalSymbol: String,
    val price: Double,
    val slug: String,
    val supply: Double,
    val volume: Double
)