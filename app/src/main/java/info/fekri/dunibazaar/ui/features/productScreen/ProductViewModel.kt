package info.fekri.dunibazaar.ui.features.productScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.fekri.dunibazaar.model.data.Comment
import info.fekri.dunibazaar.model.repository.comment.CommentRepository
import info.fekri.dunibazaar.model.repository.product.ProductRepository
import info.fekri.dunibazaar.util.EMPTY_PRODUCT
import info.fekri.dunibazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {
    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())

    private fun loadProductFromCache(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(productId)
        }

    }

    fun loadData(productId: String, isInternetConnected: Boolean) {

        loadProductFromCache(productId)

        if (isInternetConnected) {
            loadAllComments(productId)
        }
    }

    private fun loadAllComments(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            comments.value = commentRepository.getAllComments(productId)
        }

    }

}