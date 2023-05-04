package info.fekri.dunibazaar.model.repository.user

import android.content.SharedPreferences
import com.google.gson.JsonObject
import info.fekri.dunibazaar.model.net.ApiService
import info.fekri.dunibazaar.model.repository.TokenInMemory
import info.fekri.dunibazaar.util.VALUE_SUCCESS

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences
    ): UserRepository {

    override suspend fun signUp(name: String, userName: String, password: String): String {

        val jsonObj = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", userName)
            addProperty("password", password)
        }

        val result = apiService.signUp(jsonObj)
        return if (result.success) {
            // cash in memory
            TokenInMemory.refreshToken(userName, result.token)
            saveToken(result.token)
            saveUserName(userName)

            VALUE_SUCCESS
        } else {
            result.message
        }

    }
    override suspend fun signIn(userName: String, password: String): String {

        val jsonObj = JsonObject().apply {
            addProperty("email", userName)
            addProperty("password", password)
        }

        val result = apiService.signIn(jsonObj)
        return if (result.success) {
            TokenInMemory.refreshToken(userName, result.token)
            saveToken(result.token)
            saveUserName(userName)

            VALUE_SUCCESS
        } else {
            result.message
        }

    }

    override fun signOut() {
        TokenInMemory.refreshToken(null, null)
        sharedPref.edit().clear().apply()
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName(),  getToken())
    }

    override fun saveToken(newToken: String) {
        sharedPref.edit().putString("token", newToken).apply()
    }
    override fun getToken(): String {
        return sharedPref.getString("token", "")!!
    }

    override fun saveUserName(userName: String) {
        sharedPref.edit().putString("username", userName).apply()
    }
    override fun getUserName(): String {
        return sharedPref.getString("username", "")!!
    }

}