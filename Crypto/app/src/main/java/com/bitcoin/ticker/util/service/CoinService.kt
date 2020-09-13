package com.bitcoin.ticker.util.service


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bitcoin.ticker.R
import com.bitcoin.ticker.data.rest.CryptoRepository
import com.bitcoin.ticker.util.AppPreferences
import com.bitcoin.ticker.util.sealed.Resource
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CoinService: Service() {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface CoinServiceInterFace {
        val cryptoSRepository: CryptoRepository
    }

   lateinit var cryptoSRepository: CryptoRepository
    private var oldPrice:Double=-1.0

    private var my_runnable:Runnable? = Runnable {
        ping()
    }
    private var mHandler :Handler?=null
    private val NOTIFICATION_ID=1001
    private val CHANNEL_ID="CRYPTO"

    override fun onBind(intent: Intent?): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        Toast.makeText(this, "Service is Created", Toast.LENGTH_SHORT).show()
        val myMessagingServiceInterface: CoinServiceInterFace = EntryPoints.get(applicationContext, CoinServiceInterFace::class.java)
        cryptoSRepository= myMessagingServiceInterface.cryptoSRepository
        Log.d("TAG", "CREATE")


    }


    override fun onStartCommand(intent: Intent?, x: Int, y: Int): Int {
        Log.d("TAG", "onStartCommand")
        start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
        Log.d("TAG", "DESTROY")
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun notifyThis(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= 26) {
            val name: CharSequence = "my_channel"
            val Description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = Description
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(mChannel)
        }

        val notify: Notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_apple_health)
                .build()

        notify.flags = notify.flags or Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(NOTIFICATION_ID, notify)
    }

    private fun ping() {
        try {
            val user= AppPreferences.getFavorite(this@CoinService)
            val favoriteid= user?.array?.get(0)?.coinId
            GlobalScope.launch {

                when (val result= favoriteid?.let { cryptoSRepository.getAssetDetail(it) }) {
                    is Resource.Success -> {
                        val payLoadDetail = result.value
                        if(oldPrice==-1.0){
                            oldPrice=payLoadDetail.payload.price
                        } else {
                            if(oldPrice!=payLoadDetail.payload.price){
                                Log.d("TAG", "True OldPrice$oldPrice")
                                val newPrice=payLoadDetail.payload.price

                                if(newPrice-oldPrice>1){
                                    oldPrice=newPrice
                                    Log.d("TAG", "True OldPrice$oldPrice")
                                    notifyThis(payLoadDetail.payload.name,"Price changed")
                                }
                                if(-1>newPrice-oldPrice){
                                    oldPrice=newPrice
                                    notifyThis(payLoadDetail.payload.name,"Price changed")
                                }

                            }
                        }


                    }
                    is Resource.Error -> {
                        Log.d("TAG", "${result.message}")
                    }
                }
            }

        } catch (e: Exception) {
            Log.d("TAG", "In onStartCommand")
            e.printStackTrace()
        }
        restart()
    }

    private fun start() {
        mHandler=Handler()
        my_runnable?.let {my_runnable->
            mHandler?.postDelayed(my_runnable, 20000)
    }}

    private fun stop() {
        mHandler?.removeCallbacks {my_runnable}
        my_runnable=null
    }

    private fun restart() {
        my_runnable?.let {my_runnable->
            mHandler?.removeCallbacks(my_runnable)
            mHandler?.postDelayed(my_runnable, 20000)
        }

    }


}
