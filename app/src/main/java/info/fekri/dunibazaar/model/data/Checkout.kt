package info.fekri.dunibazaar.model.data

data class Checkout(
    val order: Order?,
    val success: Boolean?
) {

    data class Order(
        val amount: String,
        val creationTime: String,
        val `data`: Data,
        val orderId: String,
        val paymentId: String,
        val status: String
    ) {

        data class Data(
            val address: String,
            val postalCode: String
        )

    }

}
