package com.bitcoin.ticker.data.model.favorite


data class FavoriteCoinList(
   val array:ArrayList<FavoriteCoin>?
)
data class FavoriteCoin(
    val user:String,val coinId:String,val coinSymbol:String,val isFavoriteCoin: String
)