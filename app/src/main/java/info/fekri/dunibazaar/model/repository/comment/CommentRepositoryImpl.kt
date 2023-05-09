package info.fekri.dunibazaar.model.repository.comment

import com.google.gson.JsonObject
import info.fekri.dunibazaar.model.data.Comment
import info.fekri.dunibazaar.model.net.ApiService

class CommentRepositoryImpl(
    private val apiService: ApiService
): CommentRepository {

    override suspend fun getAllComments(productId: String): List<Comment> {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        val data = apiService.getAllComments(jsonObject)

        if (data.success) {
            return data.comments
        }
        return listOf()
    }

}