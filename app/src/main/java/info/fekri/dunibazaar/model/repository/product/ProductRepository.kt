package info.fekri.dunibazaar.model.repository.product

import info.fekri.dunibazaar.model.data.Ads
import info.fekri.dunibazaar.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(isInternetConnected: Boolean): List<Product>

    suspend fun getAllAds(isInternetConnected: Boolean): List<Ads>

    suspend fun getAllProductsByCategory(category: String): List<Product>

    suspend fun getProductById(productId: String): Product

}