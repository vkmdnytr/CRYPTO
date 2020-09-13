package com.bitcoin.ticker.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bitcoin.ticker.R
import com.bitcoin.ticker.data.rest.CryptoRemoteDataSource
import com.bitcoin.ticker.databinding.ActivityMainBinding
import com.bitcoin.ticker.ui.login.LoginActivity
import com.bitcoin.ticker.util.AppPreferences
import com.bitcoin.ticker.util.firebase.FireStoreHelper
import com.bitcoin.ticker.util.service.CoinService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var navHostFragment:NavHostFragment
    private lateinit var navController: NavController

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.setIsListen(this, "1")
        startService(Intent(this@MainActivity, CoinService::class.java))
        auth = Firebase.auth
        setUpNavigation()
        setNavigationControlDestination()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        FireStoreHelper.readFavoriteItemForUser()
        if(currentUser==null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }


    private fun setUpNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(
            bottomLayout,
            navHostFragment.navController
        )

    }

    private fun setNavigationControlDestination() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cryptoDetailFragment-> {
                    bottomLayout.isVisible = false
                }
                else -> bottomLayout.isVisible = true
            }
        }
    }




    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}