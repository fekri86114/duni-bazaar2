package info.fekri.dunibazaar.ui.features.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel: ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")

    fun signInUser() {
    }

}