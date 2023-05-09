package info.fekri.dunibazaar.model.data

data class CommentResponse(
    val comments: List<Comment>,
    val success: Boolean
)

data class Comment(
    val commentId: String,
    val text: String,
    val userEmail: String
)
