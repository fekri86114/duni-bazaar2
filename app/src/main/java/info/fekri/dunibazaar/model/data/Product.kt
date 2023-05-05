package info.fekri.dunibazaar.model.data

data class ProductResponse(
    val success: Boolean,
    val products: List<Product>
)

data class Product(
    val category: String,
    val detailText: String,
    val imgUrl: String,
    val material: String,
    val name: String,
    val price: String,
    val productId: String,
    val soldItem: String,
    val tags: String
)
