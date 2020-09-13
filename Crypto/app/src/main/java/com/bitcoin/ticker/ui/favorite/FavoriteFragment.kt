package com.bitcoin.ticker.ui.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitcoin.ticker.R
import com.bitcoin.ticker.data.model.favorite.FavoriteCoin
import com.bitcoin.ticker.ui.base.BaseFunction
import com.bitcoin.ticker.ui.crypto.CryptoFragmentDirections
import com.bitcoin.ticker.util.firebase.FireStoreHelper
import com.bitcoin.ticker.util.listener.IFavoriteListener
import com.bitcoin.ticker.util.listener.IListItemClickListener
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.favorite_fragment.*
import kotlinx.android.synthetic.main.favorite_fragment.progressBar_cyclic
import kotlinx.android.synthetic.main.fragment_crypto_coin.*


@AndroidEntryPoint
class FavoriteFragment : Fragment(), IFavoriteListener, BaseFunction, IListItemClickListener {

    private lateinit var favoriteAdapt: FavoriteAdapter

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        favoriteAdapt = FavoriteAdapter(
            emptyList(),this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (auth.currentUser != null) {
            showProgress()
            FireStoreHelper.readFavoriteItemForUser(this)
        }
        setAdapterForRecyclerView()
    }

    private fun setAdapterForRecyclerView() {
        favoriterRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        favoriterRecycler.adapter = favoriteAdapt
    }

    override fun setOnFavoriteList(favoriteList: ArrayList<FavoriteCoin>) {
        showView()

        val listForUser = favoriteList.filter { it.user == auth.currentUser?.email.toString() }
        favoriteAdapt.updateDataSource(listForUser)


    }

    override fun showProgress() {
        favoriterRecycler.isVisible = false
        progressBar_cyclic.isVisible = true
    }

    override fun showView() {
        favoriterRecycler.isVisible = true
        progressBar_cyclic.isVisible = false
    }

    private fun goToDetail(id: String) {
        val direction = FavoriteFragmentDirections.actionFavoriteFragmentToCryptoDetailFragment(id)
        findNavController().navigate(direction)
    }

    override fun setOnClickItemListener(id: String) {
        goToDetail(id)
    }




}