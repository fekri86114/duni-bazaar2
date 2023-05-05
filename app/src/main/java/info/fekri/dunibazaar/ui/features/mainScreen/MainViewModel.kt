package info.fekri.dunibazaar.ui.features.mainScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.fekri.dunibazaar.model.data.Ads
import info.fekri.dunibazaar.model.data.Product
import info.fekri.dunibazaar.model.repository.product.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class MainViewModel(
    private val productRepository: ProductRepository,
    isInternetConnected: Boolean
): ViewModel() {
    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgress = mutableStateOf(false)

    // get data from server and update when created
    // an instance from MainViewModel
    init { refreshAllDataFromNet(isInternetConnected) }

    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {

        viewModelScope.launch {

            if (isInternetConnected) {
                showProgress.value = true

                val newDataProducts = async { productRepository.getAllProducts(isInternetConnected) }
                val newDataAds = async { productRepository.getAllAds(isInternetConnected) }

                updateData(newDataProducts.await(), newDataAds.await())

                showProgress.value = false
            }

        }

    }

    private fun updateData(products: List<Product>, ads: List<Ads>) {
        dataProducts.value = products
        dataAds.value = ads
    }

}