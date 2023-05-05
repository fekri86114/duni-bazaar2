package info.fekri.dunibazaar.model.data

data class AdsResponse(
    val success: Boolean,
    val ads: List<Ads>
)

data class Ads(
    val adId: String,
    val imageUrl: String,
    val productId: String
)
