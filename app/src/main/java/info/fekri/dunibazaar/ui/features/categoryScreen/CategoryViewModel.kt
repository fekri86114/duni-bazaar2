package info.fekri.dunibazaar.ui.features.categoryScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.fekri.dunibazaar.model.data.Product
import info.fekri.dunibazaar.model.repository.product.ProductRepository
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val productRepository: ProductRepository
): ViewModel() {
    val dataProducts = mutableStateOf<List<Product>>(listOf())

    fun loadDataByCategory(category: String) {

        viewModelScope.launch {
            val dataFromLocal = productRepository.getAllProductsByCategory(category)
            dataProducts.value = dataFromLocal
        }

    }

}