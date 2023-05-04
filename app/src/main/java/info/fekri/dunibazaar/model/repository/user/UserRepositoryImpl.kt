package info.fekri.dunibazaar.model.repository.user

import android.content.SharedPreferences
import com.google.gson.JsonObject
import info.fekri.dunibazaar.model.net.ApiService
import info.fekri.dunibazaar.model.repository.TokenInMemory
import info.fekri.dunibazaar.util.VALUE_SUCCESS

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences
) : UserRepository {

    override suspend fun signUp(name: String, username: String, password: String) :String {

        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", username)
            addProperty("password", password)
        }

        val result = apiService.signUp(jsonObject)
        if(result.success) {
            TokenInMemory.refreshToken(username , result.token)
            saveToken(result.token)
            saveUserName(username)
            return VALUE_SUCCESS
        } else {
            return result.message
        }

    }

    override suspend fun signIn(username: String, password: String):String {

        val jsonObject = JsonObject().apply {
            addProperty("email" , username)
            addProperty("password" , password)
        }

        val result = apiService.signIn(jsonObject)
        if(result.success) {
            TokenInMemory.refreshToken(username , result.token)
            saveToken(result.token)
            saveUserName(username)
            return VALUE_SUCCESS
        } else {
            return result.message
        }

    }

    override fun signOut() {
        TokenInMemory.refreshToken(null , null)
        sharedPref.edit().clear().apply()
    }

    override fun loadToken() {
        TokenInMemory.refreshToken( getUserName() , getToken() )
    }

    override fun saveToken(newToken: String) {
        sharedPref.edit().putString("token" , newToken).apply()
    }

    override fun getToken(): String? {
        return sharedPref.getString("token" , null)
    }

    override fun saveUserName(username: String) {
        sharedPref.edit().putString("username" , username).apply()
    }

    override fun getUserName(): String? {
        return sharedPref.getString("username" , null)
    }

}
