package info.fekri.dunibazaar.model.repository.cart

import info.fekri.dunibazaar.model.data.UserCartInfo

interface CartRepository {

    suspend fun addToCart(productId: String): Boolean

    suspend fun removeFromCart(productId: String): Boolean

    suspend fun getCartSize(): Int

    suspend fun getUserCartInfo(): UserCartInfo

}