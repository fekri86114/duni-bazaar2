package info.fekri.dunibazaar.ui.features.signIn

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import info.fekri.dunibazaar.R
import info.fekri.dunibazaar.ui.theme.BackgroundMain
import info.fekri.dunibazaar.ui.theme.Blue
import info.fekri.dunibazaar.ui.theme.MainAppTheme
import info.fekri.dunibazaar.ui.theme.Shapes
import info.fekri.dunibazaar.util.MyScreens

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) { SignInScreen() }
    }
}

@Composable
fun SignInScreen() {
    val navigation = getNavController()
    val viewModel = getNavViewModel<SignInViewModel>()

    Box {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(Blue)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.80f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconApp()
            MainCardView(navigation, viewModel) {
                /* sign in */
                viewModel.signInUser()
            }

        }

    }

}

@Composable
fun IconApp() {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .size(64.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_icon_app),
            contentDescription = null,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
fun MainCardView(navigation: NavController, viewModel: SignInViewModel, SignInEvent: () -> Unit) {
    val email = viewModel.email.observeAsState("")
    val password = viewModel.password.observeAsState("")
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 10.dp,
        shape = Shapes.medium
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sign In",
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
                style = TextStyle(color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )

            MainTextField(
                edtValue = email.value,
                icon = R.drawable.ic_email,
                hint = "Email",
            ) { viewModel.email.value = it }

            PasswordTextField(
                edtValue = password.value,
                icon = R.drawable.ic_password,
                hint = "Password",
            ) { viewModel.password.value = it }


            Button(
                onClick = {
                    // check use input
                    if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                            SignInEvent.invoke() // call SignInEvent
                        } else {
                            Toast.makeText(
                                context,
                                "Email format is not correct!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please, fill out the blanks!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .padding(top = 28.dp, bottom = 18.dp)
            ) {
                Text(
                    text = "Login",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 18.dp)
            ) {

                Text(text = "Don't have an Account?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    // go to SignInScreen if user have account
                    // and empty the popUpTo to not to comeback to SignInScreen
                    navigation.navigate(MyScreens.SignUpScreen.route) {
                        popUpTo(MyScreens.SignInScreen.route) {
                            inclusive = true
                        }
                    }
                }) { Text(text = "Register here") }

            }

        }

    }

}

@Composable
fun PasswordTextField(
    edtValue: String,
    icon: Int,
    hint: String,
    onValueChanges: (String) -> Unit
) {
    val passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        placeholder = { Text(hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible.value) painterResource(id = R.drawable.ic_invisible)
            else painterResource(id = R.drawable.ic_visible)

            Icon(
                painter = image,
                contentDescription = null,
                modifier = Modifier.clickable { passwordVisible.value = !passwordVisible.value }
            )

        }
    )
}

@Composable
fun MainTextField(
    edtValue: String,
    icon: Int,
    hint: String,
    onValueChanges: (String) -> Unit
) {
    OutlinedTextField(
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        placeholder = { Text(hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) }
    )
}

