package info.fekri.dunibazaar.model.repository.cart

import android.content.SharedPreferences
import com.google.gson.JsonObject
import info.fekri.dunibazaar.model.data.Checkout
import info.fekri.dunibazaar.model.data.SubmitOrder
import info.fekri.dunibazaar.model.data.UserCartInfo
import info.fekri.dunibazaar.model.net.ApiService
import info.fekri.dunibazaar.util.NO_PAYMENT

class CartRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences
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

    override suspend fun submitOrder(address: String, postalCode: String): SubmitOrder {
        val jsonObject = JsonObject().apply {
            addProperty("address", address)
            addProperty("postalCode", postalCode)
        }
        val result = apiService.submitOrder(jsonObject)
        setOrderId(result.orderId)
        return result
    }

    override suspend fun checkout(orderId: String): Checkout {
        val jsonObject = JsonObject().apply {
            addProperty("orderId", orderId)
        }
        return apiService.checkout(jsonObject)
    }

    override fun setOrderId(orderId: String) {
        sharedPref.edit().putString("orderId", orderId).apply()
    }

    override fun getOrderId(): String {
        return sharedPref.getString("orderId", "0")!!
    }

    override fun setPurchaseStatus(status: Int) {
        sharedPref.edit().putInt("purchase_status", status).apply()
    }

    override fun getPurchaseStatus(): Int {
        return sharedPref.getInt("purchase_status", NO_PAYMENT)
    }

}