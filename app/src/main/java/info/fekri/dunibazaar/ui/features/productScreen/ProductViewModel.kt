package info.fekri.dunibazaar.ui.features.productScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.fekri.dunibazaar.model.data.Comment
import info.fekri.dunibazaar.model.repository.cart.CartRepository
import info.fekri.dunibazaar.model.repository.comment.CommentRepository
import info.fekri.dunibazaar.model.repository.product.ProductRepository
import info.fekri.dunibazaar.util.EMPTY_PRODUCT
import info.fekri.dunibazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())
    val isAddingProduct = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)

    private fun loadProductFromCache(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(productId)
        }
    }

    private fun loadBadgeNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }
    }

    fun loadData(productId: String, isInternetConnected: Boolean) {
        loadProductFromCache(productId)

        if (isInternetConnected) {
            loadAllComments(productId)
            loadBadgeNumber()
        }
    }

    private fun loadAllComments(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            comments.value = commentRepository.getAllComments(productId)
        }
    }

    fun addNewComment(productId: String, text: String, IsSuccess: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            commentRepository.addNewComment(productId, text, IsSuccess)
            delay(100)
            comments.value = commentRepository.getAllComments(productId)
        }
    }

    fun addProductToCart(productId: String, AddingToCartResult: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            isAddingProduct.value = true

            val result = cartRepository.addToCart(productId)
            delay(500) // just for animation

            isAddingProduct.value = false

            if (result) {
                AddingToCartResult.invoke("Product is Added to Cart")
            } else {
                AddingToCartResult.invoke("Product is not Added to Cart")
            }
        }
    }

}