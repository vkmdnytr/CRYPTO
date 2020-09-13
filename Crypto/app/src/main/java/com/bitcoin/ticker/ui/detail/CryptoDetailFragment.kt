package com.bitcoin.ticker.ui.detail


import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitcoin.ticker.R
import com.bitcoin.ticker.data.model.detail.PayLoadDetail
import com.bitcoin.ticker.data.model.favorite.FavoriteCoin
import com.bitcoin.ticker.data.model.favorite.FavoriteCoinList
import com.bitcoin.ticker.ui.base.BaseFunction
import com.bitcoin.ticker.ui.base.ViewModelBaseFragment
import com.bitcoin.ticker.util.AppPreferences
import com.bitcoin.ticker.util.AppPreferences.getFavorite
import com.bitcoin.ticker.util.firebase.FireStoreHelper
import com.bitcoin.ticker.util.sealed.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.crypto_detail_fragment.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CryptoDetailFragment : ViewModelBaseFragment() , BaseFunction {


    private val viewModel: CryptoDetailViewModel by viewModels()
    private val cryptoDetailFragmentArgs: CryptoDetailFragmentArgs? by navArgs()
    private var payLoadDetail: PayLoadDetail? = null
    private var user: String? = null
    private var id: String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.crypto_detail_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         id = cryptoDetailFragmentArgs?.id

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = FirebaseAuth.getInstance().currentUser?.email
        setObserveViewModel()
        setFavoriteImageUi()


        toolBarImage.setOnClickListener {
            findNavController().navigateUp()
        }
        imageDetailView.setOnClickListener {
            val payLoadDetail = payLoadDetail ?: return@setOnClickListener
            this.context?.let { context ->

                if (isContainFavorite(context)) {
                    Log.d("TAG", "DELETE")
                    imageDetailView.setColorFilter(Color.GRAY)
                    payLoadDetail.payload._id.let {
                        Log.d("TAG", "$it")
                        FireStoreHelper.deleteFavoriteItemForUser(it)
                    }
                    deleteFavoriteLocale(context)

                } else {
                    Log.d("TAG", "SAVE")
                    saveFireStore()
                    imageDetailView.setColorFilter(Color.RED)

                }

            }

        }
        callDetailService()

    }

    private fun setFavoriteImageUi() {
        this.context?.let {context->
            if (isContainFavorite(context)) {
                imageDetailView.setColorFilter(Color.RED)
            } else {
                imageDetailView.setColorFilter(Color.GRAY)
            }
        }
    }

    private fun callDetailService() {
        showProgress()
        id?.let { viewModel.getDetailAssetList(it) }
    }

    private fun saveFireStore() {
        val user = user ?: return
        payLoadDetail?.let { payLoadDetail ->
            FireStoreHelper.addFavoriteItemForUser(
                    user,
                    payLoadDetail.payload._id,
                    payLoadDetail.payload.originalSymbol,
                    "1"
            )
            this.context?.let { context ->
                addFavoriteLocale(context)
            }

        }
    }

    private fun isContainFavorite(
            context: Context
    ): Boolean {
        val user = user ?: return false
        val payLoadDetail = payLoadDetail ?: return false
        val favorite = FavoriteCoin(
                user, payLoadDetail.payload._id, payLoadDetail.payload.originalSymbol, "1"
        )

        val array = getFavorite(context)?.array
        return array?.contains(favorite) ?: false
    }

    private fun deleteFavoriteLocale(
            context: Context
    ) {
        val user = user ?: return
        val payLoadDetail = payLoadDetail ?: return
        val favorite = FavoriteCoin(
                user, payLoadDetail.payload._id, payLoadDetail.payload.originalSymbol,
                "1"
        )
        val array = getFavorite(context)
        val arr = array?.array ?: arrayListOf()
        arr.remove(favorite)
        AppPreferences.setFavoriteData(context, FavoriteCoinList(arr))
    }

    private fun addFavoriteLocale(
            context: Context
    ) {
        val user = user ?: return
        val payLoadDetail = payLoadDetail ?: return
        val favorite = FavoriteCoin(
                user, payLoadDetail.payload._id, payLoadDetail.payload.originalSymbol,
                "1"
        )
        val array = getFavorite(context)
        val arr = array?.array ?: arrayListOf()
        arr.add(favorite)
        AppPreferences.setFavoriteData(context, FavoriteCoinList(arr))
    }


    private fun getDate(time_stamp_server: Long): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return formatter.format(time_stamp_server*1000)
    }

    override fun setObserveViewModel() {

        viewModel.cryptoDetailDataLiveData.observe(viewLifecycleOwner, Observer { result ->
            showView()
            when (result) {
                is Resource.Success -> {
                    payLoadDetail = result.value

                    if (isContainFavorite(requireContext())) {
                        imageDetailView.setColorFilter(Color.RED)
                    } else {
                        imageDetailView.setColorFilter(Color.GRAY)
                    }

                    payLoadDetail?.let { payLoadDetail ->

                        textDetailTitle.text = payLoadDetail.payload.assetId
                        textDetailTitle.text = payLoadDetail.payload.name
                        symbolSubTitle.text=payLoadDetail.payload.originalSymbol
                        val myDate = getDate(payLoadDetail.payload.lastUpdate.toLong())
                        dateSubTitle.text=myDate
                        priceSubTitle.text= payLoadDetail.payload.price.toString()

                        payLoadDetail.payload.logo?.imageData?.let {
                            val img: String = it
                            val imageAsBytes: ByteArray = Base64.decode(img.toByteArray(), Base64.DEFAULT)
                            detailView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
                        }

                    }


                }

                is Resource.Error -> {
                    showGeneralErrorDialog(result.message)
                }
            }

        })
    }

     override fun showProgress() {
        containerLayout?.isVisible = false
        progressBar_cyclic?.isVisible = true
    }

     override fun showView() {
        containerLayout?.isVisible = true
        progressBar_cyclic?.isVisible = false
    }

}