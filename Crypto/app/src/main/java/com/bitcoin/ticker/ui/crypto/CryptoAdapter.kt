package com.bitcoin.ticker.ui.crypto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitcoin.ticker.R
import com.bitcoin.ticker.data.model.asset.PayloadX
import com.bitcoin.ticker.util.listener.IListItemClickListener


class CryptoAdapter(
    private var dataSource: List<PayloadX>,
    var listItemClickListener: IListItemClickListener

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateDataSource(dataSource: List<PayloadX>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val createTimeView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_crypto_coin, parent, false)
        return CryptoViewHolder(createTimeView, listItemClickListener)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val youtubeHolder = holder as  CryptoViewHolder
        val videoItem = dataSource[position]
        youtubeHolder.bindData(videoItem)
    }

    override fun getItemCount() = dataSource.size

    class  CryptoViewHolder(
        itemView: View,
        val listItemClickListener: IListItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private var  layout: LinearLayout = itemView.findViewById(R.id.layout)
        private var  image: ImageView = itemView.findViewById(R.id.imageViewIcon)
        private var txtCoinSymbol: TextView = itemView.findViewById(R.id.txtCoinSymbol)
        private var txtCoinName: TextView = itemView.findViewById(R.id.txtCoinName)
        fun bindData(
            mDataSource: PayloadX
        ) {
            txtCoinSymbol.text = mDataSource.originalSymbol
            txtCoinName.text = mDataSource.name
            layout.setOnClickListener{
                listItemClickListener.setOnClickItemListener(mDataSource._id)
            }

            mDataSource.logo?.imageData?.let{
                val img: String = it
                val imageAsBytes: ByteArray = Base64.decode(img.toByteArray(), Base64.DEFAULT)
                image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
            }



        }


    }
}