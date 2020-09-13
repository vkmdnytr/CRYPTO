package com.bitcoin.ticker.ui.favorite

import android.graphics.Color
import android.graphics.Color.red
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitcoin.ticker.R
import com.bitcoin.ticker.data.model.favorite.FavoriteCoin
import com.bitcoin.ticker.util.listener.IListItemClickListener


class FavoriteAdapter(
    private var dataSource: List<FavoriteCoin>,
    var listItemClickListener: IListItemClickListener

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateDataSource(dataSource: List<FavoriteCoin>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val createTimeView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_crypto_coin, parent, false)
        return FavoriteCoinViewHolder(createTimeView,listItemClickListener)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val youtubeHolder = holder as  FavoriteCoinViewHolder
        val videoItem = dataSource[position]
        youtubeHolder.bindData(videoItem)
    }

    override fun getItemCount() = dataSource.size

    class  FavoriteCoinViewHolder(
            itemView: View,
            private val listItemClickListener: IListItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private var  layout: LinearLayout = itemView.findViewById(R.id.layout)
        private var  image: ImageView = itemView.findViewById(R.id.imageViewIcon)
        private var txtCoinSymbol: TextView = itemView.findViewById(R.id.txtCoinSymbol)
        private var txtCoinName: TextView = itemView.findViewById(R.id.txtCoinName)
        fun bindData(
            mDataSource: FavoriteCoin
        ) {
            txtCoinSymbol.text = mDataSource.coinSymbol
            txtCoinName.text = mDataSource.user
            layout.setOnClickListener {
                listItemClickListener.setOnClickItemListener(mDataSource.coinId)
            }
            image.setImageResource(R.drawable.ic_apple_health)
            image.setColorFilter(Color.RED)
        }


    }
}