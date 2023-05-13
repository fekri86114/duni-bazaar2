package info.fekri.dunibazaar.model.data

data class SubmitOrder(
    val success: Boolean,
    val orderId: String,
    val paymentLink: String
)
