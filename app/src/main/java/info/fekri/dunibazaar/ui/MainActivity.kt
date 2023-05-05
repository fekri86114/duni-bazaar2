package info.fekri.dunibazaar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import info.fekri.dunibazaar.di.myModules
import info.fekri.dunibazaar.model.repository.TokenInMemory
import info.fekri.dunibazaar.model.repository.user.UserRepository
import info.fekri.dunibazaar.ui.features.IntroScreen
import info.fekri.dunibazaar.ui.features.mainScreen.MainScreen
import info.fekri.dunibazaar.ui.features.signIn.SignInScreen
import info.fekri.dunibazaar.ui.features.signUp.SignUpScreen
import info.fekri.dunibazaar.ui.theme.BackgroundMain
import info.fekri.dunibazaar.ui.theme.MainAppTheme
import info.fekri.dunibazaar.util.KEY_CATEGORY_ARG
import info.fekri.dunibazaar.util.KEY_PRODUCT_ARG
import info.fekri.dunibazaar.util.MyScreens
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                MainAppTheme {
                    Surface(color = BackgroundMain, modifier = Modifier.fillMaxSize()) {

                        val userRepository: UserRepository = get()
                        userRepository.loadToken()

                        DuniBazaarUi()

                    }
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            DuniBazaarUi()
        }
    }
}

@Composable
fun DuniBazaarUi() {
    val navController = rememberNavController()
    KoinNavHost(navController = navController, startDestination = MyScreens.MainScreen.route) {

        composable(MyScreens.MainScreen.route) {

            if (TokenInMemory.token != null) {
                MainScreen()
            } else {
                IntroScreen()
            }

        }

        composable(
            route = MyScreens.ProductScreen.route + "/{$KEY_PRODUCT_ARG}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) {
                type = NavType.StringType
            })
        ) {
            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG, "null"))
        }

        composable(
            route = MyScreens.CategoryScreen.route + "/{$KEY_CATEGORY_ARG}",
            arguments = listOf(navArgument(KEY_CATEGORY_ARG) {
                type = NavType.StringType
            })
        ) {
            CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG, "null"))
        }

        composable(MyScreens.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(MyScreens.CartScreen.route) {
            CartScreen()
        }

        composable(MyScreens.SignUpScreen.route) {
            SignUpScreen()
        }

        composable(MyScreens.SignInScreen.route) {
            SignInScreen()
        }

    }

}


@Composable
fun CartScreen() {

}

@Composable
fun ProfileScreen() {

}

@Composable
fun CategoryScreen(categoryName: String) {

}

@Composable
fun ProductScreen(productId: String) {

}

