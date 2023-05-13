package info.fekri.dunibazaar.ui.features.cartScreen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import info.fekri.dunibazaar.R
import info.fekri.dunibazaar.model.data.Product
import info.fekri.dunibazaar.ui.features.profileScreen.AddUserLocationDataDialog
import info.fekri.dunibazaar.ui.theme.Blue
import info.fekri.dunibazaar.ui.theme.PriceBackground
import info.fekri.dunibazaar.ui.theme.Shapes
import info.fekri.dunibazaar.util.MyScreens
import info.fekri.dunibazaar.util.NetworkChecker
import info.fekri.dunibazaar.util.PAYMENT_PENDING
import info.fekri.dunibazaar.util.stylePrice

@Composable
fun CartScreen() {

    val navigation = getNavController()
    val context = LocalContext.current

    val viewModel = getNavViewModel<CartViewModel>()
    viewModel.loadCartData()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 74.dp)
        ) {

            CartToolbar(OnBackClicked = {
                navigation.popBackStack()
            }, OnProfileClicked = {
                navigation.navigate(MyScreens.ProductScreen.route)
            })

            if (viewModel.productList.value.isNotEmpty()) {

                CartList(data = viewModel.productList.value,
                    isChangingNumber = viewModel.isChangingNumber.value,
                    onAddItemClicked = {
                        viewModel.addItem(it)
                    },
                    onRemoveItemClicked = {
                        viewModel.removeItem(it)
                    },
                    onItemClicked = {
                        navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
                    })

            } else {
                NoDataAnimation()
            }
        }

        PurchaseAll(context, totalPrice = viewModel.totalPrice.value.toString()) {

            if (viewModel.productList.value.isNotEmpty()) {

                val locationData = viewModel.getUserLocation()
                if (locationData.first == "click to add" || locationData.second == "click to add") {
                    viewModel.getDataDialogState.value = true
                } else {
                    // payment
                    viewModel.purchaseAll(
                        locationData.first,
                        locationData.second
                    ) { success, link ->
                        if (success) {

                            Toast.makeText(context, "Paying by using ZarinPal...", Toast.LENGTH_SHORT).show()

                            viewModel.setPaymentStatus(PAYMENT_PENDING)

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)

                        } else {
                            Toast.makeText(context, "Problem occurred while payment process!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                Toast.makeText(context, "Please, add some Product first!", Toast.LENGTH_SHORT).show()
            }
        }

        if (viewModel.getDataDialogState.value) {

            AddUserLocationDataDialog(
                showSaveLocation = true,
                onDismiss = { viewModel.getDataDialogState.value = false },
                onSubmitClicked = { address, postalCode, isChecked ->
                    if (NetworkChecker(context).isInternetConnected) {

                        if (isChecked) {
                            viewModel.setUserLocation(address, postalCode)
                        }

                        viewModel.purchaseAll(
                            address,
                            postalCode
                        ) { success, link ->
                            if (success) {

                                Toast.makeText(context, "Paying by using ZarinPal...", Toast.LENGTH_SHORT).show()

                                viewModel.setPaymentStatus(PAYMENT_PENDING)

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                context.startActivity(intent)

                            } else {
                                Toast.makeText(context, "Problem occurred while payment process!", Toast.LENGTH_SHORT).show()
                            }
                        }

                    } else {
                        Toast.makeText(
                            context,
                            "Please, check your Internet Connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
}

@Composable
fun PurchaseAll(context: Context, totalPrice: String, onPurchaseClicked: () -> Unit) {
    val configuration = LocalConfiguration.current
    val fraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.07f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        onPurchaseClicked.invoke()
                    } else {
                        Toast.makeText(
                            context,
                            "Please, check your Internet Connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(182.dp, 40.dp)
            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "Let's Purchase!",
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }

            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(Shapes.large),
                color = PriceBackground
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                    text = "total: ${stylePrice(totalPrice)}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }
        }
    }

}

@Composable
fun NoDataAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.no_data)
    )
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}

@Composable
fun CartToolbar(
    OnBackClicked: () -> Unit, OnProfileClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { OnBackClicked.invoke() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        elevation = 2.dp,
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "My Cart",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                textAlign = TextAlign.Center
            )
        },
        actions = {
            IconButton(
                onClick = { OnProfileClicked.invoke() },
                modifier = Modifier.padding(end = 6.dp),
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            }
        }
    )
}

@Composable
fun CartList(
    data: List<Product>,
    isChangingNumber: Pair<String, Boolean>,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onItemClicked: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(data.size) {
            CartItem(
                data = data[it],
                isChangingNumber = isChangingNumber,
                onAddItemClicked = onAddItemClicked,
                onRemoveItemClicked = onRemoveItemClicked,
                onItemClicked = onItemClicked
            )
        }
    }

}

@Composable
fun CartItem(
    data: Product,
    isChangingNumber: Pair<String, Boolean>,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onItemClicked: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onItemClicked.invoke(data.productId) },
        elevation = 4.dp,
        shape = Shapes.large
    ) {
        Column {
            AsyncImage(
                model = data.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = data.name,
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "From ${data.category} Group",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = "Product authenticity guarantee",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Available in stock to ship",
                        style = TextStyle(fontSize = 14.sp)
                    )

                    Surface(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 6.dp)
                            .clip(Shapes.large),
                        color = PriceBackground
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp
                            ), text = stylePrice(
                                (data.price.toInt() * (data.quantity ?: "1").toInt()).toString()
                            ), style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        )
                    }

                }

                Surface(
                    modifier = Modifier
                        .padding(bottom = 14.dp, end = 8.dp)
                        .align(Alignment.Bottom)
                ) {
                    Card(
                        border = BorderStroke(2.dp, Blue)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            // minus / delete
                            if (data.quantity?.toInt() == 1) {
                                IconButton(onClick = { onRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp)
                                    )
                                }
                            } else {

                                IconButton(onClick = { onRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_minus),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp)
                                    )
                                }

                            }

                            // price - size of product
                            if (isChangingNumber.first == data.productId && isChangingNumber.second) {

                                Text(
                                    text = "...",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                            } else {

                                Text(
                                    text = data.quantity ?: "1",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                            }

                            // add button -> +
                            IconButton(onClick = { onAddItemClicked.invoke(data.productId) }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp, start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}