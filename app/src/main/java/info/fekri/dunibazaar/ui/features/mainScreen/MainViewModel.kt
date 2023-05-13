package info.fekri.dunibazaar.ui.features.mainScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.fekri.dunibazaar.model.data.Ads
import info.fekri.dunibazaar.model.data.Checkout
import info.fekri.dunibazaar.model.data.Product
import info.fekri.dunibazaar.model.repository.cart.CartRepository
import info.fekri.dunibazaar.model.repository.product.ProductRepository
import info.fekri.dunibazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class MainViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    isInternetConnected: Boolean
) : ViewModel() {
    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgress = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)
    val showPaymentResultDialog = mutableStateOf(false)
    val checkoutData = mutableStateOf(Checkout(null, null))

    // get data from server and update when created
    // an instance from MainViewModel
    init {
        refreshAllDataFromNet(isInternetConnected)
    }

    fun getCheckoutData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = cartRepository.checkout(cartRepository.getOrderId())
            if (result.success!!) {
                checkoutData.value = result
                showPaymentResultDialog.value = true
            }
        }
    }

    fun getPaymentStatus(): Int {
        return cartRepository.getPurchaseStatus()
    }

    fun setPaymentStatus(status: Int) {
        cartRepository.setPurchaseStatus(status)
    }

    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {

            if (isInternetConnected) {
                showProgress.value = true

                // this is for test!
                delay(1200)

                val newDataProducts =
                    async { productRepository.getAllProducts(isInternetConnected) }
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

    fun loadBadgeNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }
    }

}