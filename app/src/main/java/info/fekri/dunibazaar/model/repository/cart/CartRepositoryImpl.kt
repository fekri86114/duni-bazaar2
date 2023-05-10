package info.fekri.dunibazaar.model.repository.cart

import com.google.gson.JsonObject
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

}