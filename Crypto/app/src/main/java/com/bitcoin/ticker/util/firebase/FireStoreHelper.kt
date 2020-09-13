package com.bitcoin.ticker.util.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.bitcoin.ticker.data.model.Payload
import com.bitcoin.ticker.data.model.favorite.FavoriteCoin
import com.bitcoin.ticker.util.listener.IFavoriteListener
import com.google.firebase.firestore.FirebaseFirestore


object FireStoreHelper {
    private val db = FirebaseFirestore.getInstance()

    var favoriteCoin: ArrayList<FavoriteCoin>? = arrayListOf()

    fun addFavoriteItemForUser(user: String, coinId: String, coinSymbol: String,isFavorite:String) {

        db.collection("users_coin").document(coinId).set(hashMapOf(
            "user" to user,
            "coinId" to coinId,
            "coinSymbol" to coinSymbol,
            "isFavorite" to isFavorite
        ))


    }

    fun deleteFavoriteItemForUser(coinId: String){
        db.collection("users_coin").document(coinId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                readFavoriteItemForUser()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun readFavoriteItemForUser() {

        db.collection("users_coin")
            .get()
            .addOnSuccessListener { result ->
                favoriteCoin?.clear()
                for (document in result) {
                    favoriteCoin?.add(
                        FavoriteCoin(
                            document.data["user"].toString(),
                            document.data["coinId"].toString(),
                            document.data["coinSymbol"].toString(),
                            document.data["isFavorite"].toString()

                        )

                    )
                    Log.d(TAG, "${document.id} => ${document.data}")

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun readFavoriteItemForUser(listener: IFavoriteListener) {

        db.collection("users_coin")
            .get()
            .addOnSuccessListener { result ->
                favoriteCoin?.clear()
                for (document in result) {

                    favoriteCoin?.add(
                        FavoriteCoin(
                            document.data["user"].toString(),
                            document.data["coinId"].toString(),
                            document.data["coinSymbol"].toString(),
                            document.data["isFavorite"].toString()

                        )


                    )

                }
                favoriteCoin?.let { listener.setOnFavoriteList(it) }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}