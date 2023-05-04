package info.fekri.dunibazaar.di

import info.fekri.dunibazaar.ui.features.signIn.SignInViewModel
import info.fekri.dunibazaar.ui.features.signUp.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    viewModel { SignUpViewModel() }
    viewModel { SignInViewModel() }
}