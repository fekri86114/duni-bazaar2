package info.fekri.dunibazaar.di

import android.content.Context
import info.fekri.dunibazaar.model.net.createApiService
import info.fekri.dunibazaar.model.repository.user.UserRepository
import info.fekri.dunibazaar.model.repository.user.UserRepositoryImpl
import info.fekri.dunibazaar.ui.features.signIn.SignInViewModel
import info.fekri.dunibazaar.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
}