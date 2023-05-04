package info.fekri.dunibazaar.model.net

import com.google.gson.JsonObject
import info.fekri.dunibazaar.model.data.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("signUp")
    suspend fun signUp( @Body jsonObject: JsonObject ): LoginResponse

    @POST("signIn")
    suspend fun signIn( @Body jsonObject: JsonObject ): LoginResponse

}