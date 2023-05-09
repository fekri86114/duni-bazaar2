package info.fekri.dunibazaar.ui.features.productScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.fekri.dunibazaar.model.repository.product.ProductRepository
import info.fekri.dunibazaar.util.EMPTY_PRODUCT
import info.fekri.dunibazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository
): ViewModel() {
    val thisProduct = mutableStateOf(EMPTY_PRODUCT)

    private fun loadProductFromCache(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(productId)
        }

    }

    fun loadData(productId: String) {

        loadProductFromCache(productId)

    }

}