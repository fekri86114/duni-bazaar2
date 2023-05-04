package info.fekri.dunibazaar.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import dev.burnoo.cokoin.navigation.getNavController
import info.fekri.dunibazaar.R
import info.fekri.dunibazaar.ui.theme.BackgroundMain
import info.fekri.dunibazaar.ui.theme.Blue
import info.fekri.dunibazaar.ui.theme.MainAppTheme
import info.fekri.dunibazaar.util.MyScreens

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            IntroScreen()
        }
    }
}

@Composable
fun IntroScreen() {
    val navigation = getNavController()

    Image(
        painter = painterResource(id = R.drawable.img_intro),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = {
                /* go to `SignUpScreen` */
                navigation.navigate(MyScreens.SignUpScreen.route)
            }
        ) {
            Text(text = "Sign Up")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            onClick = {
                /* go to `SignInScreen` */
                navigation.navigate(MyScreens.SignInScreen.route)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
        ) {
            Text(text = "Sign In", color = Blue)
        }
    }

}