package com.bitcoin.ticker.util.listener

import com.bitcoin.ticker.data.model.favorite.FavoriteCoin

interface IFavoriteListener {
    fun setOnFavoriteList(favoriteList:ArrayList<FavoriteCoin>)
}