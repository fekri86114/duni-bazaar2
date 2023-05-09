package info.fekri.dunibazaar.di

import android.content.Context
import androidx.room.Room
import info.fekri.dunibazaar.model.db.AppDatabase
import info.fekri.dunibazaar.model.net.createApiService
import info.fekri.dunibazaar.model.repository.comment.CommentRepository
import info.fekri.dunibazaar.model.repository.comment.CommentRepositoryImpl
import info.fekri.dunibazaar.model.repository.product.ProductRepository
import info.fekri.dunibazaar.model.repository.product.ProductRepositoryImpl
import info.fekri.dunibazaar.model.repository.user.UserRepository
import info.fekri.dunibazaar.model.repository.user.UserRepositoryImpl
import info.fekri.dunibazaar.ui.features.categoryScreen.CategoryViewModel
import info.fekri.dunibazaar.ui.features.mainScreen.MainViewModel
import info.fekri.dunibazaar.ui.features.productScreen.ProductViewModel
import info.fekri.dunibazaar.ui.features.signIn.SignInViewModel
import info.fekri.dunibazaar.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "duni_bazaar_database.db",
        ).build()
    }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (isNetConnected: Boolean) -> MainViewModel(get(), isNetConnected) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductViewModel(get(), get()) }
}