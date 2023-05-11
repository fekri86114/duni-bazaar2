package info.fekri.dunibazaar.model.repository.cart

import com.google.gson.JsonObject
import info.fekri.dunibazaar.model.data.UserCartInfo
import info.fekri.dunibazaar.model.net.ApiService

class CartRepositoryImpl(
    private val apiService: ApiService
): CartRepository {

    override suspend fun addToCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        return apiService.addProductToCart(jsonObject).success
    }

    override suspend fun removeFromCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        return apiService.removeFromCart(jsonObject).success
    }

    override suspend fun getCartSize(): Int {
        val result = apiService.getUserCart()
        if (result.success) {

            var counter = 0
            result.productList.forEach {
                counter += (it.quantity ?: "0").toInt()
            }

            return counter
        }

        return 0
    }

    override suspend fun getUserCartInfo(): UserCartInfo {
        return apiService.getUserCart()
    }

}