package info.fekri.dunibazaar.model.repository.cart

import info.fekri.dunibazaar.model.data.Product

interface CartRepository {

    suspend fun addToCart(productId: String): Boolean

}