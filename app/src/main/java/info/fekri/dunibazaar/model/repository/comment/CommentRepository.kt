package info.fekri.dunibazaar.model.repository.comment

import info.fekri.dunibazaar.model.data.Comment

interface CommentRepository {

    suspend fun getAllComments(productId: String): List<Comment>

}