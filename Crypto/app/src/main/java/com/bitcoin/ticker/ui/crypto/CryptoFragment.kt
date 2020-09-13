package com.bitcoin.ticker.ui.crypto


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitcoin.ticker.R
import com.bitcoin.ticker.data.model.asset.PayloadX
import com.bitcoin.ticker.ui.base.BaseFunction
import com.bitcoin.ticker.ui.base.ViewModelBaseFragment
import com.bitcoin.ticker.util.listener.IListItemClickListener
import com.bitcoin.ticker.util.sealed.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_crypto_coin.*


@AndroidEntryPoint
class CryptoFragment : ViewModelBaseFragment(), IListItemClickListener , BaseFunction {

    private val viewModel: CryptoViewModel by viewModels()
    private lateinit var cryptoAdapter: CryptoAdapter
    private var itemArrayList: List<PayloadX>? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crypto_coin, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cryptoAdapter = CryptoAdapter(
                emptyList(),
                this
        )

        createCounterRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        createCounterRecycler.adapter = cryptoAdapter


        setObserveViewModel()


        searchView.queryHint = "Search View"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
        showProgress()
        viewModel.getAllAssetList()

    }

    private fun filter(text: String) {
        val filterdNames: ArrayList<PayloadX> = ArrayList()
        itemArrayList?.map { item ->
            if (item.name.toLowerCase().contains(text.toLowerCase()) || item.originalSymbol.toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(item)
            }
            cryptoAdapter.updateDataSource(filterdNames)
        }


    }


    override fun showProgress() {
        createCounterRecycler?.isVisible=false
        progressBar_cyclic?.isVisible=true
    }

    override fun showView() {
        createCounterRecycler?.isVisible=true
        progressBar_cyclic?.isVisible=false
    }

    override fun setObserveViewModel() {
        viewModel.bitcoinDataLiveData.observe(viewLifecycleOwner, Observer { result ->
            Log.d("TAG", result.toString())
            when (result) {
                is Resource.Success -> {
                    showView()
                    itemArrayList = result.value.payload
                    itemArrayList?.let { payLoad ->
                        cryptoAdapter.updateDataSource(payLoad)
                    }
                }
                is Resource.Error -> {
                    showView()
                    showGeneralErrorDialog(result.message)
                }
            }

        })
    }

    override fun setOnClickItemListener(id: String) {
        goToDetail(id)
    }

    private fun goToDetail(id: String) {
        val direction = CryptoFragmentDirections.actionCreateCounterFragmentToCryptoDetailFragment(id)
        findNavController().navigate(direction)
    }


}