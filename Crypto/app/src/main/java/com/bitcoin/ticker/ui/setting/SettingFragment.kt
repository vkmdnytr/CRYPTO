package com.bitcoin.ticker.ui.setting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitcoin.ticker.R
import com.bitcoin.ticker.ui.MainActivity
import com.bitcoin.ticker.ui.login.LoginActivity
import com.bitcoin.ticker.util.AppPreferences
import com.bitcoin.ticker.util.service.CoinService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting.*


@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoginButtonUi()
        setFavoriteCoinListenButtonUi()
        signInButton.setOnClickListener {
            if (auth.currentUser == null) {
                val intent = Intent(activity, LoginActivity::class.java)
                (activity as MainActivity?)?.startActivity(intent)
                findNavController().navigateUp()
            } else {
                FirebaseAuth.getInstance().signOut()
                emailEditText.text = "Lütfen Login olun"
                signInButton.text = "LOG IN"
            }
        }
        listenCrypto.setOnClickListener {
            setFavoriteCoinListenSetting()
        }

    }

    private fun setLoginButtonUi() {
        if (auth.currentUser == null) {
            emailEditText.text = "Lütfen Login olun"
            signInButton.text = "LOG IN"
        } else {
            emailEditText.text = auth.currentUser?.email.toString()
            signInButton.text = "LOG OUT"

        }
    }

    private fun setFavoriteCoinListenButtonUi() {
        this.context?.let {
            when {
                AppPreferences.getIsListen(it) == "0" -> {
                    listenCrypto.setBackgroundColor(Color.GRAY)
                }
                else -> {
                    listenCrypto.setBackgroundColor(ContextCompat.getColor(it, R.color.turquoise))
                }
            }
        }
    }

    private fun setFavoriteCoinListenSetting() {
        this.context?.let {
            when {
                AppPreferences.getIsListen(it) == "0" -> {
                    AppPreferences.setIsListen(it, "1")
                    listenCrypto.setBackgroundColor(ContextCompat.getColor(it, R.color.turquoise))
                    (activity as MainActivity?)?.startService(Intent(activity, CoinService::class.java))
                }
                else -> {
                    AppPreferences.setIsListen(it, "0")
                    (activity as MainActivity?)?.stopService(Intent(activity, CoinService::class.java))
                    listenCrypto.setBackgroundColor(Color.GRAY)
                }
            }
        }
    }

}