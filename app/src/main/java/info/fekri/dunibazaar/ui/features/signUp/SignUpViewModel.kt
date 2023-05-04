package info.fekri.dunibazaar.ui.features.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {

    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    fun signUpUser() {
    }

}