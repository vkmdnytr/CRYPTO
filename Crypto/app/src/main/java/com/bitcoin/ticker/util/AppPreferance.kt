package com.bitcoin.ticker.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.provider.Contacts.SettingsColumns.KEY
import com.bitcoin.ticker.data.model.favorite.FavoriteCoinList
import com.google.gson.Gson


object AppPreferences {

    private val FAVORITE = "FAVORITE"
    private val IS_LISTEN = "IS_LISTEN"

    fun setFavoriteData(context: Context, favoriteData: FavoriteCoinList?) {
        val data: String = if (favoriteData == null) {
            ""
        } else {
            Gson().toJson(favoriteData)
        }
        val preferences = context.getSharedPreferences(FAVORITE, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(FAVORITE, data)
        editor.apply()
    }

    fun getFavorite(context: Context): FavoriteCoinList? {
        val preferences = context.getSharedPreferences(FAVORITE, Context.MODE_PRIVATE)
        return Gson().fromJson(preferences.getString(FAVORITE, ""), FavoriteCoinList::class.java)
    }
    fun setIsListen(context: Context,isListen:String) {
        val sharedPreferences= context.getSharedPreferences(IS_LISTEN, MODE_PRIVATE)
        val editor= sharedPreferences.edit()
        editor.putString(IS_LISTEN, isListen)
        editor.apply()
    }

    fun getIsListen(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(IS_LISTEN, MODE_PRIVATE)
        return sharedPreferences.getString(IS_LISTEN, "")
    }

    fun reset(context: Context) {
        setFavoriteData(context, null)
    }
}