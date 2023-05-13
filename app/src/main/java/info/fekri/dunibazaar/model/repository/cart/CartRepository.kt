package info.fekri.dunibazaar.model.repository.cart

import info.fekri.dunibazaar.model.data.Checkout
import info.fekri.dunibazaar.model.data.SubmitOrder
import info.fekri.dunibazaar.model.data.UserCartInfo

interface CartRepository {

    suspend fun addToCart(productId: String): Boolean

    suspend fun removeFromCart(productId: String): Boolean

    suspend fun getCartSize(): Int

    suspend fun getUserCartInfo(): UserCartInfo

    suspend fun submitOrder(address: String, postalCode: String): SubmitOrder
    suspend fun checkout(orderId: String): Checkout

    fun setOrderId(orderId: String)
    fun getOrderId(): String

    fun setPurchaseStatus(status: Int)
    fun getPurchaseStatus(): Int

}